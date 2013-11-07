package easyops.eoa.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import easyops.eoa.resource.DataBase;
import easyops.eoa.ui.Shell;
import easyops.eoa.ui.arguments.Argument;

public class TestShell {

	@Before
	public void setup() {

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
				"-db=basedb.basedb_1, basedb.basedb_3, acctdb.acctdb_3"};
		Argument a = new Argument();
		new JCommander(a, par);
		assertEquals(a.id, "4test");
		assertEquals(a.dbCheckInteral, 3000);
		assertEquals(a.failCodes.length, 4);
		assertEquals(a.failCodes[0], 1);
		DataBase db = DataBase.buildDB(a.db);
		assertEquals(db.domainList.size(), 2);
		assertEquals(db.domainList.get(0).isPartition, false);
		assertEquals(db.domainList.get(0).name, "basedb");

	}

	@Test
	public void testShellInit() {

		String[] args = {
				"-command=start",
				"-id=4test",
				"-zkserver=" + TestZKBase.zkaddress,
				"-zkSessionTimeout=1000",
				"-dbcheckInterval=3000",
				"-dbCheckMaxTry=3",
				"-failCodes=1,3,4,5",
				"-freezeTime=10000",
				"-db=basedb.basedb_1,basedb.basedb_3,acctdb.acctdb_3"};
		Shell.init(args);
	}

	@After
	public void complete() {

	}

}
