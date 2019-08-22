package com.n33.netty.learn.handler.bytemessagedecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
/**
 * 自定义解码器
 *
 * @author N33
 * @date 2019/8/20
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode invoked!");

        System.out.println(in.readableBytes());

        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
