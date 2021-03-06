package com.able.zkstudy;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author jipeng
 * @date 2018-12-06 19:22
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApacheCuraotrStudy {
    CuratorFramework curator;
    public static final String zkServerPath = "192.168.119.147:2181";

    @Before
    public void init() {
        /**
         * 同步创建zk实例 元素的api是异步的
         * curator 链接zk的策略：ExponentialBackoffRetry
         * baseSleepTimeMs:重试之间等待的初始时间
         * maxRetries:最大重试次数
         * maxSleeepMs:最大重试时间
         * 参数名	说明
         baseSleepTimeMs	初始sleep时间
         maxRetries	最大重试次数
         maxSleepMs	最大sleep时间
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5, 10000);
        /**
         * curator链接zk的重试策略：retryNtimes
         * n:重试的次数
         * sleepMsBetweenRetries:每次重试的时间间隔
         */
        RetryPolicy retryPolicy1 = new RetryNTimes(5, 5000);

        /**
         * curator链接zk的重试策略：RetryOneTime
         * sleepMsBetweenRetry:每次重试的时间间隔
         */
//        RetryPolicy retryPolicy2=new RetryOneTime(3000);

        /**
         * curator链接zk的重试策略:retryForever
         * 永远重试
         * 不推荐使用
         */
//        RetryPolicy retryPolicy3=new RetryForever(3000);

        RetryPolicy retryPolicy4 = new RetryUntilElapsed(2000, 3000);

        curator = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                //权限认证
                //.authorization(Lists.newArrayList(new AuthInfo("digest","huoying:kakaxi".getBytes())))
                .retryPolicy(retryPolicy)
                .namespace("person")
                .build();
        curator.start();
    }

    @Test
    public void test1() throws Exception {
        CuratorFrameworkState state = curator.getState();
        log.info("stat={}", state);
        TimeUnit.SECONDS.sleep(3);
        curator.close();
        state = curator.getState();
        log.info("stat={}", state);
    }

    /**
     * 创建节点
     *
     * @throws Exception
     */
    @Test
    public void testCreateNode() throws Exception {

        CuratorFrameworkState state = curator.getState();
        log.info("stat={}", state);
        String nodePath = "/super/imooc";
        byte[] bytes = "data".getBytes();
        String s = curator.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        log.info("异步创建节点");
                        log.info("curaotrEvent={}", event);
                    }
                })
                .forPath(nodePath, bytes);


        curator.close();
        state = curator.getState();
        log.info("stat={}", state);
        log.info("s={}", s);
    }

    @Test
    public void testUpdateNode() throws Exception {
        try {
            String nodePath = "/super/imooc0000000003";
            byte[] bytes = "new data".getBytes();
            Stat stat = curator.setData().withVersion(0).forPath(nodePath, bytes);
            log.info("stat={}", stat);
        } finally {
            curator.close();

        }

    }

    @Test
    public void testDeleteNode() throws Exception {
        String nodePath = "/super/imooc0000000003";
        curator.delete().guaranteed()//解决边缘情况 当服务端删除了节点 而回送响应的时候发生失败
                .deletingChildrenIfNeeded()//如果有子节点 就删除
                .withVersion(1)
                .forPath(nodePath);
        log.info("删除节点");

    }

    @Test
    public void testRead() throws Exception {
        try {
            //读取节点的数据
            String path = "/super/imooc0000000002";
            Stat stat = new Stat();
            byte[] bytes = curator.getData()
                    .storingStatIn(stat)
                    .forPath(path);
            log.info("读取到的数据为:{}", new String(bytes));
            log.info("version={}", stat.getVersion());


            List<String> list = curator.getChildren().forPath("/");
            log.info("查询到子节点的数据为:{}", list);

            Stat stat1 = curator.checkExists().forPath("/man1");
            log.info("节点是否存在：{}", stat1 != null ? "true" : "false");

        } finally {
            curator.close();
        }
    }

    @Test
    public void testWatch() throws Exception {
        String path = "/man";
        //使用usingWatcher的时候只会触发一次监听 监听玩就销毁
        curator.getData().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                log.info("event={}", event);
                log.info(event.getPath());
            }
        }).forPath(path);
        TimeUnit.SECONDS.sleep(100);
    }

    @Test
    public void testWatch1() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //为节点添加watcher
        //NodeCache:监听数据节点的变更 会触发监听事件
        String path = "/man";
        NodeCache nodeCache = new NodeCache(curator, path);
        //buildInitial:初始化的时候获取node的值 并且缓存
        nodeCache.start(true);
        ChildData currentData = nodeCache.getCurrentData();
        if (currentData != null) {
            log.info("节点初始化的数据为:{}", new String(currentData.getData()));
        } else {
            log.info("节点初始化的数据为空");
        }
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData currentData1 = nodeCache.getCurrentData();
                if (currentData1 != null) {
                    log.info("节点路径:{},节点数据:{}", currentData1.getPath(), new String(currentData1.getData()));
                } else {
                    log.info("节点已经被删除");
                }
                if (currentData == null && currentData1 != null) {
                    log.info("节点创建:{},节点数据:{}", currentData1.getPath(), new String(currentData1.getData()));
                }
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Test
    public void testWatch2() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String path = "/";
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curator, path, true);
        /**
         * startModel：初始化方式
         *      POST_INITIALIZED_EVENT: 异步初始化 初始化后会触发事件
         *      NORMAL:异步初始化
         *      BUILD_INITIAL_CACHE:同步初始化
         */
        //pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        //pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        List<ChildData> currentData = pathChildrenCache.getCurrentData();
        if (currentData != null) {
            currentData.forEach(x -> log.info("子节点路径为:{}数据为:{}", x.getPath(), new String(x.getData())));
        }
        //添加监听事件
        pathChildrenCache.getListenable().addListener((client, event) -> {
            //初始化
            if (PathChildrenCacheEvent.Type.INITIALIZED.equals(event.getType())) {
                log.info("子节点初始化成功");
            } else if (PathChildrenCacheEvent.Type.CHILD_ADDED.equals(event.getType())) {
                ChildData data = event.getData();
                log.info("子节点添加:path={},data={}", data.getPath(), new String(data.getData()));
            } else if (PathChildrenCacheEvent.Type.CHILD_REMOVED.equals(event.getType())) {
                log.info("删除子节点");
                ChildData data = event.getData();
                log.info("path={},data={}", data.getPath(), new String(data.getData()));

            } else if (PathChildrenCacheEvent.Type.CHILD_UPDATED.equals(event.getType())) {
                log.info("子节点更新");
                ChildData data = event.getData();
                log.info("path={},data={}", data.getPath(), new String(data.getData()));

            }


        });

        countDownLatch.await();
    }

    @Test
    public void testAcl() throws Exception {
        String path = "/you";
        byte[] data = "天照".getBytes();
        Opera(new Function<CuratorFramework, Void>() {
            @Override
            public Void apply(CuratorFramework curatorFramework) {
                try {
                    String s = curatorFramework.create()
                            .creatingParentContainersIfNeeded()
                            .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                            .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                            .forPath(path, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;

            }
        });
    }

    @Test
    public void testUpdateAcl() throws Exception {
        List<ACL> list = Lists.newArrayList();
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("kakaxi:kakaxi"));
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("mingren:mingren"));
        list.add(new ACL(ZooDefs.Perms.ALL, id1));
        list.add(new ACL(ZooDefs.Perms.READ, id2));
        String opera = Opera(curator -> {
            String path = "/person/you0000000004";
            try {
                curator.setACL().withACL(list).forPath(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "123";
        });
    }

    private <T> T Opera(Function<CuratorFramework, T> function) {
        try {
            T t = function.apply(curator);
            return t;
        } finally {
            curator.close();
            CuratorFrameworkState state = curator.getState();
            log.info("status={}", state);
        }
    }

}

