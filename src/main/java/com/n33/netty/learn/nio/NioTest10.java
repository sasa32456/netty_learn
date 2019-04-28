package com.n33.netty.learn.nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
* 文件锁(共享锁，排它锁)
*
* @author N33
* @date 2019/4/28
*/
public class NioTest10 {

    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest10.txt", "rw");
        final FileChannel fileChannel = randomAccessFile.getChannel();

        final FileLock fileLock = fileChannel.lock(3, 6, true);

        System.out.println("valid: " + fileLock.isValid());
        System.out.println("lock type: " + fileLock.isShared());

        fileLock.release();
        randomAccessFile.close();
    }
}
