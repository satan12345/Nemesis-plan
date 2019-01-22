package com.able.netty.secondexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;


/**
 * @author jipeng
 * @date 2019-01-19 10:38
 * @description
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //增加解码器
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,5));
        //增加编码器
        pipeline.addLast(new LengthFieldPrepender(4));
        //字符解码
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //字符编码
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

        pipeline.addLast(new MyServerHandler());
    }
}

