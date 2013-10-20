package easyops.eoa.test.ui;

import static org.junit.Assert.*;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import easyops.eoa.Agent;
import easyops.eoa.resource.ZNode;
import easyops.eoa.ui.arguments.Argument;

public class TestMonitor extends TestZKBase {

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
