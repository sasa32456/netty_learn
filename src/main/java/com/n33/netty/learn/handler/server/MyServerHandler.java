package com.n33.netty.learn.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 *
 * @author N33
 * @date 2019/8/20
 */
//public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
//MessageToMessage
public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " , " + msg);
        ctx.writeAndFlush(654321L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
