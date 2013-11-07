package easyops.eoa.test.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import easyops.eoa.Agent;
import easyops.eoa.base.ZNode;
import easyops.eoa.controller.IDBController;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DBStatus;
import easyops.eoa.resource.DataBase;
import easyops.eoa.test.ui.TestAgent;
import easyops.eoa.test.ui.TestZKBase;
import easyops.eoa.ui.arguments.Argument;

public class TestMonitor extends TestZKBase {

	@Before
	public void setup() throws Exception {
		super.init();
	}

	@Test
	public void testDBSlaveDown() {
		Argument arg = TestAgent.initArguments();
		Agent agent = TestAgent.startAgent(arg);
		ZooKeeper zk = agent.getZk();
		String path = "/runtime/database/mysql/basedb/servers/basedb_3";
		try {
			checkSlaveDown(agent, zk, path);
		} catch (KeeperException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		} finally {
			agent.shutdown();
		}
	}

	@Test
	public void testDBSlaveDown2UP() {
		Argument arg = TestAgent.initArguments();
		Agent agent = TestAgent.startAgent(arg);
		ZooKeeper zk = agent.getZk();
		IDBController con;
		String data;
		DBServer server;
		String path = "/runtime/database/mysql/basedb/servers/basedb_3";
		Stat stat = new Stat();
		try {
			checkSlaveDown(agent, zk, path);
			con = findDBController(agent, "10.10.10.100", 5003);
			if (con == null) {
				fail("can't find dbserver");
			}
			con.startDB();
			agent.waitUnitOnceDBMonitor();

			data = ZNode.bytes2String(zk.getData(path, false, stat));
			server = DBServer.fromJson(data);
			assertEquals(DBStatus.Running, server.getStatus());

		} catch (KeeperException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		} finally {
			agent.shutdown();
		}
	}

	private void checkSlaveDown(Agent agent, ZooKeeper zk, String path)
			throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, false);
		if (stat == null) {
			fail();
		}

		IDBController con = findDBController(agent, "10.10.10.100", 5003);
		if (con == null) {
			fail("can't find dbserver");
		}
		con.shutDownDB();
		agent.waitUnitOnceDBMonitor();

		String data = ZNode.bytes2String(zk.getData(path, false, stat));
		DBServer server = DBServer.fromJson(data);
		assertEquals(DBStatus.Running2Down, server.getStatus());
	}

	private IDBController findDBController(Agent agent, String address, int port) {
		IDBController con = null;
		for (IDBController controller : agent.dbControllers) {
			DBServer server = controller.getServer();
			if (server.address.equals(address) && server.port == port) {
				con = controller;
				controller.shutDownDB();
			}
		}
		return con;
	}

	@Test
	public void testDBMasterDown() {
		Argument arg = TestAgent.initArguments();
		Agent agent = TestAgent.startAgent(arg);
		agent.waitUnitOnceDBMonitor();
		ZooKeeper zk = agent.getZk();
		
		try {
			checkMasterDown(agent, zk);
		} catch (KeeperException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		} finally {
			agent.shutdown();
		}
	}

	@Test
	public void testDBMasterDown2UP() {
		Argument arg = TestAgent.initArguments();
		arg.masterAutoActive = true;
		Agent agent = TestAgent.startAgent(arg);
		agent.waitUnitOnceDBMonitor();
		ZooKeeper zk = agent.getZk();

		try {
			
			checkMasterDown(agent, zk);
			checkMasterUP(agent, zk);

		} catch (KeeperException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		} finally {
			agent.shutdown();
		}
	}

	private void checkMasterUP(Agent agent, ZooKeeper zk)
			throws KeeperException, InterruptedException {
		IDBController con;
		String data;
		DBServer server;
		String path;
		Stat stat = new Stat();
		con = findDBController(agent, "10.10.10.100", 5000);
		if (con == null) {
			fail("can't find dbserver");
		}
		con.startDB();
		agent.waitUnitOnceDBMonitor();
		path = "/runtime/database/mysql/basedb/servers/basedb_1";
		data = ZNode.bytes2String(zk.getData(path, false, stat));
		server = DBServer.fromJson(data);
		assertEquals(DBStatus.Active, server.getStatus());
		Thread.sleep(1000);
		path = "/runtime/database/mysql/basedb/"+DataBase.ACTIVE_LOCK;
		stat = zk.exists(path, false);
		if (stat == null) {

		} else {
			Assert.assertTrue("basedb_1:10.10.10.100:5000".equals(ZNode
					.bytes2String(zk.getData(path, false, stat))));
		}
		
		path = "/runtime/database/mysql/basedb/"+DataBase.ACTIVE_NODE;
		data = ZNode.bytes2String(zk.getData(path, false, stat));
		assertEquals("basedb_1:10.10.10.100:5000", data);

	}

	private void checkMasterDown(Agent agent, ZooKeeper zk)
			throws KeeperException, InterruptedException {
		String path = "/runtime/database/mysql/basedb/servers/basedb_1";
		Stat stat = zk.exists(path, false);
		if (stat == null) {
			fail();
		}
		path = "/runtime/database/mysql/basedb/lock";
		stat = zk.exists(path, false);
		if (stat == null) {
			fail();
		}
		String lockdata = ZNode.bytes2String(zk.getData(path, false, stat));
		assertEquals("basedb_1:10.10.10.100:5000", lockdata);
		IDBController con = findDBController(agent, "10.10.10.100", 5000);
		if (con == null) {
			fail("can't find dbserver");
		}
		con.shutDownDB();
		agent.waitUnitOnceDBMonitor();
		path = "/runtime/database/mysql/basedb/servers/basedb_1";
		String data = ZNode.bytes2String(zk.getData(path, false, stat));
		DBServer server = DBServer.fromJson(data);
		assertEquals(DBStatus.Active2Down, server.getStatus());
		Thread.sleep(1000);
		path = "/runtime/database/mysql/basedb/"+DataBase.ACTIVE_LOCK;
		stat = zk.exists(path, false);
		if (stat == null) {

		} else {
			lockdata = ZNode.bytes2String(zk.getData(path, false, stat));
			Assert.assertTrue(!"basedb_1:10.10.10.100:5000".equals(lockdata));
		}
		path = "/runtime/database/mysql/basedb/"+DataBase.ACTIVE_NODE;
		data = ZNode.bytes2String(zk.getData(path, false, stat));
		assertEquals("basedb_3:10.10.10.100:5003", data);
	}

	@After
	public void complete() {
		super.shutdown();
	}

	@Test
	public void testStartMonitor() {
		Argument arg = TestAgent.initArguments();
		Agent agent = TestAgent.startAgent(arg);
		agent.waitUnitOnceDBMonitor();

		/*
		 * check result
		 */
		ZooKeeper zk = agent.getZk();
		String path;
		Stat stat;
		path = "/runtime/database/mysql/basedb/"+DataBase.ACTIVE_LOCK;
		try {
			stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}

			String data = ZNode.bytes2String(zk.getData(path, false, stat));
			assertEquals("basedb_1:10.10.10.100:5000", data);
			path = "/runtime/database/mysql/basedb/"+DataBase.ACTIVE_NODE;
			data = ZNode.bytes2String(zk.getData(path, false, stat));
			assertEquals("basedb_1:10.10.10.100:5000", data);
			path = "/runtime/database/mysql/acctdb/"+DataBase.ACTIVE_LOCK;
			stat = zk.exists(path, false);
			if (stat != null) {
				fail();
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			agent.shutdown();
		}

	}

}
