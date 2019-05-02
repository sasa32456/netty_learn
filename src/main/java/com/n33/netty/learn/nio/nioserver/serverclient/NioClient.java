package com.n33.netty.learn.nio.nioserver.serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聊天客户端
 *
 * @author N33
 * @date 2019/5/2
 */
public class NioClient {

    public static void main(String[] args) throws IOException {


        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            //注册，是注册向服务器发起的连接
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

            while (true) {
                selector.select();
                final Set<SelectionKey> keySet = selector.selectedKeys();
                for (SelectionKey selectionKey : keySet) {
                    //判断服务器端是否建立好连接
                    if (selectionKey.isConnectable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();

                        //判断是否连接
                        if (client.isConnectionPending()) {
                            //完成连接
                            client.finishConnect();

                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                            writeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
                            writeBuffer.flip();
                            client.write(writeBuffer);

                            //输入开启新的线程来做(线程部分后面再补)
                            ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                            executorService.submit(() -> {
                                while (true) {
                                    try {
                                        writeBuffer.clear();
                                        //io这块也要补补了
                                        InputStreamReader input = new InputStreamReader(System.in);
                                        BufferedReader br = new BufferedReader(input);

                                        String sendMessage = br.readLine();

                                        writeBuffer.put(sendMessage.getBytes());
                                        writeBuffer.flip();
                                        client.write(writeBuffer);

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }

                        //注册读取事件
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        final SocketChannel client = (SocketChannel) selectionKey.channel();

                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                        int count = client.read(readBuffer);

                        if (count > 0) {
                            String receivedMessage = new String(readBuffer.array(), 0, count);
                            System.out.println(receivedMessage);
                        }
                    }
                    //试试看自己的移除方式,结果建议不在foreach中用，因为只能在迭代器中remove(阿里的规范，虽然不知道为啥，该复习基础了，估计为了循环不被空指针吧)
                    //使用正常(不清除也可以不报错？？？估计有且仅有一个？？？)
                    //keySet.remove(selectionKey);
                }
                //清空处理完成的selectKey
                keySet.clear();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
/**
 * Tells whether or not a connection operation is in progress on this
 * channel.
 * 判断连接操作是否正在进行中
 * 频道。 *
 *
 * @return <tt>true</tt> if, and only if, a connection operation has been
 * initiated on this channel but not yet completed by invoking the
 * {@link #finishConnect finishConnect} method
 * @return <tt>true</tt> if，且仅当在此通道上启动了连接操作但尚未通过调用{@link #finishConnect finishConnect}方法完成
 * public abstract boolean isConnectionPending();
 * <p>
 * Finishes the process of connecting a socket channel.
 * 完成连接套接字通道的过程。
 * *
 * <p> A non-blocking connection operation is initiated by placing a socket
 * channel in non-blocking mode and then invoking its {@link #connect
 * connect} method.  Once the connection is established, or the attempt has
 * failed, the socket channel will become connectable and this method may
 * be invoked to complete the connection sequence.  If the connection
 * operation failed then invoking this method will cause an appropriate
 * {@link java.io.IOException} to be thrown.
 * <p>通过放置套接字启动非阻塞连接操作
 * 通道处于非阻塞模式，然后调用其{@link #connect
 * connect}方法。建立连接后，或尝试连接
 * 失败，套接字通道将变为可连接，此方法可能
 * 被调用以完成连接序列。如果连接
 * 操作失败然后调用此方法将导致适当的
 * {@link java.io.IOException}将被抛出。
 * *
 * <p> If this channel is already connected then this method will not block
 * and will immediately return <tt>true</tt>.  If this channel is in
 * non-blocking mode then this method will return <tt>false</tt> if the
 * connection process is not yet complete.  If this channel is in blocking
 * mode then this method will block until the connection either completes
 * or fails, and will always either return <tt>true</tt> or throw a checked
 * exception describing the failure.
 * <p>如果此通道已连接，则此方法不会阻止
 * 并将立即返回<tt> true </ tt>。如果此频道在
 * 非阻塞模式然后这个方法将返回<tt> false </ tt>如果
 * 连接过程尚未完成。如果此频道处于阻止状态
 * 模式然后此方法将阻止，直到连接完成
 * 或失败，并将始终返回<tt> true </ tt>或抛出一个检查
 * 描述失败的异常。 *
 * <p> This method may be invoked at any time.  If a read or write
 * operation upon this channel is invoked while an invocation of this
 * method is in progress then that operation will first block until this
 * invocation is complete.  If a connection attempt fails, that is, if an
 * invocation of this method throws a checked exception, then the channel
 * will be closed.  </p>
 * <p>可以随时调用此方法。如果是读或写
 * 在调用此函数时调用此通道上的操作
 * 方法正在进行中，然后该操作将首先阻止，直到此为止
 * 调用完成。如果连接尝试失败，即，如果是
 * 调用此方法会抛出一个已检查的异常，然后抛出该通道
 * 将被关闭。 </ p> * @return <tt>true</tt> if, and only if, this channel's socket is now
 * connected
 * @throws NoConnectionPendingException
 * If this channel is not connected and a connection operation
 * has not been initiated
 * @throws ClosedChannelException
 * If this channel is closed
 * @throws AsynchronousCloseException
 * If another thread closes this channel
 * while the connect operation is in progress
 * @throws ClosedByInterruptException
 * If another thread interrupts the current thread
 * while the connect operation is in progress, thereby
 * closing the channel and setting the current thread's
 * interrupt status
 * @throws IOException
 * If some other I/O error occurs
 * <p>
 * public abstract boolean finishConnect() throws IOException;
 */

