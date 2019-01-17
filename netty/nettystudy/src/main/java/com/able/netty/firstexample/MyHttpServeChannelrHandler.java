package com.able.netty.firstexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author jipeng
 * @date 2019-01-17 13:22
 * @description 自定义处理器
 */
public class MyHttpServeChannelrHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * @description 读取客户端请求 并向客户端相应的方法
     * messageReceived
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            //向客户端返回的内容
            ByteBuf content = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
            //构造响应  指定协议 设置返回状态  设置返回内容
            FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
            //增加headers
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //利用上下文对象将响应刷新到客户端
            ctx.writeAndFlush(response);
        }

    }
}

