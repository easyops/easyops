package easyops.eoa.monitor;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

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
		if (server.znode.zk.getMyStatus() == MyZookeeperStatus.ClOSING) {
			return;
		}
		if (server.getStatus() != DBStatus.Running) {
			reWatch(event);
			return;
		}
		if (server.freezeStamp != 0
				&& server.freezeStamp + freezeTime > System.currentTimeMillis()) {
			reWatch(event);
			return;
		}
		if (event.getType() == EventType.NodeDeleted) {
			if (server.active()) {
				if (!server.isActiveServer()) {
					server.deactive();
					server.freezeStamp = System.currentTimeMillis();
					reWatch(event);
				}
			} else {
				reWatch(event);

			}
		} else {
			reWatch(event);
		}

	}

	private void reWatch(WatchedEvent event) {
		String path = event.getPath();
		try {
			server.znode.zk.exists(path, this);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
