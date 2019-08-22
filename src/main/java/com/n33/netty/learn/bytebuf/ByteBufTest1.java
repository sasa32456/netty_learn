package com.n33.netty.learn.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author N33
 * @date 2019/8/18
 */
public class ByteBufTest1 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("张hello world", Charset.forName("utf-8"));

        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            System.out.println(new String(content,Charset.forName("utf-8")));
            System.out.println(byteBuf);

            System.out.println("------------------------");
            //数组偏移量
            System.out.println(byteBuf.arrayOffset());
            //读指针
            System.out.println(byteBuf.readerIndex());
            //写指针
            System.out.println(byteBuf.writerIndex());
            //容量，可以自动扩容
            System.out.println(byteBuf.capacity());
            //可读字节数(w-r)
            System.out.println(byteBuf.readableBytes());

            for (int i = 0; i < byteBuf.readableBytes(); i++) {
                System.out.print((char)byteBuf.getByte(i));
            }
            System.out.println("\n=========================================");
            //打出部分
            //张h
            System.out.println(byteBuf.getCharSequence(0, 4, Charset.forName("utf-8")));
            //从4开始打出6个
            //ello w
            System.out.println(byteBuf.getCharSequence(4, 6, Charset.forName("utf-8")));
        }


    }
}
