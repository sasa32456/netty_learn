package com.n33.netty.learn.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;
/**
 * 875ms
 *
 * @author N33
 * @date 2019/7/22
 */
public class OldClient {

    public static void main(String[] args) throws Exception{

        Socket socket = new Socket("localhost", 8899);

        String fileName = "D:/BaiduNetdiskDownload/精通lambda表达式  Java多核编程.pdf";
        InputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;

        long startTime = System.currentTimeMillis();

        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.println("发送的总字节数：" + total + "，耗时：" + (System.currentTimeMillis() - startTime));

        dataOutputStream.close();
        socket.close();
        inputStream.close();

    }
}
