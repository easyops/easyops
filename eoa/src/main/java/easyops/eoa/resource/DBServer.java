package easyops.eoa.resource;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import easyops.eoa.base.BaseObject;
import easyops.eoa.base.ZNode;
import easyops.eoa.controller.IDBController;

public class DBServer extends BaseObject {

	@Expose
	public String address;
	@Expose
	public String user;
	@Expose
	public String password;
	@Expose
	public int port;
	@Expose
	public DBRole role = DBRole.SLAVE;
	@Expose
	private DBStatus status = DBStatus.Down;
	@Expose
	public String serverName="";

	public IDBController controller;

	public DBStatus getStatus() {
		return status;
	}

	public void setStatus(DBStatus status) {
		this.status = status;
	}

	@Expose
	public long freezeStamp = 0;
	@Expose
	public long checkInStamp = 0;

	public Watcher lockWatch;

	public String getMark() {
		return address + ":" + port;
	}

	public String getPassword() {
		return password;
	}

	public boolean isActiveServer() {
		return true;
	}

	public Watcher getLockWatcher() {
		return this.lockWatch;
	}

	public void setLockWatcher(Watcher watcher) {
		this.lockWatch = watcher;
		try {
			znode.zk.exists(getLockPath(), this.lockWatch);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String getLockPath() {
		return znode.pnode.pnode.path + "/" + DataBase.ACTIVE_LOCK;
	}

	public ZNode getLockNode() {
		ZNode lockNode = znode.pnode.pnode.getChild(DataBase.ACTIVE_LOCK);
		if (lockNode != null) {
			lockNode.sychronize();
		} else {
			try {
				Stat s = znode.zk.exists(getLockPath(), false);
				if (s == null) {
					return null;
				} else {
					lockNode = createLockNode();
				}
			} catch (KeeperException e) {
				e.printStackTrace();
				return null;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}

		}
		return lockNode;
	}

	public ZNode createLockNode() {
		ZNode lockNode = znode.pnode.pnode.addChild(DataBase.ACTIVE_LOCK);
		return lockNode;
	}

	public static DBServer fromJson(String json) {
		Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		DBServer server = g.fromJson(json, new TypeToken<DBServer>() {
		}.getType());
		return server;
	}

	public boolean active() {
		ZNode lockNode = getLockNode();
		if (lockNode != null) {
			lockNode.delete();
		}

		lockNode = createLockNode();
		lockNode.setData(getMark());
		lockNode.createMode = CreateMode.EPHEMERAL;
		if (lockNode.create()) {
			if (controller.activeDB()) {
				changeMasterNode();
				setStatus(DBStatus.Active);
			} else {
				deactive();
				freezeStamp = System.currentTimeMillis();
			}
		} else {
			return false;
		}

		return true;
	}

	private void changeMasterNode() {
		ZNode mnode = znode.pnode.pnode.getChild(DataBase.MASTER);
		if (noMaster(mnode)) {
			if (mnode == null) {
				mnode = znode.pnode.pnode.addChild(DataBase.MASTER);
			}
			mnode.createMode = CreateMode.PERSISTENT;
			mnode.setData(getMark());
			mnode.create();
		}

		ZNode serverNameNode = mnode.addChild(DataBase.SERVER_NAME);
		serverNameNode.setData(serverName);
		serverNameNode.save();
		ZNode hostNode = mnode.addChild(DataBase.HOST);
		hostNode.setData(address);
		hostNode.save();
		ZNode portNode = mnode.addChild(DataBase.PORT);
		portNode.setData("" + port);
		portNode.save();

	}

	public void deactive() {
		ZNode lockNode = getLockNode();
		if (lockNode != null) {
			if (lockNode.getStringData().equals(getMark())) {
				lockNode.delete();
				if (getStatus() == DBStatus.Active) {
					setStatus(DBStatus.Running);
				}
			}
		}

	}

	public void initMasterNode() {

		if (role == DBRole.MASTER) {
			ZNode mnode = znode.pnode.pnode.getChild(DataBase.MASTER);
			if (noMaster(mnode)) {
				if (mnode == null) {
					mnode = znode.pnode.pnode.addChild(DataBase.MASTER);
				}
				mnode.createMode = CreateMode.PERSISTENT;
				mnode.setData(getMark());
				mnode.create();
			}
			ZNode serverNameNode = mnode.addChild(DataBase.SERVER_NAME);
			serverNameNode.setData(serverName);
			serverNameNode.save();
			ZNode hostNode = mnode.addChild(DataBase.HOST);
			hostNode.setData(address);
			hostNode.save();
			ZNode portNode = mnode.addChild(DataBase.PORT);
			portNode.setData("" + port);
			portNode.save();
		}

	}

	private boolean noMaster(ZNode mnode) {
		boolean isNone = false;
		if (mnode == null) {
			isNone = true;
		} else {
			mnode.exists();
			if (mnode.stat != null) {
				isNone = true;
			}
		}
		return isNone;
	}

}
