package easyops.eoa.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import easyops.eoa.Agent;
import easyops.eoa.controller.IDBController;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DBStatus;
import easyops.eoa.resource.ZNode;
import easyops.eoa.ui.arguments.Argument;

public class TestMonitor extends TestZKBase {

	@Before
	public void setup() {
		super.init();
	}

	@Test
	public void testDBSlaveDown() {
		Argument arg = TestAgent.initArguments();
		Agent agent = TestAgent.startAgent(arg);
		ZooKeeper zk = agent.getZk();
		String path = "/runtime/database/mysql/basedb/servers/10.10.10.10:5001";
		try {
			Stat stat = zk.exists(path, false);
			if(stat == null){
				fail();
			}
			IDBController con=null;
			for(IDBController controller: agent.dbControllers){
				DBServer server = controller.getServer();
				if(server.address.equals("10.10.10.10") && server.port == 5001){
					con = controller;
					controller.shutDownDB();
				}
			}
			if(con==null){
				fail("can't find dbserver");
			}
			agent.restartOnceDBMonitor();
			agent.waitUnitOnceDBMonitor();
			
			String data = ZNode.bytes2String(zk.getData(path, false, stat));
			System.out.println(data);
			DBServer server = DBServer.fromJson(data);
			assertEquals(DBStatus.Running2Down, server.status);			
			
		} catch (KeeperException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
	}

	@After
	public void complete() {
		super.shutdown();
	}

	@Test
	public void testStartMonitor() {
		Argument arg = TestAgent.initArguments();
		Agent agent = TestAgent.startAgent(arg);

		/*
		 * check result
		 */
		ZooKeeper zk = agent.getZk();
		String path;
		Stat stat;
		path = "/runtime/database/mysql/basedb/lock";
		try {
			stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}

			String data = ZNode.bytes2String(zk.getData(path, false, stat));
			assertEquals("10.10.10.10:5000", data);
			path = "/runtime/database/mysql/acctdb/lock";
			stat = zk.exists(path, false);
			if (stat != null) {
				fail();
			}
			agent.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

}
