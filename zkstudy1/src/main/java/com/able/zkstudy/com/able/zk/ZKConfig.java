package com.able.zkstudy.com.able.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jipeng
 * @date 2018-12-10 18:24
 * @description
 */
@Configuration
public class ZKConfig {
    @Bean
    RetryPolicy retryPolicy(){
        RetryPolicy retryPolicy=new RetryNTimes(10,5000);
        return retryPolicy;
    }
    @Bean(initMethod="start")
    CuratorFramework curatorFramework(RetryPolicy retryPolicy){
        /**
         * String connectString,
         * int sessionTimeoutMs,
         * int connectionTimeoutMs,
         * RetryPolicy retryPolicy
         */
//        zk服务器地址 集群用,分割
        String connectString="192.168.119.147:2181";
        //session timeout 会话超时时间
        int sessionTimeoutMs=10000;
        //创建连接超时时间
        int connectionTimeoutMs=5000;
        //retryPolicy 重试策略
        return CuratorFrameworkFactory.newClient(connectString,sessionTimeoutMs,connectionTimeoutMs,retryPolicy);
    }
    @Bean(initMethod = "init")
    public ZkCurator zkCurator(CuratorFramework curatorFramework){
        return new ZkCurator(curatorFramework);
    }

}

