package easyops.eoa;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import easyops.eoa.resource.DBDomain;
import easyops.eoa.resource.DBPartition;
import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DataBase;
import easyops.eoa.resource.ZNode;
import easyops.eoa.ui.arguments.Argument;

public class Agent implements Watcher {

	private ZNode zroot;
	private ZooKeeper zk;
	private Argument arg;
	private DataBase db;

	public Agent(Argument arg) {
		this.arg = arg;
		this.db = new DataBase(this.arg.db);
	}

	public void check() {

	}

	public void start() throws IOException {
		buildZK();
		startMoniterDB();
		startMoniterZK();
	}

	private void startMoniterZK() {
		// TODO Auto-generated method stub

	}

	private void startMoniterDB() {

	}

	private void buildZK() throws IOException {
		zk = new ZooKeeper(arg.zkserver, arg.zkSessionTimeout, this);
		zroot = new ZNode(zk);
		ZNode node = zroot.addChild("runtime");
		node = node.addChild("basebase");
		node = node.addChild("mysql");
		buildDBDomain(node);
	}

	private void buildDBDomain(ZNode node) {
		for (DBDomain domain : db.dbList) {
			ZNode znode = node.addChild(domain.name);
			znode.createMode = CreateMode.PERSISTENT;
			znode.data = domain.toJsonBytes();
			znode.create();
			domain.znode = znode;
			buildDBServer(domain, znode);
		}
	}

	private void buildDBServer(DBDomain domain, ZNode domainNode) {
		if (domain.isPartition) {
			for (DBPartition partition : domain.partitionList) {
				ZNode znode = domainNode.addChild(partition.partitionId);
				znode.createMode = CreateMode.PERSISTENT;
				znode.create();
				partition.znode = znode;
				buildDBServer(partition.serverlist, znode);
			}
		} else {
			buildDBServer(domain.serverList, domainNode);
		}
	}

	private void buildDBServer(List<DBServer> serverList, ZNode pnode) {

		for (DBServer server : serverList) {
			ZNode znode = pnode.addChild(server.getMark());
			znode.data = server.toJsonBytes();
			znode.create();
			server.znode = znode;
		}

	}

	public void shutdown() {

	}

	@Override
	public void process(WatchedEvent event) {

	}

}
