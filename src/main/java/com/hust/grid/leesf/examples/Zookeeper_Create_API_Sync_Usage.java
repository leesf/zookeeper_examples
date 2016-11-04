package com.hust.grid.leesf.examples;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class Zookeeper_Create_API_Sync_Usage implements Watcher {
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Zookeeper_Create_API_Sync_Usage());
		System.out.println(zookeeper.getState());
		connectedSemaphore.await();
		
		String path1 = zookeeper.create("/zk-test-ephemeral-/heheda/fuck", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);	
		System.out.println("Success create znode: " + path1);
		
		String path2 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);	
		System.out.println("Success create znode: " + path2);
	}

	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}
}
