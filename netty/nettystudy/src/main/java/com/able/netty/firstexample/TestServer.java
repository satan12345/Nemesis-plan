package com.able.netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author jipeng
 * @date 2019-01-17 10:49
 * @description
 *  定义好父子线程组
 *  定义好HanlderInitializer
 *  定义好通道处理器 写好ChannelHanlder的回调方法
 *
 */
public class TestServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 定义两个事件循环组
         * bossGroup用于接受链接
         * workGroup用来处理链接
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            //服务端启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    //定义组
                    .group(bossGroup, workerGroup)
                    //定义通道
                    .channel(NioServerSocketChannel.class)
                    //设置子处理器--通道初始化器
                    .childHandler(new TestServerInitilaizer());
            //端口绑定 并同步
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();

            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }
}

