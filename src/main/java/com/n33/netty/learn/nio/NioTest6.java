package com.n33.netty.learn.nio;

import java.nio.ByteBuffer;

/**
* Slice Buffer 拷贝地址
*
* @author N33
* @date 2019/4/28
*/
public class NioTest6 {
    public static void main(String[] args) {

        final ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        buffer.position(2);
        buffer.limit(6);

        final ByteBuffer sliceBuffer = buffer.slice();

        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i,b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
