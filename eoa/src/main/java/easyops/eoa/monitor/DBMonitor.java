package easyops.eoa.monitor;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import easyops.eoa.base.ZNode;
import easyops.eoa.controller.IDBController;
import easyops.eoa.resource.DBRole;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DBStatus;

public class DBMonitor extends TimerTask {

	DBServer server;
	boolean masterAutoActive;
	IDBController controller;
	private CountDownLatch dbMonitorInitLatch;
	private long runCount = 0;
	private static Logger log = Logger.getLogger(DBMonitor.class);

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
		log.info("DB(" + server.getMark() + ") is down ");
		if (server.getStatus() == DBStatus.Active) {
			server.setStatus(DBStatus.Active2Down);
			log.info("DB(" + server.getMark() + ") from active to down");
		} else if (server.getStatus() == DBStatus.Running) {
			server.setStatus(DBStatus.Running2Down);
			log.info("DB(" + server.getMark() + ") from running to down");
		} else {
			server.setStatus(DBStatus.Down);
			log.debug("DB(" + server.getMark() + ") is still down");
		}
		server.checkInStamp = System.currentTimeMillis();
		server.znode.data = server.toJsonBytes();
		server.znode.save();
		if (server.getStatus() == DBStatus.Active2Down) {
			server.deactive();
		}
	}

	private void reportRunning() {
		log.debug("db(" + server.getMark() + ", status is "
				+ server.getStatus() + ") is running");
		if (server.getStatus() != DBStatus.Active
				&& server.getStatus() != DBStatus.Running) {
			server.setStatus(DBStatus.Running);
			log.debug("change db (" + server.getMark() + ") status to running");
		}
		if (server.role == DBRole.MASTER) {
			if (server.getStatus() == DBStatus.Running) {
				if (masterAutoActive || isNoLock()) {
					server.active();
					log.info("Master DB(" + server.getMark()
							+ ") has been actived ");
				}
			}else if(server.getStatus() == DBStatus.Active){
				if(isNoLock()){
					if(server.lockNode()){
						server.lockActiveNode();
					}
					
				}
			}
		}

		server.checkInStamp = System.currentTimeMillis();
		server.znode.data = server.toJsonBytes();
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
