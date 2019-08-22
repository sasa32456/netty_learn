package com.n33.netty.learn.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * byteBuf学习
 *
 * @author N33
 * @date 2019/8/16
 */
public class ByteBufTest {

    public static void main(String[] args) {

        ByteBuf byteBuf = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.getByte(i));
            System.out.println(byteBuf.readByte());
            System.out.println("--------------------------------");
        }

    }

}
