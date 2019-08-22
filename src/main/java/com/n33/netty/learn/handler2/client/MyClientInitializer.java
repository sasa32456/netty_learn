package com.n33.netty.learn.handler2.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println(this);

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyClientHandler());

    }
}
