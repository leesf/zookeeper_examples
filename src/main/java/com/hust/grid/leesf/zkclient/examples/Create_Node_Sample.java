package com.hust.grid.leesf.zkclient.examples;
import org.I0Itec.zkclient.ZkClient;

public class Create_Node_Sample {
    public static void main(String[] args) throws Exception {
    	ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        String path = "/zk-book/c1";
        zkClient.createPersistent(path, true);
        System.out.println("success create znode.");
    }
}