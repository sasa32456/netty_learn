package com.n33.netty.learn.firstexample.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args) throws InterruptedException {

        /**
         *事件循环组,死循环
         */
        //从客户端接收
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理,反馈
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            /**
             * {@link Bootstrap} sub-class which allows easy bootstrap of {@link ServerChannel}
             * Bootstrap子类，允许简单地引导ServerChannel
             * 启动服务端代码
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * Set the {@link EventLoopGroup} for the parent (acceptor) and the child (client). These
             * {@link EventLoopGroup}'s are used to handle all the events and IO for {@link ServerChannel} and
             * {@link Channel}'s.
             *为父（接受者）和子（客户端）设置{@link EventLoopGroup}。这些{@link EventLoopGroup}用于处理{@link ServerChannel}的所有事件和IO{@link Channel}。         */

            /**
             * The {@link Class} which is used to create {@link Channel} instances from.
             * You either use this or {@link #channelFactory(io.netty.channel.ChannelFactory)} if your
             * {@link Channel} implementation has no no-args constructor.
             * {@link Class}，用于创建{@link Channel}实例。如果您使用此或{@link #channelFactory（io.netty.channel.ChannelFactory）}{@link Channel}实现没有no-args构造函数。         */
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    //自定义请求处理器
                    .childHandler(new TestServerInitializer());

            //监听端口
            final ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            //关闭端口
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
