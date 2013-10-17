package easyops.eoa.monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimerTask;

import org.apache.zookeeper.ZooKeeper;

import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.ZNode;

public class DBMonitor extends TimerTask {

	DBServer server;
	Connection conn;
	int timeout;
	int[] failCodes;
	int maxTry;

	public DBMonitor(DBServer dbserver, int checkTimeout, int[] failCodes) {
		this.server = dbserver;
		this.timeout = checkTimeout;
		this.failCodes = failCodes;
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
		reportStatus("down:" + message);

	}

	private void reportRunning() {
		reportStatus("running");
	}

	private void reportStatus(String status) {
		ZNode node = server.znode;
		if (node.children.size() == 0) {
			ZNode snode = node.addChild(ZNode.STATUS);
			snode.setData(status);
			snode.create();
			ZNode cnode = node.addChild(ZNode.CHECH_IN_STAMP);
			cnode.setData("" + System.currentTimeMillis());
			cnode.create();
		} else {
			for (ZNode n : node.children) {
				if (ZNode.STATUS.equals(n.name)) {
					n.setData(status);
				} else if (ZNode.CHECH_IN_STAMP.equals(n.name)) {
					n.setData("" + System.currentTimeMillis());
				}
				n.save();
			}
		}
	}

}
