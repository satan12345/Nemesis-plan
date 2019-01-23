package com.able.netty.secondexample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author jipeng
 * @date 2019-01-19 17:14
 * @description
 */
public class MyClient {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            //bootstrap.group(eventLoopGroup).channel(N)
        } finally {

        }
    }
}

