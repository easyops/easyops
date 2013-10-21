package easyops.eoa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

import easyops.eoa.controller.DBControllerFactory;
import easyops.eoa.controller.IDBController;
import easyops.eoa.monitor.ActiveLockWatcher;
import easyops.eoa.monitor.DBMonitor;
import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBPartition;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;
import easyops.eoa.resource.ZNode;
import easyops.eoa.ui.arguments.Argument;

public class Agent implements Watcher {

	private ZNode zroot;
	private ZooKeeper zk;
	private CountDownLatch dbMonitorInitLatch;
	private List<Timer> dbTimers = new ArrayList<Timer>();
	public List<IDBController> dbControllers = new ArrayList<IDBController>();
	private List<DBServer> dbServers;
	private List<DBMonitor> dbMonitors = new ArrayList<DBMonitor>();

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}

	private Argument arg;
	private DataBase db;
	private CountDownLatch latch;

	public Agent(Argument arg) {
		this.arg = arg;
		this.db = DataBase.buildDB(this.arg.db);

	}

	public void check() {

	}

	public void start() throws IOException {
		buildZK();
		startMoniterDB();
		startMoniterZK();
	}

	private void startMoniterZK() {

	}

	private void startMoniterDB() {
		dbServers = db.getAllServerList();

		for (DBServer server : dbServers) {
			Timer timer = new Timer();
			IDBController controller = DBControllerFactory.getController();
			controller.init(server, arg);
			dbControllers.add(controller);
			DBMonitor m = new DBMonitor(controller, server,
					arg.masterAutoActive);
			dbMonitors.add(m);
			m.restartOneMonitor(dbMonitorInitLatch);
			timer.schedule(m, 1000, arg.dbCheckInteral);
			dbTimers.add(timer);
		}
	}

	private void buildZK() throws IOException {
		latch = new CountDownLatch(1);
		zk = new ZooKeeper(arg.zkserver, arg.zkSessionTimeout, this);
		waitUntilConnected();
		zroot = new ZNode(zk);
		ZNode node = zroot.addChild("runtime");
		node.create();
		node = node.addChild("database");
		node.create();
		node = node.addChild("mysql");
		node.create();
		buildDBDomain(node);
	}

	private void waitUntilConnected() {
		if (States.CONNECTING == zk.getState()) {
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}

	}

	private void restartOnceDBMonitor() {
		dbMonitorInitLatch = new CountDownLatch(dbServers.size());
		for (DBMonitor m : dbMonitors) {
			m.restartOneMonitor(dbMonitorInitLatch);
		}
	}

	public void waitUnitOnceDBMonitor() {
		try {
			restartOnceDBMonitor();
			dbMonitorInitLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void buildDBDomain(ZNode node) {
		for (DBDomain domain : db.dbList) {
			ZNode znode = node.addChild(domain.name);
			znode.createMode = CreateMode.PERSISTENT;
			znode.data = domain.toJsonBytes();
			znode.create();
			domain.znode = znode;
			buildDBServer(domain, znode);
		}
	}

	private void buildDBServer(DBDomain domain, ZNode domainNode) {
		if (domain.isPartition) {
			for (DBPartition partition : domain.partitionList) {
				ZNode znode = domainNode.addChild(partition.partitionId);
				znode.createMode = CreateMode.PERSISTENT;
				znode.create();
				partition.znode = znode;
				buildDBServer(partition.serverlist, znode);
			}
		} else {
			buildDBServer(domain.serverList, domainNode);
		}
	}

	private void buildDBServer(List<DBServer> serverList, ZNode pnode) {

		pnode = pnode.addChild(ZNode.DBSERVER_LIST);
		pnode.create();
		for (DBServer server : serverList) {
			ZNode znode = pnode.addChild(server.getMark());
			znode.data = server.toJsonBytes();
			znode.createMode = CreateMode.EPHEMERAL;
			znode.create();
			server.znode = znode;
			if (server.role == DBRole.MASTER) {
				ZNode mnode = server.znode.pnode.pnode.getChild(ZNode.MASTER);
				if (noMaster(mnode)) {
					if (mnode == null) {
						mnode = server.znode.pnode.pnode.addChild(ZNode.MASTER);
					}
					mnode.createMode = CreateMode.PERSISTENT;
					mnode.setData(server.getMark());
					mnode.create();
				}

			}
			server.setLockWatcher(new ActiveLockWatcher(server, arg.freezeTime));
		}

	}

	private boolean noMaster(ZNode mnode) {
		boolean isNone = false;
		if (mnode == null) {
			isNone = true;
		} else {
			mnode.exists();
			if (mnode.stat != null) {
				isNone = true;
			}
		}
		return isNone;
	}

	public void shutdown() {

		for (Timer timer : dbTimers) {
			timer.cancel();
		}
		try {
			zk.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {

		if (event.getState() == KeeperState.SyncConnected) {
			latch.countDown();
		}

	}

}
