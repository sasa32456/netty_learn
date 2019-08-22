package com.n33.netty.learn.secondexample.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        /**
         * 异步方法
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            System.out.println("......");
        });

    }

    /**
     * 回调（回调函数的意思吧）客户端反馈，服务端反馈，可会断反馈（from ser...）只要活动就会一直进行。。。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("来自于客户端的问候");
        //再次发起服务
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

            }
        }).connect("localhost",8899).sync().channel().closeFuture().sync();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
