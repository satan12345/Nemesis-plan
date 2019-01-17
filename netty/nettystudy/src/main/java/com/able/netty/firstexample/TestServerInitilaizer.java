package com.able.netty.firstexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * @author jipeng
 * @date 2019-01-17 13:17
 * @description 自定义channel初始化器
 */
public class TestServerInitilaizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //channel的管道
        ChannelPipeline pipeline = ch.pipeline();
        //添加处理器(netty提供的 与自定义的)
        //增加channelHander 管道处理器(相当于处理器 切面)
        //编解码处理器
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        //增加自定义channelHander
        pipeline.addLast("TestHttpServerHandler",new MyHttpServeChannelrHandler());

    }
}

