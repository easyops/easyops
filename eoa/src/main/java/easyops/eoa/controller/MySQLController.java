package easyops.eoa.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import easyops.eoa.resource.DBServer;
import easyops.eoa.ui.Shell;
import easyops.eoa.ui.arguments.Argument;

public class MySQLController extends BaseDBController implements IDBController {

	private Connection conn;
	private int timeout;
	private int[] failCodes;
	private int maxTry;
	private int port;
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private String user;
	private String password;

	@Override
	public void checkDB() {
		checkDB(0);

	}

	private void checkDB(int count) {
		if (conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				Shell.shutdown();
				return;
			}
			try {

				conn = DriverManager.getConnection("jdbc:mysql://localhost:"
						+ port, user, password);
			} catch (SQLException e) {
				e.printStackTrace();
				if (isFailCode(e.getErrorCode())) {
					if (count >= maxTry) {
						setValid(false);
						setMessage("check connection error : " + e.getMessage()
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
				setValid(false);
				setMessage("Connection is not valid! try  " + count);
				return;
			} else {
				checkDB(count + 1);
				return;
			}
		} else {
			try {
				if (conn.isValid(timeout)) {
					setValid(true);
					return;
				} else {
					if (count >= maxTry) {
						setValid(false);
						setMessage("Connection is not valid! try " + count);
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
						setValid(false);
						setMessage("check connection error : " + e.getMessage()
								+ " try " + count);
						return;
					} else
						checkDB(count + 1);
					return;
				}
			}
		}
	}

	private boolean isFailCode(int code) {
		for (int fc : failCodes) {
			if (code == fc) {
				return true;
			}
		}
		return false;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int[] getFailCodes() {
		return failCodes;
	}

	public void setFailCodes(int[] failCodes) {
		this.failCodes = failCodes;
	}

	public int getMaxTry() {
		return maxTry;
	}

	public void setMaxTry(int maxTry) {
		this.maxTry = maxTry;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void init(DBServer dbserver, Argument arg) {
		this.server = dbserver;
		setFailCodes(arg.failCodes);
		setMaxTry(arg.dbCheckMaxTry);
		setTimeout(arg.dbCheckTimeout);
		setPort(server.port);
		setAddress(server.address);
		setPassword(server.getPassword());
		setUser(server.user);
	}

	@Override
	public void shutDownDB() {
		
		
	}

	@Override
	public void startDB() {
		// TODO Auto-generated method stub
		
	}
	

}
