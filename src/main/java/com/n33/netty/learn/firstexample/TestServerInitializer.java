package com.n33.netty.learn.firstexample;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * A special {@link ChannelInboundHandler} which offers an easy way to initialize a {@link Channel} once it was
 * registered to its {@link EventLoop}.
 * 一个特殊的{@link ChannelInboundHandler}，它提供了一种简单的方法来初始化{@link Channel}
 * 已注册其{@link EventLoop}。 *
 * Implementations are most often used in the context of {@link Bootstrap#handler(ChannelHandler)} ,
 * {@link ServerBootstrap#handler(ChannelHandler)} and {@link ServerBootstrap#childHandler(ChannelHandler)} to
 * setup the {@link ChannelPipeline} of a {@link Channel}.
 * 实现最常用于{@link Bootstrap＃handler（ChannelHandler）}的上下文中，
 * {@link ServerBootstrap＃handler（ChannelHandler）}和{@link ServerBootstrap＃childHandler（ChannelHandler）}到
 * 设置{@link Channel}的{@link ChannelPipeline}。 *
 * <pre>
 *
 * public class MyChannelInitializer extends {@link ChannelInitializer} {
 *     public void initChannel({@link Channel} channel) {
 *         channel.pipeline().addLast("myHandler", new MyHandler());
 *     }
 * }
 *
 * ...
 * bootstrap.childHandler(new MyChannelInitializer());
 * ...
 * </pre>
 * Be aware that this class is marked as {@link Sharable} and so the implementation must be safe to be re-used.
 * 请注意，此类标记为{@link Sharable}，因此实现必须安全才能重复使用。 *
 *
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //管道
        ChannelPipeline pipeline = ch.pipeline();
        //编解码
        pipeline.addLast("httpServerCode",new HttpServerCodec());
        //转到自定义
        pipeline.addLast("testHttpServerHandler", new TestHttpServerHandler());


    }
}
