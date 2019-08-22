package com.n33.netty.learn.handler.replayingdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;
/**
 * 不用检查缓冲长度了
 *
 * @author N33
 * @date 2019/8/21
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder2 decode invoked!");

        out.add(in.readLong());
    }
}
