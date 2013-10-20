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
			if(dbMonitorInitLatch!=null){
				dbMonitorInitLatch.countDown();
				dbMonitorInitLatch = null;
			}
			setRunCount(getRunCount() + 1);

		} catch (Throwable e) {
			e.printStackTrace();
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
		server.checkInStamp = System.currentTimeMillis();
		server.znode.data = server.toJsonBytes();
		server.znode.save();
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

		if (server.status != DBStatus.Active
				|| server.status != DBStatus.Running) {
			server.status = DBStatus.Running;
		}

		server.checkInStamp = System.currentTimeMillis();
		server.znode.data = server.toJsonBytes();
		server.znode.save();

		if (server.status == DBStatus.Running && server.role == DBRole.MASTER) {

			if (masterAutoActive || isNoLock()) {
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

	private boolean isNoLock() {
		ZNode lockNode = server.znode.pnode.pnode.getChild(ZNode.ACTIVE_LOCK);
		return lockNode == null || (lockNode.exists() && lockNode.stat == null);
	}
	
	public void restartOneMonitor(CountDownLatch dbMonitorInitLatch){
		this.dbMonitorInitLatch = dbMonitorInitLatch;
		
	}

	public long getRunCount() {
		return runCount;
	}

	public void setRunCount(long runCount) {
		this.runCount = runCount;
	}

}
