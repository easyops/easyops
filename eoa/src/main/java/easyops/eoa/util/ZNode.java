package easyops.eoa.util;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZNode {
	public ZNode pnode;
	public ZNode root;
	public ZooKeeper zk;
	public List<ZNode> children;
	public SOURCE souce = SOURCE.LOCAL;
	public String path;
	public byte[] data;
	public Stat stat;
	public String name;

	public ZNode(ZooKeeper zk) {
		this.root = this;
		this.pnode = null;
		this.zk = zk;
		this.path = "/";
		this.name = "/";
	}

	public ZNode(ZNode pnode, String name) {
		this.root = pnode.root;
		this.pnode = pnode;
		pnode.children.add(this);
		this.zk = pnode.zk;
		this.path = pnode.path + "/" + name;
	}

	public boolean createE() {
		return create(CreateMode.EPHEMERAL);
	}

	public boolean createP() {
		return create(CreateMode.PERSISTENT);
	}

	private boolean create(CreateMode createMode) {
		try {
			zk.create(path, data, Ids.OPEN_ACL_UNSAFE, createMode);
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
}
