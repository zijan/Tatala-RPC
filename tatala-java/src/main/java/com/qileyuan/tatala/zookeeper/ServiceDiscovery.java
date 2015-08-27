package com.qileyuan.tatala.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ServiceDiscovery {
	private static final Logger log = Logger.getLogger(ServiceDiscovery.class);

	private static CountDownLatch latch = new CountDownLatch(1);

	private static List<String> dataList = new ArrayList<String>();

	public static void init(String registryAddress){
		ZooKeeper zk = connectServer(registryAddress);
		if (zk != null) {
			watchNode(zk);
		}
	}
	
	public static String discover() {
		String data = null;
		int size = dataList.size();
		if (size > 0) {
			if (size == 1) {
				data = dataList.get(0);
			} else {
				data = dataList.get(ThreadLocalRandom.current().nextInt(size));
			}
		}
		return data;
	}

	private static ZooKeeper connectServer(String registryAddress) {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, 5000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getState() == Event.KeeperState.SyncConnected) {
						latch.countDown();
					}
				}
			});
			latch.await();
		} catch (IOException | InterruptedException e) {
			log.error("ServiceDiscovery.connectServer: ", e);
		}
		return zk;
	}

	private static void watchNode(final ZooKeeper zk) {
		try {
			List<String> nodeList = zk.getChildren(ServiceRegistry.ZK_REGISTRY_PATH, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getType() == Event.EventType.NodeChildrenChanged) {
						watchNode(zk);
					}
				}
			});
			List<String> dataList = new ArrayList<>();
			for (String node : nodeList) {
				byte[] bytes = zk.getData(ServiceRegistry.ZK_REGISTRY_PATH + "/" + node, false, null);
				dataList.add(new String(bytes));
			}
			ServiceDiscovery.dataList = dataList;
		} catch (Exception e) {
			log.error("ServiceDiscovery.watchNode: ", e);
		}
	}
}