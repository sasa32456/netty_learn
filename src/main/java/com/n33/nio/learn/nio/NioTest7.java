package com.n33.nio.learn.nio;

import java.nio.ByteBuffer;

public class NioTest7 {
    public static void main(String[] args) {

        final ByteBuffer buffer = ByteBuffer.allocate(10);

        System.out.println(buffer);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }


        final ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        readOnlyBuffer.put(1, (byte) 1);
        /**
         * Exception in thread "main" java.nio.ReadOnlyBufferException
         * 	at java.nio.HeapByteBufferR.put(HeapByteBufferR.java:181)
         * 	at com.n33.nio.learn.nio.NioTest7.main(NioTest7.java:)
         */
        System.out.println(readOnlyBuffer);



    }
}
