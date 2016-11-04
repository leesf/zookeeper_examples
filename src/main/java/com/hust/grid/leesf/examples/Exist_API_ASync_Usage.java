package com.hust.grid.leesf.examples;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Exist_API_ASync_Usage implements Watcher {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zk;

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		zk = new ZooKeeper("127.0.0.1:2181", 5000, 
				new Exist_API_ASync_Usage());
		connectedSemaphore.await();

		zk.exists(path, true, new IIStatCallback(), null);

		zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.setData(path, "123".getBytes(), -1);

		zk.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("success create znode: " + path + "/c1");

		zk.delete(path + "/c1", -1);
		zk.delete(path, -1);

		Thread.sleep(Integer.MAX_VALUE);
	}

	public void process(WatchedEvent event) {
		try {
			if (KeeperState.SyncConnected == event.getState()) {
				if (EventType.None == event.getType() && null == event.getPath()) {
					connectedSemaphore.countDown();
				} else if (EventType.NodeCreated == event.getType()) {
					System.out.println("success create znode: " + event.getPath());
					zk.exists(event.getPath(), true, new IIStatCallback(), null);
				} else if (EventType.NodeDeleted == event.getType()) {
					System.out.println("success delete znode: " + event.getPath());
					zk.exists(event.getPath(), true, new IIStatCallback(), null);
				} else if (EventType.NodeDataChanged == event.getType()) {
					System.out.println("data changed of znode: " + event.getPath());
					zk.exists(event.getPath(), true, new IIStatCallback(), null);
				}
			}
		} catch (Exception e) {
		}
	}
}

class IIStatCallback implements AsyncCallback.StatCallback {
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		System.out.println("rc: " + rc + ", path: " + path + ", stat: " + stat);
	}	
}
