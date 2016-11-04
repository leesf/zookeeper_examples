package com.hust.grid.leesf.examples;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class AuthSample_Get {
	final static String PATH = "/zk-book-auth_test";

	public static void main(String[] args) throws Exception {

		ZooKeeper zookeeper1 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zookeeper1.addAuthInfo("digest", "foo:true".getBytes());
		zookeeper1.create(PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
		System.out.println("success create znode: " + PATH);
		ZooKeeper zookeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zookeeper2.getData(PATH, false, null);
	}
}
