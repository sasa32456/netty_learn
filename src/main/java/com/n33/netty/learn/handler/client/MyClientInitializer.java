package com.n33.netty.learn.handler.client;

import com.n33.netty.learn.handler.bytemessagedecoder.MyByteToLongDecoder;
import com.n33.netty.learn.handler.bytemessagedecoder.MyLongToByteEncoder;
import com.n33.netty.learn.handler.replayingdecoder.MyByteToLongDecoder2;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyByteToLongDecoder2());
        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyClientHandler());

    }
}
