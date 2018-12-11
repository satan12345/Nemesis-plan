package com.able.zkstudy.com.able.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.util.Assert;

import java.util.concurrent.CountDownLatch;

/**
 * @author jipeng
 * @date 2018-12-11 9:24
 * @description
 */

@Slf4j
public class DistributeLock {

    private CuratorFramework curatorFramework;
    CountDownLatch countDownLatch = new CountDownLatch(1);
    /**
     * 分布式锁的节点名称
     */
    public static final String ZK_LOCK_PROJECT = "imooc-locks";
    /**
     * 分布式锁节点
     */

    public static final String DISTRIBUTE_LOCK = "distribute_lock";

    public DistributeLock(CuratorFramework curatorFramework) {
        Assert.notNull(curatorFramework, "参数怒能为null");
        this.curatorFramework = curatorFramework;
    }

    public void init() {
        curatorFramework.usingNamespace("ZKLocks-Namespace");
        /**
         * 创建zk锁的总节点，相当于eclipse的工作空间下的项目
         * /ZKLocks-Namespace/imooc-locks/distribute_lock
         */
        try {

            if (curatorFramework.checkExists().forPath("/" + ZK_LOCK_PROJECT) == null) {
                curatorFramework.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK_PROJECT);
                addWatcheToLock("/" + ZK_LOCK_PROJECT);
            }
        } catch (Exception e) {
            throw new RuntimeException("zk连接发生错误");
        }
    }

    private void addWatcheToLock(String path) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(curatorFramework, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((client, event) -> {
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                String path1 = event.getData().getPath();
                log.info("上有个会话已经释放锁或者会话已经断开,节路径为:{}", path1);
                if (path1.contains(DISTRIBUTE_LOCK)) {
                    log.info("释放计数器，让当前请求来获得分布式锁...");
                    countDownLatch.countDown();
                }
            }
        });
    }

    public boolean releaseLock() {
        try {
            String path="/"+ZK_LOCK_PROJECT+"/"+DISTRIBUTE_LOCK;
            if (curatorFramework.checkExists().forPath(path)!=null) {
                curatorFramework.delete().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void getLock() {
        while (true) {
            try {

                curatorFramework.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTE_LOCK);
                log.info("获取分布式锁成功");
                if (countDownLatch.getCount() <= 0) {
                    countDownLatch = new CountDownLatch(1);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("获取分布式锁失败");

                try {
                    //阻塞线程
                    countDownLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


}

