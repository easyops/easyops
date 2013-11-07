package easyops.eoa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper.States;

import easyops.eoa.base.ZNode;
import easyops.eoa.controller.DBControllerFactory;
import easyops.eoa.controller.IDBController;
import easyops.eoa.monitor.ActiveLockWatcher;
import easyops.eoa.monitor.DBMonitor;
import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBPartition;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;
import easyops.eoa.ui.MyZooKeeper;
import easyops.eoa.ui.arguments.Argument;

public class Agent implements Watcher {

	private ZNode zroot;
	private MyZooKeeper zk;
	private CountDownLatch dbMonitorInitLatch;
	private List<Timer> dbTimers = new ArrayList<Timer>();
	public List<IDBController> dbControllers = new ArrayList<IDBController>();
	private List<DBServer> dbServers;
	private List<DBMonitor> dbMonitors = new ArrayList<DBMonitor>();

	public MyZooKeeper getZk() {
		return zk;
	}

	public void setZk(MyZooKeeper zk) {
		this.zk = zk;
	}

	private Argument arg;
	private DataBase database;
	private CountDownLatch latch;

	public Agent(Argument arg) {
		this.arg = arg;
		this.database = DataBase.buildDB(this.arg.db);

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
		dbServers = database.getAllServerList();

		for (DBServer server : dbServers) {
			Timer timer = new Timer();
			IDBController controller = DBControllerFactory.getController();
			controller.init(server, arg);
			server.controller = controller;
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
		zk = new MyZooKeeper(arg.zkserver, arg.zkSessionTimeout, this);
		waitUntilConnected();
		zroot = new ZNode(zk);
		ZNode node = zroot.addChild("runtime");
		node.sychronize();
		node = node.addChild("database");
		node.sychronize();
		node = node.addChild("mysql");
		node.sychronize();
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
		for (DBDomain domain : database.domainList) {
			ZNode znode = node.addChild(domain.name);
			znode.sychronize();
			domain.znode = znode;
			buildDBServer(domain, znode);
		}
	}

	private void buildDBServer(DBDomain domain, ZNode domainNode) {
		if (domain.isPartition) {
			for (DBPartition partition : domain.partitionList) {
				ZNode znode = domainNode.addChild(partition.partitionId);
				znode.sychronize();				
				partition.znode = znode;
				buildDBServer(partition.serverlist, znode);
			}
		} else {
			buildDBServer(domain.serverList, domainNode);
		}
	}

	private void buildDBServer(List<DBServer> serverList, ZNode pnode) {

		pnode = pnode.addChild(DataBase.DBSERVER_LIST);
		pnode.sychronize();		
		for (DBServer server : serverList) {
			ZNode znode = pnode.addChild(server.serverName);
			znode.sychronize();
			server.znode = znode;
			server.setValuefromJson(znode.getStringData());			
			server.initActiveNode();
			server.setLockWatcher(new ActiveLockWatcher(server, arg.freezeTime));
		}

	}

	public void shutdown() {

		for (Timer timer : dbTimers) {
			timer.cancel();
		}
		try {
			zk.close();
		} catch (InterruptedException e) {

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
