package com.n33.netty.learn.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * wrap(byte[] array)不要用，直接修改数组或buffer都会修改
 * <p>
 * allocate java内存，当使用时，会在系统内存中copy一份
 * <p>
 * <p>
 * allocateDirect 直接读取内存，零拷贝
 *
 * @author N33
 * @date 2019/4/28
 */
public class NioTest8 {
    public static void main(String[] args) throws Exception {

        FileInputStream inputStream = new FileInputStream("input2.txt");
        FileOutputStream outputStream = new FileOutputStream("output2.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(10);

        while (true) {
            buffer.clear();

            final int read = inputChannel.read(buffer);

            System.out.println("read: " + read);

            if (-1 == read) {
                break;
            }

            buffer.flip();
            outputChannel.write(buffer);

        }


        inputChannel.close();
        outputChannel.close();


    }
}
