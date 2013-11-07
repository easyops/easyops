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
	public String serverName = "";
	@Expose
	public String agentId = "";
	@Expose
	public long freezeStamp = 0;
	@Expose
	public long checkInStamp = 0;
	
	
	
	public IDBController controller;

	public DBStatus getStatus() {
		return status;
	}

	public void setStatus(DBStatus status) {
		this.status = status;
	}



	public Watcher lockWatch;

	public String getMark() {
		return serverName + ":" + address + ":" + port;
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
	
	public void setValuefromJson(String json) {
		Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		DBServer server = g.fromJson(json, new TypeToken<DBServer>() {
		}.getType());
		this.address = server.address;
		this.agentId = server.agentId;
		this.checkInStamp = server.checkInStamp;
		this.freezeStamp = server.freezeStamp;
		this.password = server.password;
		this.port = server.port;
		this.role = server.role;
		this.serverName = server.serverName;
		this.status = server.status;
		this.user = server.user;
		
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
				changeActiveNode();
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

	private void changeActiveNode() {
		ZNode mnode = znode.pnode.pnode.getChild(DataBase.ACTIVE_NODE);
		if (noActiveNode(mnode)) {
			if (mnode == null) {
				mnode = znode.pnode.pnode.addChild(DataBase.ACTIVE_NODE);
			}
			mnode.createMode = CreateMode.EPHEMERAL;
			mnode.setData(getMark());
			mnode.create();
		}else{
			mnode.setData(getMark());
			mnode.save();
		}

		

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

	public void initActiveNode() {

		if (role == DBRole.MASTER) {
			changeActiveNode();
		}

	}

	private boolean noActiveNode(ZNode node) {
		boolean isNone = false;
		if (node == null) {
			isNone = true;
		} else {
			node.exists();
			if (node.stat == null) {
				isNone = true;
			}
		}
		return isNone;
	}

}
