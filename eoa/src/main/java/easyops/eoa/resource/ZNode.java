package easyops.eoa.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class ZNode {
	
	public enum SOURCE {
		LOCAL,
		REMOTE

	}
	
	public ZNode pnode;
	public ZNode root;
	public ZooKeeper zk;
	public List<ZNode> children;
	public SOURCE souce = SOURCE.LOCAL;
	public String path;
	public byte[] data;
	public Stat stat;
	public String name;
	public CreateMode createMode = CreateMode.EPHEMERAL;
	public ArrayList<ACL> acl = Ids.OPEN_ACL_UNSAFE;

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
	
	public ZNode addChild(String name){
		ZNode node = new ZNode(this, name);
		return node;
	}

	public boolean create() {
		return create(createMode);
	}

	private boolean create(CreateMode createMode) {
		try {
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
}
