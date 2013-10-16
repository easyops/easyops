package easyops.eoa;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import easyops.eoa.resource.ZNode;
import easyops.eoa.ui.arguments.Argument;
import easyops.eoa.ui.arguments.DBDomain;
import easyops.eoa.ui.arguments.DBServer;

public class Agent implements Watcher {

	private ZNode zroot;
	private ZooKeeper zk;
	private Argument arg;

	public Agent(Argument arg) {
		this.arg = arg;
	}

	public void check() {

	}

	public void start() throws IOException {

		zk = new ZooKeeper(arg.zkserver, arg.zkSessionTimeout, this);
		zroot = new ZNode(zk);
		ZNode node = zroot.addChild("runtime");
		node = node.addChild("basebase");
		node = node.addChild("mysql");
		buildDBDomain(node);
	}

	private void buildDBDomain(ZNode node) {
		for (DBDomain db : arg.dbList) {
			ZNode domain = node.addChild(db.name);
			for (DBServer server : db.serverlist) {
				domain.addChild(server.getMark());
			}
		}
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(WatchedEvent event) {

	}

}
