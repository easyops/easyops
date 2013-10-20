package easyops.eoa.resource;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class ZNode {

	public static String STATUS = "status";
	public static String CHECK_IN_STAMP = "check_in_stamp";
	public static String DBSERVER_LIST = "servers";
	public static String ACTIVE_LOCK = "lock";
	public static String MASTER = "master";

	public enum SOURCE {
		LOCAL, REMOTE

	}

	public ZNode pnode;
	public ZNode root;
	public ZooKeeper zk;
	public List<ZNode> children = new ArrayList<ZNode>();
	public SOURCE source = SOURCE.LOCAL;
	public String path;
	public byte[] data;
	public Stat stat;
	public String name;
	public CreateMode createMode = CreateMode.PERSISTENT;
	public ArrayList<ACL> acl = Ids.OPEN_ACL_UNSAFE;

	public ZNode(ZooKeeper zk) {
		this.root = this;
		this.pnode = null;
		this.zk = zk;
		this.path = "/";
		this.name = "/";
		try {
			stat = zk.exists(path, false);
			if (stat == null) {
				this.source = SOURCE.LOCAL;
			} else {
				this.source = SOURCE.REMOTE;
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ZNode(ZNode pnode, String name) {
		this.root = pnode.root;
		this.pnode = pnode;
		this.name = name;
		pnode.children.add(this);
		this.zk = pnode.zk;
		if (pnode == this.root) {
			this.path = pnode.path + name;
		} else {
			this.path = pnode.path + "/" + name;
		}
		try {
			stat = zk.exists(path, false);
			if (stat == null) {
				this.source = SOURCE.LOCAL;
			} else {
				this.source = SOURCE.REMOTE;
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ZNode addChild(String name) {
		ZNode node = new ZNode(this, name);
		return node;
	}

	public boolean create() {
		return create(createMode);
	}

	private boolean create(CreateMode createMode) {
		try {
			stat = zk.exists(path, false);
			if (stat != null) {
				return true;
			}
			zk.create(path, data, this.acl, createMode);
		} catch (KeeperException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addExistWater(Watcher watcher) {
		try {
			zk.exists(path, watcher);
		} catch (KeeperException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addModifyWatcher(Watcher watcher) {
		try {
			zk.getData(path, watcher, stat);
		} catch (KeeperException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Stat getStat() {
		try {
			stat = zk.exists(path, false);
		} catch (KeeperException e) {
			e.printStackTrace();
			stat = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			stat = null;
		}
		return stat;
	}

	public void setData(String s) {
		try {
			data = s.getBytes(BaseResource.CharCode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public boolean save() {
		try {
			Stat s = zk.exists(path, false);
			if (s == null) {
				create();
			} else {
				zk.setData(path, data, s.getVersion());
			}
		} catch (KeeperException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public ZNode getChild(String name) {
		if (name == null) {
			return null;
		}
		for (ZNode node : children) {
			if (name.equals(node.name)) {
				return node;
			}
		}
		return null;
	}

	public boolean delete() {
		try {
			Stat s = zk.exists(path, false);
			if (s != null) {
				zk.delete(path, s.getVersion());
			}
			return true;
		} catch (KeeperException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean exists() {
		try {
			Stat s = zk.exists(path, false);
			this.stat = s;
			return true;
		} catch (KeeperException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean sychronize() {
		try {
			Stat s = new Stat();
			this.data = zk.getData(BaseResource.CharCode, false, s);
			this.stat = s;
			return true;
		} catch (KeeperException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

	}

	public String getStringData() {
		return bytes2String(data);
	}

	public static String bytes2String(byte[] data) {
		try {
			return new String(data, BaseResource.CharCode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] string2Bytes(String s) {
		try {
			return s.getBytes(BaseResource.CharCode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
