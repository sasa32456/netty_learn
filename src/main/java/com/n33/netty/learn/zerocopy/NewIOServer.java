package com.n33.netty.learn.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端
 *
 * @author N33
 * @date 2019/7/17
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception {

        InetSocketAddress address = new InetSocketAddress(8899);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(true);

            int readCount = 0;

            while (-1 != readCount) {
                try {
                    readCount = socketChannel.read(byteBuffer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }

                byteBuffer.rewind();
            }
        }

    }
}
/**
 * 启用/禁用{@link SocketOptions＃SO_REUSEADDR SO_REUSEADDR}套接字选项。
 * <p>
 * 当TCP连接关闭时，连接可能会在连接关闭后的一段时间内保持超时状态（通常称为{@code TIME_WAIT}状态或{@code 2MSL}等待状态）
 * 。对于使用众所周知的套接字地址或端口的应用程序，如果在涉及套接字地址或端口的超时状态中存在连接，则可能无法将套接字绑定到所需的{@code SocketAddress}。
 * <p>
 * 在使用{@link #bind（SocketAddress）}绑定套接字之前启用{@link SocketOptions＃SO_REUSEADDR SO_REUSEADDR}允许套接字绑定，即使先前的连接处于超时状态。
 * <p>
 * 创建{@code ServerSocket}时，未定义{@link SocketOptions＃SO_REUSEADDR SO_REUSEADDR}的初始设置。
 * 应用程序可以使用{@link #getReuseAddress（）}来确定{@link SocketOptions＃SO_REUSEADDR SO_REUSEADDR}的初始设置。
 * <p>
 * 未定义在绑定套接字后启用或禁用{@link SocketOptions＃SO_REUSEADDR SO_REUSEADDR}时的行为（请参阅{@link #isBound（）}）。
 *
 * @param on 是否启用或禁用套接字选项
 * @exception SocketException如果发生错误，启用或禁用{@link SocketOptions＃SO_REUSEADDR SO_REUSEADDR}套接字选项，或者套接字已关闭。
 * <pre>
 * @since 1.4
 * @see #getReuseAddress()
 * @see #bind(SocketAddress)
 * @see #isBound()
 * @see #isClosed()
 *   public void setReuseAddress(boolean on) throws SocketException {
 *   </pre>
 */
