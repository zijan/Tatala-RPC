package com.qileyuan.tatala.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ServiceRegistry {

    private static final Logger log = Logger.getLogger(ServiceRegistry.class);
    
    public static final String ZK_REGISTRY_PATH = "/registry";

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void register(String address) {
        if (address != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                createNode(zk, address);
            }
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, 5000, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
        	log.error("ZooKeeper.connectServer: ", e);
        }
        return zk;
    }

    private void createNode(ZooKeeper zk, String address) {
        try {
            byte[] bytes = address.getBytes();
            String path = zk.create(ZK_REGISTRY_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("Create zookeeper node ("+path+" => "+address+")");
        } catch (Exception e) {
        	log.error("ZooKeeper.createNode:", e);
        }
    }
}
