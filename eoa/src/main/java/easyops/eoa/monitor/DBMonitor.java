package easyops.eoa.monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimerTask;

import org.apache.zookeeper.CreateMode;

import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DBStatus;
import easyops.eoa.resource.ZNode;

public class DBMonitor extends TimerTask {

	DBServer server;
	Connection conn;
	int timeout;
	int[] failCodes;
	int maxTry;
	boolean masterAutoActive;

	public DBMonitor(DBServer dbserver, int checkTimeout, int[] failCodes,
			boolean masterAutoActive) {
		this.server = dbserver;
		this.timeout = checkTimeout;
		this.failCodes = failCodes;
		this.masterAutoActive = masterAutoActive;
	}

	@Override
	public void run() {
		try {
			checkDB();

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private void checkDB() {
		checkDB(0);
	}

	private boolean isFailCode(int code) {
		for (int fc : failCodes) {
			if (code == fc) {
				return true;
			}
		}
		return false;
	}

	private void checkDB(int count) {
		if (conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
				return;
			}
			try {
				conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:port", server.user,
						server.getPassword());
			} catch (SQLException e) {
				e.printStackTrace();
				if (isFailCode(e.getErrorCode())) {
					if (count >= maxTry) {
						reportDown("check connection error : " + e.getMessage()
								+ " try " + count);
						return;
					} else {
						checkDB(count + 1);
						return;
					}
				}
			}
		}

		if (conn == null) {
			if (count >= maxTry) {
				reportDown("Connection is not valid! try  " + count);
				return;
			} else {
				checkDB(count + 1);
				return;
			}
		} else {
			try {
				if (conn.isValid(timeout)) {
					reportRunning();
					return;
				} else {
					if (count >= maxTry) {
						reportDown("Connection is not valid! try " + count);
						return;
					} else {
						checkDB(count + 1);
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				if (isFailCode(e.getErrorCode())) {
					if (count >= maxTry) {
						reportDown("check connection error : " + e.getMessage()
								+ " try " + count);
						return;
					} else
						checkDB(count + 1);
					return;
				}
			}
		}
	}

	private void reportDown(String message) {
		if (server.status == DBStatus.Active) {
			server.status = DBStatus.Active2Down;
		} else if (server.status == DBStatus.Running) {
			server.status = DBStatus.Running2Down;
		} else {
			server.status = DBStatus.Down;
		}
		reportStatus(DBStatus.getStatus(server.status) + message);
		if (server.status == DBStatus.Active2Down) {
			ZNode lockNode = server.znode.pnode.pnode
					.getChild(ZNode.ACTIVE_LOCK);
			if (lockNode != null) {
				lockNode.sychronize();
				String mark = lockNode.getStringData();
				if (server.getMark().equals(mark)) {
					lockNode.delete();
				}
			}

		}
	}

	private void reportRunning() {

		if (server.status == DBStatus.Active
				|| server.status != DBStatus.Running) {
			server.status = DBStatus.Running;
		}

		reportStatus(DBStatus.getStatus(server.status));

		if (server.status == DBStatus.Running && server.role == DBRole.MASTER) {
			if (masterAutoActive) {
				ZNode lockNode = server.znode.pnode.pnode
						.getChild(ZNode.ACTIVE_LOCK);
				if (lockNode == null) {
					lockNode = server.znode.pnode.pnode
							.addChild(ZNode.ACTIVE_LOCK);
					lockNode.setData(server.getMark());
					lockNode.create();
				} else {
					lockNode.setData(server.getMark());
					lockNode.save();
				}
			}
		}
	}
	
	private void reportStatus(String status){
		reportStatus(server.znode, status);
		if(server.role == DBRole.MASTER){
			ZNode mnode = server.znode.pnode.pnode.getChild(ZNode.MASTER);
			if(mnode == null){
				mnode = server.znode.pnode.pnode.addChild(ZNode.MASTER);
				mnode.createMode = CreateMode.PERSISTENT;
				mnode.setData(server.getMark());
				mnode.create();
			}
			reportStatus(mnode, status);
		}
	}

	private void reportStatus(ZNode node, String status) {

		ZNode snode = node.getChild(ZNode.STATUS);
		ZNode cnode = node.getChild(ZNode.CHECK_IN_STAMP);
		if (snode == null) {
			snode = node.addChild(ZNode.STATUS);
			snode.setData(status);
			snode.create();
		} else {
			snode.setData(status);
			snode.save();
		}
		if (cnode == null) {
			cnode = node.addChild(ZNode.CHECK_IN_STAMP);
			cnode.setData("" + System.currentTimeMillis());
			cnode.create();
		} else {
			cnode.setData("" + System.currentTimeMillis());
			cnode.save();
		}

	}

}
