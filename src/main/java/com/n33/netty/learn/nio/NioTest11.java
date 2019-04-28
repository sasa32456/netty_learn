package com.n33.netty.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering 传递buffer数组，按顺序读，读满才下一个
 * Gathering 反之，写满才下一个弄
 *
 * @author N33
 * @date 2019/4/28
 */
public class NioTest11 {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        final InetSocketAddress address = new InetSocketAddress(8899);

        serverSocketChannel.socket().bind(address);

        int messageLength = 2 + 3 + 4;

        ByteBuffer[] buffers = new ByteBuffer[3];

        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        final SocketChannel socketChannel = serverSocketChannel.accept();


        while (true) {
            int bytesRead = 0;

            while (bytesRead < messageLength) {
                long r = socketChannel.read(buffers);
                bytesRead += r;
                System.out.println("bytesRead: " + bytesRead);

                Arrays.asList(buffers).stream().
                        map(buffer -> "position: " + buffer.position() + ",limit: " + buffer.limit()).
                        forEach(System.out::println);
            }


            Arrays.asList(buffers).forEach(buffer -> {
                buffer.flip();
            });

            long bytesWritten = 0;
            while (bytesWritten < messageLength) {
                final long r = socketChannel.write(buffers);
                bytesWritten += r;
            }

            Arrays.asList(buffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("bytesRead: " + bytesRead + ",bytesWritten: " + bytesWritten + ",messageLength: "
                    + messageLength);
        }

    }
}
