package com.n33.netty.learn.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
* map映射
*
* @author N33
* @date 2019/4/28
*/
public class NioTest9 {
    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest9.txt", "rw");
        final FileChannel fileChannel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'a');

        //mappedByteBuffer.put(6, (byte) 'b');
        /** Exception in thread "main" java.lang.IndexOutOfBoundsException
         * 	at java.nio.Buffer.checkIndex(Buffer.java:540)
         * 	at java.nio.DirectByteBuffer.put(DirectByteBuffer.java:306)
         * 	at com.n33.netty.learn.nio.NioTest9.main(NioTest9.java:17)
         */
        mappedByteBuffer.put(3, (byte) 'b');
        randomAccessFile.close();


    }
}
