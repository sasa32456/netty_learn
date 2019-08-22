package com.n33.netty.learn.handler.messagetomessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
/**
 * 泛型为输入数据类型
 *
 * @author N33
 * @date 2019/8/21
 */
public class MyLongToStringDecoder extends MessageToMessageDecoder<Long> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Long msg, List<Object> out) throws Exception {
        System.out.println("MyLongToStringDecoder decode invoked!");

        out.add(String.valueOf(msg));
    }
}
