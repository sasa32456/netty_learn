package com.n33.netty.learn.handler3.server;

import com.n33.netty.learn.handler3.mydecoder.MyPersonDecoder;
import com.n33.netty.learn.handler3.mydecoder.MyPersonEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyPersonDecoder());
        pipeline.addLast(new MyPersonEncoder());

        pipeline.addLast(new MyServerHandler());
    }
}
