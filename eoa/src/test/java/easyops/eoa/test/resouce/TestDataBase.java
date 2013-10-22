package easyops.eoa.test.resouce;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

import org.junit.Test;

import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBPartition;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;

public class TestDataBase {

	@Test
	public void testToJSon() {
		DataBase db = buildDB();
		System.out.println(db.toJsonString());

	}

	@Test
	public void testDomain() {
		DataBase db = buildDB();
		for (DBDomain domain : db.dbList) {
			if ("basedb".equals(domain.name)) {
				List<DBServer> serverList = domain.getSlaveServerList();
				assertEquals(true, serverList != null);
				assertEquals(1, serverList.size());
				assertEquals(DBRole.SLAVE, serverList.get(0).role);
				serverList = domain.getAllServerList();
				assertEquals(true, serverList != null);
				assertEquals(2, serverList.size());				
			}else if("acctdb".equals(domain.name)){
				List<DBServer> serverList = domain.getSlaveServerList();
				assertEquals(true, serverList != null);
				assertEquals(1, serverList.size());
				assertEquals(DBRole.SLAVE, serverList.get(0).role);
				serverList = domain.getAllServerList();
				assertEquals(true, serverList != null);
				assertEquals(1, serverList.size());				
			}
		}
	}

	private DataBase buildDB() {
		DataBase db = new DataBase();
		DBDomain domain = new DBDomain();
		domain.isPartition = false;
		domain.name = "basedb";
		List<DBServer> serverList = new ArrayList<DBServer>();
		DBServer server = new DBServer();
		server.address = "10.10.10.10";
		server.port = 5000;
		server.user = "dbmonitor";
		server.password = "dbmonitor123";
		server.role = DBRole.MASTER;
		serverList.add(server);
		
		server = new DBServer();
		server.address = "10.10.10.10";
		server.port = 5001;
		server.user = "dbmonitor";
		server.password = "dbmonitor123";
		server.role = DBRole.SLAVE;
		serverList.add(server);
		domain.serverList = serverList;
		
		db.dbList = new ArrayList<DBDomain>();
		db.dbList.add(domain);
		System.out.println(db.toJsonString());

		domain = new DBDomain();

		domain.name = "acctdb";
		domain.isPartition = true;
		DBPartition p = new DBPartition();
		p.partitionId = "p1";

		serverList = new ArrayList<DBServer>();
		server = new DBServer();
		server.address = "10.10.10.10";
		server.port = 5001;
		server.user = "dbmonitor";
		server.password = "dbmonitor123";
		server.role = DBRole.SLAVE;
		serverList.add(server);

		p.serverlist = serverList;
		domain.partitionList = new ArrayList<DBPartition>();
		domain.partitionList.add(p);
		domain.serverList = null;
		System.out.println(db.toJsonString());

		db.dbList.add(domain);
		return db;
	}

}
