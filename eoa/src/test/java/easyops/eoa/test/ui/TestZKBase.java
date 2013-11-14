package easyops.eoa.test.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;

import easyops.eoa.controller.DBControllerFactory;
import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;
import easyops.eoa.test.controller.SimuDBController;

public class TestZKBase implements Watcher {
	public static String zkaddress;
	ZooKeeper zookeeper;
	CountDownLatch latch;

	public void init() throws Exception {
		DBControllerFactory.clazz = SimuDBController.class;
		zkaddress = "localhost:2181";
		zkaddress = "10.10.12.83:2181";

		try {
			zookeeper = new ZooKeeper(zkaddress, 1000, this);
			latch = new CountDownLatch(1);
			waitUntilConnected(zookeeper, latch);
			Stat stat = zookeeper.exists("/runtime", false);
			if (stat != null) {
				deleteAllNode("/runtime");
			}

			String path = "/runtime";
			createNode(zookeeper, path);
			path += "/database";
			createNode(zookeeper, path);
			path += "/mysql";
			createNode(zookeeper, path);
			DataBase db = initDB();
			db = init83DB();
			for (DBDomain domain : db.domainList) {
				String tp = path + "/" + domain.name;
				createNode(zookeeper, tp);
				String tsp = tp + "/servers";
				createNode(zookeeper, tsp);
				for (DBServer server : domain.serverList) {
					createNode(zookeeper, tsp + "/" + server.serverName,
							server.toJsonString());
				}
			}
		} catch (KeeperException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			latch.countDown();
		}
	}

	private void waitUntilConnected(ZooKeeper zk, CountDownLatch latch) {
		if (States.CONNECTING == zk.getState()) {
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}

	}

	public void shutdown() {
		try {
			zookeeper.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void deleteAllNode(String path) {
		try {
			List<String> chilren = zookeeper.getChildren(path, false);
			for (String p : chilren) {
				deleteAllNode(path + "/" + p);
			}
			zookeeper.delete(path, -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void createNode(ZooKeeper zk, String path, String data)
			throws Exception {
		if (data == null) {
			zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			zk.create(path, data.getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}
	}

	private void createNode(ZooKeeper zk, String path) throws Exception {
		createNode(zk, path, null);
	}

	private DataBase init83DB() {
		DataBase db = new DataBase();

		DBDomain domain = new DBDomain();
		domain.isPartition = false;
		domain.name = "acctdb";
		domain.serverList = new ArrayList<DBServer>();

		DBServer server = new DBServer();
		server.address = "10.10.12.83";
		server.port = 3306;
		server.role = DBRole.MASTER;
		server.serverName = "acctdb_1";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		server.agentId = server.address;
		domain.serverList.add(server);

		server = new DBServer();
		server.address = "10.10.12.155";
		server.agentId = server.address;
		server.port = 6306;
		server.role = DBRole.SLAVE;
		server.serverName = "acctdb_2";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		domain.serverList.add(server);
		
		db.domainList.add(domain);

		return db;
	}

	private DataBase initDB() {
		DataBase db = new DataBase();

		DBDomain domain = new DBDomain();
		domain.isPartition = false;
		domain.name = "basedb";
		domain.serverList = new ArrayList<DBServer>();

		DBServer server = new DBServer();
		server.address = "10.10.10.100";
		server.port = 5000;
		server.role = DBRole.MASTER;
		server.serverName = "basedb_1";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		server.agentId = server.address;
		domain.serverList.add(server);

		server = new DBServer();
		server.address = "10.10.10.101";
		server.agentId = server.address;
		server.port = 5001;
		server.role = DBRole.SLAVE;
		server.serverName = "basedb_2";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		domain.serverList.add(server);

		server = new DBServer();
		server.address = "10.10.10.100";
		server.agentId = server.address;
		server.port = 5003;
		server.role = DBRole.SLAVE;
		server.serverName = "basedb_3";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		domain.serverList.add(server);

		db.domainList.add(domain);

		domain = new DBDomain();
		domain.isPartition = false;
		domain.name = "acctdb";

		server = new DBServer();
		server.address = "10.10.10.100";
		server.agentId = server.address;
		server.port = 5001;
		server.role = DBRole.SLAVE;
		server.serverName = "acctdb_2";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		domain.serverList.add(server);

		server = new DBServer();
		server.address = "10.10.10.101";
		server.agentId = server.address;
		server.port = 5000;
		server.role = DBRole.MASTER;
		server.serverName = "acctdb_1";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		domain.serverList.add(server);

		server = new DBServer();
		server.address = "10.10.10.100";
		server.agentId = server.address;
		server.port = 5002;
		server.role = DBRole.SLAVE;
		server.serverName = "acctdb_3";
		server.user = "dbmonitor";
		server.password = "Qwert135%";
		domain.serverList.add(server);

		db.domainList.add(domain);

		return db;
	}
}
