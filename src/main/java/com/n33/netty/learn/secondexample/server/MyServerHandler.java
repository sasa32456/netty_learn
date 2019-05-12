package com.n33.netty.learn.secondexample.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
* 服务器信息处理
*
* @author N33
* @date 2019/5/11
*/
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     *
     * @param ctx http信息
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        ctx.channel().write("from server: ");
        //写并推
        ctx.channel().writeAndFlush(UUID.randomUUID().toString());
    }

    /**
     * 回调（回调函数的意思吧）客户端反馈，服务端反馈，可会断反馈（from ser...）只要活动就会一直进行。。。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("来自于客户端的问候");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
