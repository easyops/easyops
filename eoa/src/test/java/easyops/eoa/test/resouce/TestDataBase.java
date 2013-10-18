package easyops.eoa.test.resouce;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBPartition;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;

public class TestDataBase {

	@Test
	public void testToJSon() {
		DataBase db = new DataBase();
		DBDomain domain = new DBDomain();
		domain.isPartition = false;
		domain.name = "basedb";
		List<DBServer> serverList = new ArrayList<DBServer>();
		DBServer server = new DBServer();
		server.address = "10.10.10.10";
		server.port = 5000;
		server.user = "dbmonitor";
		server.password="dbmonitor123";
		server.role = DBRole.MASTER;
		serverList.add(server);
		server = new DBServer();
		server.address = "10.10.10.10";
		server.port = 5001;
		server.user = "dbmonitor";
		server.password="dbmonitor123";
		server.role = DBRole.SLAVE;
		domain.serverList = serverList;
		db.dbList = new ArrayList<DBDomain>();
		db.dbList.add(domain);
		System.out.println(db.toJsonString());
		
		domain.isPartition = true;
		DBPartition p = new DBPartition();
		p.partitionId = "p1";
		p.serverlist = domain.serverList;
		domain.partitionList = new ArrayList<DBPartition>();
		domain.partitionList.add(p);
		domain.serverList = null;
		System.out.println(db.toJsonString());
		
		db.dbList.add(domain);
		System.out.println(db.toJsonString());

	}

}
