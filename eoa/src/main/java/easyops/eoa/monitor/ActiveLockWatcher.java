package easyops.eoa.monitor;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import easyops.eoa.resource.DBServer;
import easyops.eoa.resource.DBStatus;
import easyops.eoa.resource.ZNode;

public class ActiveLockWatcher implements Watcher {

	private DBServer server;
	private int freezeTime;

	public ActiveLockWatcher(DBServer server, int freezeTime) {
		this.server = server;
		this.freezeTime = freezeTime;
	}
	
	@Override
	public void process(WatchedEvent event) {
		
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
			if(ZNode.lockMe(server)) {
				if (!server.activeServer()) {
					ZNode.unLockMe(server);
					server.freezeStamp = System.currentTimeMillis();
					reWatch(event);
				}
			} else {				
				reWatch(event);

			}
		}else{
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
