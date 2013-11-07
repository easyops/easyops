package easyops.eoa.test.ui;

import static org.junit.Assert.fail;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import easyops.eoa.Agent;
import easyops.eoa.resource.DataBase;
import easyops.eoa.ui.arguments.Argument;

public class TestAgent extends TestZKBase {

	@Before
	public void setup() throws Exception {
		super.init();

	}

	@Test
	public void testDbDown() {

	}

	@After
	public void complete() {
		super.shutdown();
	}

	@Test
	public void testDBRegister() {

		Argument arg = initArguments();
		Agent agent = startAgent(arg);

		/*
		 * check result
		 */
		ZooKeeper zk = agent.getZk();
		String path = "/runtime/database/mysql/basedb/servers/basedb_1";
		try {
			Stat stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}
			path = "/runtime/database/mysql/basedb/" + DataBase.ACTIVE_NODE;
			stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}
			path = "/runtime/database/mysql/acctdb/servers/acctdb_3";
			stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}

			path = "/runtime/database/mysql/acctdb/" + DataBase.ACTIVE_NODE;
			stat = zk.exists(path, false);
			if (stat != null) {
				fail();
			}

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

	public static Agent startAgent(Argument arg) {
		Agent agent = new Agent(arg);

		try {
			agent.start();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		agent.waitUnitOnceDBMonitor();
		return agent;
	}

	public static Argument initArguments() {
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
		arg.db = "basedb.basedb_1, basedb.basedb_3 , acctdb.acctdb_3";
		return arg;
	}
}
