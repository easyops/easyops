package easyops.eoa.monitor;

import java.sql.Connection;
import java.util.TimerTask;

import easyops.eoa.resource.DBServer;

public class DBMonitor extends TimerTask {
	
	DBServer server;
	Connection conn;
	
	public DBMonitor( DBServer dbserver){
		this.server = dbserver;
	}

	@Override
	public void run() {

		
	}

}
