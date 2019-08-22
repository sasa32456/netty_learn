package com.n33.netty.learn.handler2.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 *
 *
 * @author N33
 * @date 2019/8/22
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println(this);

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyServerHandler());
    }
}
