package easyops.eoa.test.ui;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;

import easyops.eoa.controller.DBControllerFactory;
import easyops.eoa.test.controller.SimuDBController;

public class TestZKBase implements Watcher {
	public static String zkaddress;
	ZooKeeper zookeeper;
	CountDownLatch latch;

	public void init() {
		DBControllerFactory.clazz = SimuDBController.class;
		zkaddress = "localhost:2181";

		try {
			zookeeper = new ZooKeeper(zkaddress, 1000, this);
			latch = new CountDownLatch(1);
			waitUntilConnected(zookeeper, latch);
			Stat stat = zookeeper.exists("/runtime", false);
			if (stat != null) {
				deleteAllNode("/runtime");
			}
		} catch (KeeperException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			latch.countDown();
		}
	}

	private void waitUntilConnected(ZooKeeper zk, CountDownLatch latch) {
		if (States.CONNECTING == zk.getState()) {
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}

	}

	public void shutdown() {
		try {
			zookeeper.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public void deleteAllNode(String path){
		try {
			List<String> chilren = zookeeper.getChildren(path, false);
			for(String p : chilren){
				deleteAllNode(path + "/" + p);
			}
			zookeeper.delete(path, -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
