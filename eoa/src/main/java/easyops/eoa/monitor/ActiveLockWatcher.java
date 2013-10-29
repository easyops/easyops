package easyops.eoa.monitor;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DBStatus;
import easyops.eoa.ui.MyZookeeperStatus;

public class ActiveLockWatcher extends BaseWatcher implements Watcher {

	private DBServer server;
	private int freezeTime;

	public ActiveLockWatcher(DBServer server, int freezeTime) {
		this.server = server;
		this.freezeTime = freezeTime;
	}

	@Override
	public void process(WatchedEvent event) {
		String path = event.getPath();
		if (server.znode.zk.getMyStatus() == MyZookeeperStatus.ClOSING) {
			return;
		}
		if (server.getStatus() != DBStatus.Running) {
			watch(path);
			return;
		}
		if (server.freezeStamp != 0
				&& server.freezeStamp + freezeTime > System.currentTimeMillis()) {
			watch(path);
			return;
		}
		if (event.getType() == EventType.NodeDeleted) {
			if (server.active()) {
				if (!server.isActiveServer()) {
					watch(path);
				}
			} else {
				watch(path);

			}
		} else {
			watch(path);
		}

	}

	private void watch(String path) {
		watch(server.znode.zk, path, this);
	}

	public static void watch(ZooKeeper zk, String path, Watcher watcher) {
		try {
			zk.exists(path, watcher);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
