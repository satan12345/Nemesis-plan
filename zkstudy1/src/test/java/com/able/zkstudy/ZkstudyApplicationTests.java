package com.able.zkstudy;


import com.able.zkstudy.com.able.zk.ZKConfig;
import com.able.zkstudy.com.able.zk.ZkCurator;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ZkstudyApplicationTests {

    public static final String zkServerPath = "192.168.119.147:2181";
    //public static final String zkServerPath="192.168.119.138:2181,192.168.119.141:2181,192.168.119.143:2181";
    /**
     * 超时时间
     */
    public static final Integer timeOut = 5000;

    ZooKeeper zookeeper;
    CountDownLatch countDownLatch = new CountDownLatch(1);
    CountDownLatch eventCountDownLatch = new CountDownLatch(1);

    @Test
    public void contextLoads() {
    }


    @Test
    public void test1() throws Exception {
        /**
         * 客户端和zk服务器的连接是一个异步的过程
         * 当链接成功之后 客户端会受到一个watch事件
         * 参数：
         * connectString:连接服务器的ip字符串
         * 比如 192.168.119.138:2181,192.168.119.141:2181,192.168.119.143:2181
         * 可以是一个ip也可以是多个ip 一个ip代表单机 多个ip代表集群
         * 也可以在ip后加路径
         * SessionTimeOut :超时时间 指定时间收不到心跳 则认为超时
         * watcher:监听事件 如果有对应的事件触发 则会受到一个通知 如果不需要 就设置为null
         * canBeReadOnly:可读 当这个物理机节点断开后 还是可以继续读数据的 只是不能写
         * 				此时数据被读取到 可能是旧数据 此处建议设置为false 不推荐使用
         * sessionId:会话的id
         * sessionPasswd:会话密码 当会话丢失后 可以根据sessonId和sessionPasswd重新获取密码
         */
        ZooKeeper zooKeeper = new ZooKeeper(zkServerPath, timeOut, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("触发事件回调:" + event);


            }
        });
        log.info("客户端开始链接服务器");
        log.info("连接状态:{}", zooKeeper.getState());
        TimeUnit.SECONDS.sleep(10);
        log.info("连接状态:{}", zooKeeper.getState());
    }

    @Test
    public void test2() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(zkServerPath, timeOut, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("触发事件回调:" + event);
            }
        });
        log.info("连接状态:{}", zooKeeper.getState());
        TimeUnit.SECONDS.sleep(10);
        log.info("连接状态:{}", zooKeeper.getState());
        long sessionId = zooKeeper.getSessionId();
        log.info("sessionId={}", sessionId);
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
        log.info("sessionPasswd={}", new String(sessionPasswd));

        log.info("客户端开始链接服务器");
        log.info("连接状态:{}", zooKeeper.getState());

        TimeUnit.SECONDS.sleep(1);
        log.info("开始重连会话");

        zooKeeper = new ZooKeeper(zkServerPath, timeOut, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("触发事件回调1:" + event);
            }
        }, sessionId, sessionPasswd);

        log.info("重新连接状态:{}", zooKeeper.getState());
        TimeUnit.SECONDS.sleep(10);
        log.info("重新连接状态:{}", zooKeeper.getState());
        sessionId = zooKeeper.getSessionId();
        log.info("重连后sessionId={}", sessionId);
    }

    @Test
    public void test3() throws Exception {
        ZooKeeper zookeeper = new ZooKeeper(zkServerPath, timeOut, event -> log.info("触发事件回调:" + event));
        /**
         * 同步或者异步创建节点  都不支持子节点的递归创建 异步有一个callback函数
         * 参数:
         * 		path:创建的路径
         * 		data:存储数据的byte[]
         * 		acl:控制权限策略
         * 		ZooDefs.Ids.OPEN_ACL_UNSAFE -->world:anyone:cdrwa
         * 		CREATOR_ALL_ACL -->auth:user:password:cdrwa
         *createModel:节点类型 是一个枚举
         * 	PERSISTENT:持久节点
         * 	PERSISTENT_SEQUENTIAL:持久顺序节点
         * 	EPHEMERAL:临时节点
         *  EPHEMERAL_SEQUENTIAL:临时顺序节点
         *
         */
        TimeUnit.SECONDS.sleep(10);
//        String s = zookeeper.create("/testnode", "testnode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
//        log.info("s={}",s);
        zookeeper.create("/asyncnode", "asyncnode".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL, new AsyncCallback.StringCallback() {
                    @Override
                    public void processResult(int rc, String path, Object ctx, String name) {
                        log.info("异步创建节点成功");
                        log.info("rc={}", rc);
                        log.info("path={}", path);
                        log.info("ctx={}", ((Supplier) ctx).get());
                        log.info("name={}", name);
                    }
                }, (Supplier<Integer>) () -> 3);
        TimeUnit.SECONDS.sleep(5);

    }

    @Test
    public void test4() throws Exception {
        ZooKeeper zookeeper = new ZooKeeper(zkServerPath, timeOut, event -> log.info("触发事件回调:" + event));
        TimeUnit.SECONDS.sleep(10);
        //同步更新数据
//        Stat stat = zookeeper.setData("/asyncnode0000000014", "new Date".getBytes(), 0);
//        log.info("stat={}",stat);
        zookeeper.setData("/asyncnode0000000014", "async set data".getBytes(), 1, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                log.info("异步更新节点成功");
                log.info("rc={}", rc);
                log.info("path={}", path);
                log.info("ctx={}", ((Supplier) ctx).get());
                log.info("stat={}", stat);
            }
        }, (Supplier) () -> "xyz");
        TimeUnit.SECONDS.sleep(5);

    }

    @Test
    public void test5() throws Exception {
        //同步删除

//        try {
//            zookeeper.delete("/testnode1",-1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (KeeperException e) {
//            KeeperException.Code code = e.code();
//            log.info("code={}",code);
//            e.printStackTrace();
//        }
        zookeeper.delete("/asyncnode0000000015", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                log.info("异步删除节点成功");
                log.info("rc={}", rc);
                log.info("path={}", path);
                log.info("ctx={}", ((Function) ctx).apply(path));

            }
        }, (Function<String, String>) s -> {
            log.info("删除节点的的路径为:{},发送mq咯", s);
            return "卡卡西";
        });
        log.info("删除节点成功");
        TimeUnit.SECONDS.sleep(2);

        //zookeeper.delete("");

    }

    private void bind(String path) {
        zookeeper.getData("/names", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("event={}", event);
                eventCountDownLatch.countDown();
                bind(path);

            }
        }, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                log.info("异步查询节点成功");
                log.info("rc={}", rc);
                log.info("path={}", path);
                log.info("ctx={}", ((Function) ctx).apply(path));

            }
        }, new Function<String, String>() {
            @Override
            public String apply(String s) {
                log.info("apply={}", s);
                return s;
            }
        });
    }

    @Test
    public void test6() throws Exception {
        Stat stat = new Stat();
//        byte[] data = zookeeper.getData("/names", true, stat);
//        log.info("查询到的data为:{}",new String(data));
//        log.info("stat={}",stat);

        bind("/names");
        eventCountDownLatch.await();
//        byte[] data = zookeeper.getData("/names", true, stat);
//        log.info("查询到的data为:{}", new String(data));
//        log.info("stat={}", stat);
//        TimeUnit.SECONDS.sleep(100);
//        log.info("事件触发了");

    }

    @Before
    public void init() throws Exception {
        long start = System.currentTimeMillis();
        zookeeper = new ZooKeeper(zkServerPath, timeOut, event -> {
            log.info("触发事件回调:" + event);
            countDownLatch.countDown();
        });
        countDownLatch.await();
        long end = System.currentTimeMillis();
        log.info("链接花费时间为:{}", (end - start));

    }
//    @Before
//    public void init() throws Exception {
//        zookeeper = new ZooKeeper(zkServerPath, timeOut, event -> log.info("触发事件回调:" + event));
//        TimeUnit.SECONDS.sleep(10);
//    }

    @Test
    public void test7() throws Exception {
//            zookeeper.getChildren("/names", new Watcher() {
//                @Override
//                public void process(WatchedEvent event) {
//                    log.info("event={}",event);
//                }
//            }, new AsyncCallback.ChildrenCallback() {
//                @Override
//                public void processResult(int rc, String path, Object ctx, List<String> children) {
//                    log.info("异步查询子节点成功");
//                    log.info("rc={}", rc);
//                    log.info("path={}", path);
//                    log.info("ctx={}", ((Function) ctx).apply(path));
//                    log.info("children:{}",children);
//
//                }
//            }, new Function<String,String>() {
//                @Override
//                public String apply(String s) {
//                    log.info("apply={}",s);
//                    return "123";
//                }
//            });
        zookeeper.getChildren("/names", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("event={}", event);
                eventCountDownLatch.countDown();
            }
        }, new AsyncCallback.Children2Callback() {

            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                log.info("异步查询子节点成功");
                log.info("rc={}", rc);
                log.info("path={}", path);
                log.info("ctx={}", ((Function) ctx).apply(path));
                log.info("children:{}", children);
                log.info("stat={}", stat);
            }
        }, new Function<String, String>() {
            @Override
            public String apply(String s) {
                log.info("apply={}", s);
                return "123";
            }
        });
        eventCountDownLatch.await();
    }

    @Test
    public void test8() throws Exception {
        zookeeper.exists("/kakaxi/leiqie", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("event={}", event);
                eventCountDownLatch.countDown();
            }
        }, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                log.info("异步查询节点是否存在");
                log.info("rc={}", rc);
                log.info("path={}", path);
                log.info("ctx={}", ((Function) ctx).apply(path));
                log.info("stat={}", stat);

            }
        }, new Function<String, String>() {
            @Override
            public String apply(String s) {
                log.info("apply={}", s);
                return "123";
            }
        });
        eventCountDownLatch.await();
    }

    @Test
    public void test9() throws Exception {
        //自定义用户权限访问列表
        List<ACL> acls = Lists.newArrayList();
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("kakaxi:kakaxi"));
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("dahe:dahe"));
        acls.add(new ACL(ZooDefs.Perms.ALL, id1));
        acls.add(new ACL(ZooDefs.Perms.READ|ZooDefs.Perms.WRITE, id2));
        String s = zookeeper.create("/huoying/kakaxi", "kakaxi".getBytes(), acls, CreateMode.PERSISTENT_SEQUENTIAL);
        log.info("创建节点成功:{}",s);
    }
    @Test
    public void test10 () throws Exception{
        //登录
        //zookeeper.addAuthInfo("digest","dahe:dahe".getBytes());
        Stat stat=new Stat();
        byte[] data = zookeeper.getData("/huoying/kakaxi0000000002", false, stat);
        log.info("获取到的数据为:{}",new String(data));
    }
    @Test
    public void test11 () throws Exception{
        List<ACL> acls=Lists.newArrayList();
        Id id=new Id("ip","192.168.70.97");
        acls.add(new ACL(ZooDefs.Perms.ALL,id));
        String s = zookeeper.create("/huoying/kakaxi", "kakaxi".getBytes(), acls, CreateMode.PERSISTENT_SEQUENTIAL);
        log.info("创建节点成功:{}",s);
    }
    @Resource
    ZkCurator zkCurator;
    @Test
    public void testIsAlive (){
        boolean alived = zkCurator.isAlived();
        log.info("alived={}",alived);
        ApplicationContext applicationContext=
                new AnnotationConfigApplicationContext(ZKConfig.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).forEach(s -> System.out.println(s));
    }



}
