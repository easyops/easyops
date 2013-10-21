package easyops.eoa.monitor;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import easyops.eoa.controller.IDBController;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DBStatus;
import easyops.eoa.resource.ZNode;

public class DBMonitor extends TimerTask {

	DBServer server;
	boolean masterAutoActive;
	IDBController controller;
	private CountDownLatch dbMonitorInitLatch;
	private long runCount = 0;

	public DBMonitor(IDBController controller, DBServer dbserver,
			boolean masterAutoActive) {
		this.server = dbserver;
		this.controller = controller;
		this.masterAutoActive = masterAutoActive;

	}

	@Override
	public void run() {
		try {
			controller.checkDB();
			if (controller.isValid()) {
				reportRunning();
			} else {
				reportDown(controller.getMessage());
			}
			if (dbMonitorInitLatch != null) {
				dbMonitorInitLatch.countDown();
				dbMonitorInitLatch = null;
			}
			setRunCount(getRunCount() + 1);

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private void reportDown(String message) {
		if (server.getStatus() == DBStatus.Active) {
			server.setStatus(DBStatus.Active2Down);
		} else if (server.getStatus() == DBStatus.Running) {
			server.setStatus(DBStatus.Running2Down);
		} else {
			server.setStatus(DBStatus.Down);
		}
		server.checkInStamp = System.currentTimeMillis();
		server.znode.data = server.toJsonBytes();
		server.znode.save();
		if (server.getStatus() == DBStatus.Active2Down) {
			ZNode.unLockMe(server);
		}
	}

	private void reportRunning() {

		if (server.getStatus() != DBStatus.Active
				&& server.getStatus() != DBStatus.Running) {
			server.setStatus(DBStatus.Running);
		}

		server.checkInStamp = System.currentTimeMillis();
		server.znode.data = server.toJsonBytes();
		if (server.getStatus() == DBStatus.Running
				&& server.role == DBRole.MASTER) {

			if (masterAutoActive || isNoLock()) {
				ZNode.lockMe(server);
			}
		}
		server.znode.save();
	}

	private boolean isNoLock() {
		ZNode lockNode = server.getLockNode();
		return lockNode == null || (lockNode.exists() && lockNode.stat == null);
	}

	public void restartOneMonitor(CountDownLatch dbMonitorInitLatch) {
		this.dbMonitorInitLatch = dbMonitorInitLatch;

	}

	public long getRunCount() {
		return runCount;
	}

	public void setRunCount(long runCount) {
		this.runCount = runCount;
	}

}
