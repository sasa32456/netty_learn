package com.n33.nio.learn.nio.nioserver.serverclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        final ServerSocket serverSocket = serverSocketChannel.socket();
        //等同于<p>serverSocketChannel.socket().bind(new InetSocketAddress(8899))</p>;
        serverSocket.bind(new InetSocketAddress(8899));


        final Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (true) {

            try {
                //阻塞，等待
                selector.select();

                //selectKey可以获取事件集合，同时可以追溯channel
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();

                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;


                    try {
                        if (selectionKey.isAcceptable()) {
                            final ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);

                            //将注册key 维护
                            String key = " 【" + UUID.randomUUID().toString() + "】 ";
                            System.out.println(key);
                            clientMap.put(key, client);
                        } else if (selectionKey.isReadable()) {
                            //此时不用再次接受，可以直接获得，因为已经注册
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            int count = client.read(readBuffer);
                            //简化，实际要循环
                            if (count > 0) {
                                readBuffer.flip();

                                //字符集
                                final Charset charset = Charset.forName("utf-8");
                                String receivedMessage = String.valueOf(
                                        //charBuffer
                                        charset.decode(readBuffer)
                                                //char[]从而被valueof到string类型
                                                .array()
                                );

                                System.out.println(client + ": " + receivedMessage);

                                String senderKey = null;

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    SocketChannel value = entry.getValue();

                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                                    writeBuffer.put((senderKey + ": " + receivedMessage).getBytes());
                                    writeBuffer.flip();

                                    value.write(writeBuffer);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                //特别重要,清空selectKey
                selectionKeys.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
