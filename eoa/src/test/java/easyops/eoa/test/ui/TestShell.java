package easyops.eoa.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import easyops.eoa.Agent;
import easyops.eoa.controller.DBControllerFactory;
import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;
import easyops.eoa.resource.ZNode;
import easyops.eoa.test.controller.SimuDBController;
import easyops.eoa.ui.Shell;
import easyops.eoa.ui.arguments.Argument;

public class TestShell implements Watcher {
	private static String zkaddress;
	ZooKeeper zookeeper;
	CountDownLatch latch;

	@Before
	public void setup() {
		DBControllerFactory.clazz = SimuDBController.class;
		zkaddress = "localhost:2181";
		
		try {
			zookeeper = new ZooKeeper(zkaddress, 1000, this);
			latch = new CountDownLatch(1);
			waitUntilConnected(zookeeper, latch);
			Stat stat;

			stat = zookeeper.exists("/rumtime", false);
			if (stat != null) {
				zookeeper.delete("/runtime", stat.getVersion());
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testVersion() {
		String[] args = { "-command=version" };
		Argument a = new Argument();
		new JCommander(a, args);
		assertTrue("read version parameter success!",
				a.command.equals("version"));
	}

	@Test
	public void testALLParameter() {
		String[] par = {
				"-command=start",
				"-id=4test",
				"-zkserver=10.10.10.10:200,10.10.10.11:200",
				"-zkSessionTimeout=1000",
				"-dbcheckInterval=3000",
				"-dbCheckMaxTry=3",
				"-failCodes=1,3,4,5",
				"-freezeTime=10000",
				"-db={\"dbList\":[{\"name\":\"basedb\",\"isPartition\":false,\"serverList\":[{\"address\":\"10.10.10.10\",\"user\":\"dbmonitor\",\"password\":\"dbmonitor123\",\"port\":5000,\"role\":\"MASTER\",\"status\":\"Down\",\"freezeStamp\":0}]}]}" };
		Argument a = new Argument();
		new JCommander(a, par);
		assertEquals(a.id, "4test");
		assertEquals(a.dbCheckInteral, 3000);
		assertEquals(a.failCodes.length, 4);
		assertEquals(a.failCodes[0], 1);
		DataBase db = DataBase.buildDB(a.db);
		assertEquals(db.dbList.size(), 1);
		assertEquals(db.dbList.get(0).isPartition, false);
		assertEquals(db.dbList.get(0).name, "basedb");

	}

	@Test
	public void testMainRun() {
		String[] args = {
				"-command=start",
				"-id=4test",
				"-zkserver=" + zkaddress,
				"-zkSessionTimeout=1000",
				"-dbcheckInterval=3000",
				"-dbCheckMaxTry=3",
				"-failCodes=1,3,4,5",
				"-freezeTime=10000",
				"-db={\"dbList\":[{\"name\":\"basedb\",\"isPartition\":false,\"serverList\":[{\"address\":\"10.10.10.10\",\"user\":\"dbmonitor\",\"password\":\"dbmonitor123\",\"port\":5000,\"role\":\"MASTER\",\"status\":\"Down\",\"freezeStamp\":0}]}]}" };
		Shell.main(args);
	}

	@Test
	public void testDBRegister() {
		Argument arg = new Argument();
		arg.command = "start";
		arg.dbCheckInteral = 1000;
		arg.dbCheckTimeout = 1000;
		arg.dbCheckMaxTry = 3;
		arg.failCodes = new int[] {};
		arg.freezeTime = 1000;
		arg.id = "4test";
		arg.masterAutoActive = false;
		arg.zkserver = zkaddress;
		arg.zkSessionTimeout = 1000;
		DataBase db = new DataBase();
		db.dbList = new ArrayList<DBDomain>();
		DBDomain basedb = new DBDomain();
		basedb.name = "basedb";
		basedb.isPartition = false;
		basedb.serverList = new ArrayList<DBServer>();
		DBServer master = new DBServer();
		master.address = "10.10.10.10";
		master.port = 5000;
		master.user = "dbmonitor";
		master.password = "123";
		master.role = DBRole.MASTER;

		DBServer slave = new DBServer();
		slave.address = "10.10.10.10";
		slave.port = 5001;
		slave.user = "dbmonitor";
		slave.password = "1234";
		slave.role = DBRole.SLAVE;

		basedb.serverList.add(master);
		basedb.serverList.add(slave);

		db.dbList.add(basedb);

		DBDomain acctdb = new DBDomain();
		acctdb.name = "acctdb";
		acctdb.isPartition = false;
		acctdb.serverList = new ArrayList<DBServer>();

		slave = new DBServer();
		slave.address = "10.10.10.10";
		slave.port = 5002;
		slave.user = "dbmonitor";
		slave.password = "1234";
		slave.role = DBRole.SLAVE;
		acctdb.serverList.add(slave);
		db.dbList.add(acctdb);
		arg.db = db.toJsonString();
		Agent agent = new Agent(arg);
		try {
			agent.start();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		ZooKeeper zk = agent.getZk();
		String path = "/runtime/database/mysql/basedb/servers/10.10.10.10:5000";
		try {
			Stat stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}
			path = "/runtime/database/mysql/basedb/servers/"+ZNode.MASTER;
			stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}

		} catch (KeeperException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void testDBDown() {

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
	
	@After
	public void complete(){
		try {
			zookeeper.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
