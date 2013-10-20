package easyops.eoa.test.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import easyops.eoa.Agent;
import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;
import easyops.eoa.resource.ZNode;
import easyops.eoa.ui.arguments.Argument;

public class TestAgent extends TestZKBase {

	@Before
	public void setup() {
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
		String path = "/runtime/database/mysql/basedb/servers/10.10.10.10:5000";
		try {
			Stat stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}
			path = "/runtime/database/mysql/basedb/" + ZNode.MASTER;
			stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}
			path = "/runtime/database/mysql/acctdb/servers/10.10.10.10:5002";
			stat = zk.exists(path, false);
			if (stat == null) {
				fail();
			}

			path = "/runtime/database/mysql/acctdb/master";
			stat = zk.exists(path, false);
			if (stat != null) {
				fail();
			}

			agent.shutdown();

		} catch (KeeperException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
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
		return arg;
	}
}
