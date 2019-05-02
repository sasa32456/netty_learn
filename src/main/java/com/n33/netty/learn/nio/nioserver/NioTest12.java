package com.n33.netty.learn.nio.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioTest12 {
    public static void main(String[] args) throws IOException {

        int[] posts = new int[5];

        posts[0] = 5000;
        posts[1] = 5001;
        posts[2] = 5002;
        posts[3] = 5003;
        posts[4] = 5004;

        Selector selector = Selector.open();

        for (int i = 0; i < posts.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(posts[i]);
            serverSocket.bind(address);

            //必须选OP_ACCEPT，因为要注册接收
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口: " + posts[i]);
        }

        while (true) {
            final int numbers = selector.select();
            System.out.println("numbers: " + numbers);

            final Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectKeys: " + selectionKeys);

            final Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                final SelectionKey selectionKey = iter.next();
                //如果已经有连接
                if (selectionKey.isAcceptable()) {
                    //获取selectionKey关联的serverSocketChannel对象
                    final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    //创建新的连接，上个连接只是为了发现连接
                    final SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    //关注读取信息
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    //移除连接，避免重复关注已经绑定的连接
                    iter.remove();

                    System.out.println("获取客户端的连接: " + socketChannel);
                }
                //获取可读的注册对象
                else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    int bytesRead = 0;

                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

                        byteBuffer.clear();

                        int read = socketChannel.read(byteBuffer);

                        if (read <= 0) {
                            break;
                        }

                        byteBuffer.flip();

                        socketChannel.write(byteBuffer);

                        bytesRead += read;
                    }

                    System.out.println("读取： "+ bytesRead +" , 来源于： " + socketChannel) ;

                    //一定要移除,用完就删
                    iter.remove();
                }


            }

        }


    }
}


/**
 * A token representing the registration of a {@link SelectableChannel} with a
 * {@link Selector}.
 * 表示{@link SelectableChannel}注册的令牌
 * {@link选择器}。 *
 * <p> A selection key is created each time a channel is registered with a
 * selector.  A key remains valid until it is <i>cancelled</i> by invoking its
 * {@link #cancel cancel} method, by closing its channel, or by closing its
 * selector.  Cancelling a key does not immediately remove it from its
 * selector; it is instead added to the selector's <a
 * href="Selector.html#ks"><i>cancelled-key set</i></a> for removal during the
 * next selection operation.  The validity of a key may be tested by invoking
 * its {@link #isValid isValid} method.
 * <p>每次使用a注册频道时，都会创建一个选择键
 * 选择器。密钥保持有效，直到通过调用<i>取消</ i>
 * {@link #cancel cancel}方法，关闭其频道，或关闭其频道
 * 选择器。取消密钥不会立即将其删除
 * 选择器;它被添加到选择器的<a
 * href =“Selector.html #ks”> <i>已取消密钥集</ i> </a>，以便在此期间删除
 * 下一步选择操作。可以通过调用来测试密钥的有效性
 * 其{@link #isValid isValid}方法。
 * *
 * <a name="opsets"></a>
 *
 * <p> A selection key contains two <i>operation sets</i> represented as
 * integer values.  Each bit of an operation set denotes a category of
 * selectable operations that are supported by the key's channel.
 * <p>选择键包含两个<i>操作集</ i>，表示为
 * 整数值。操作集的每个位表示一个类别
 * 密钥通道支持的可选操作。 *
 * <ul>
 *
 * <li><p> The <i>interest set</i> determines which operation categories will
 * be tested for readiness the next time one of the selector's selection
 * methods is invoked.  The interest set is initialized with the value given
 * when the key is created; it may later be changed via the {@link
 * #interestOps(int)} method. </p></li>
 * <li> <p> <i>兴趣集</ i>决定了哪些操作类别
 * 下次选择一个选择器时，测试是否准备就绪
 * 调用方法。利息集用给定的值初始化
 * 创建密钥时;稍后可以通过{@link更改它
 * #interestOps（int）}方法。 </ p> </ LI>
 * *
 * <li><p> The <i>ready set</i> identifies the operation categories for which
 * the key's channel has been detected to be ready by the key's selector.
 * The ready set is initialized to zero when the key is created; it may later
 * be updated by the selector during a selection operation, but it cannot be
 * updated directly. </p></li>
 * <li> <p> <i> ready set </ i>标识其操作类别
 * 钥匙的选择器检测到钥匙的通道准备就绪。
 * 创建密钥时，就绪集初始化为零;可能以后
 * 在选择操作期间由选择器更新，但不能
 * 直接更新。 </ p> </ LI> *
 * </ul>
 *
 * <p> That a selection key's ready set indicates that its channel is ready for
 * some operation category is a hint, but not a guarantee, that an operation in
 * such a category may be performed by a thread without causing the thread to
 * block.  A ready set is most likely to be accurate immediately after the
 * completion of a selection operation.  It is likely to be made inaccurate by
 * external events and by I/O operations that are invoked upon the
 * corresponding channel.
 * <p>选择键的就绪设置表示其通道已准备就绪
 * 某些操作类别是一个提示，但不是保证，即一个操作
 * 这样的类别可以由线程执行而不会导致线程
 * 阻止。准备好的集合最有可能在紧随其后
 * 完成选择操作。它很可能会变得不准确
 * 外部事件和通过I / O操作调用
 * 相应的渠道。 *
 * <p> This class defines all known operation-set bits, but precisely which
 * bits are supported by a given channel depends upon the type of the channel.
 * Each subclass of {@link SelectableChannel} defines an {@link
 * SelectableChannel#validOps() validOps()} method which returns a set
 * identifying just those operations that are supported by the channel.  An
 * attempt to set or test an operation-set bit that is not supported by a key's
 * channel will result in an appropriate run-time exception.
 * <p>这个类定义了所有已知的操作集位，但确切地说是哪个
 * 给定通道支持的位取决于通道的类型。
 * {@link SelectableChannel}的每个子类定义{@link
 * SelectableChannel＃validOps（）validOps（）}方法，返回一个集合
 * 仅识别渠道支持的那些操作。一个
 * 尝试设置或测试密钥不支持的操作设置位
 * 通道将导致适当的运行时异常。 *
 * <p> It is often necessary to associate some application-specific data with a
 * selection key, for example an object that represents the state of a
 * higher-level protocol and handles readiness notifications in order to
 * implement that protocol.  Selection keys therefore support the
 * <i>attachment</i> of a single arbitrary object to a key.  An object can be
 * attached via the {@link #attach attach} method and then later retrieved via
 * the {@link #attachment() attachment} method.
 * <p>通常需要将一些特定于应用程序的数据与a关联起来
 * 选择键，例如表示a的状态的对象
 * 更高级别的协议并处理准备就绪通知
 * 实施该协议。因此选择键支持
 * <i>附加</ i>单个任意对象到一个键。一个对象可以
 * 通过{@link #attach attach}方法附加，然后通过
 * {@link #attachment（）attachment}方法。 *
 * <p> Selection keys are safe for use by multiple concurrent threads.  The
 * operations of reading and writing the interest set will, in general, be
 * synchronized with certain operations of the selector.  Exactly how this
 * synchronization is performed is implementation-dependent: In a naive
 * implementation, reading or writing the interest set may block indefinitely
 * if a selection operation is already in progress; in a high-performance
 * implementation, reading or writing the interest set may block briefly, if at
 * all.  In any case, a selection operation will always use the interest-set
 * value that was current at the moment that the operation began.  </p>
 * <p>多个并发线程可以安全地使用选择键。该
 * 一般来说，阅读和写作兴趣集的操作
 * 与选择器的某些操作同步。究竟是怎么回事
 * 执行同步是依赖于实现的：在一个天真的
 * 实施，阅读或写入利息集可能会无限期地阻止
 * 如果选择操作已在进行中;在高性能
 * 实施，阅读或撰写兴趣集可能会暂时阻止，如果在
 * 全部。在任何情况下，选择操作将始终使用兴趣集
 * 操作开始时的当前值。 </ p> *
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @see SelectableChannel
 * @see Selector
 * <p>
 * <p>
 * public abstract class SelectionKey {
 * <p>
 * Operation-set bit for socket-accept operations.
 * 用于套接字操作的操作设置位。             *
 * <p> Suppose that a selection key's interest set contains
 * <p>假设选择键的兴趣集包含
 * * <tt>OP_ACCEPT</tt> at the start of a <a
 * href="Selector.html#selop">selection operation</a>.  If the selector
 * detects that the corresponding server-socket channel is ready to accept
 * another connection, or has an error pending, then it will add
 * <tt>OP_ACCEPT</tt> to the key's ready set and add the key to its
 * selected-key&nbsp;set.  </p>
 * <tt> OP_ACCEPT </ tt>在<a的开头
 * href =“Selector.html＃selop”>选择操作</a>。如果选择器
 * 检测到相应的服务器套接字通道已准备好接受
 * 另一个连接，或有一个错误待处理，然后它将添加
 * <tt> OP_ACCEPT </ tt>到密钥的就绪集并将密钥添加到其中
 * select-key＆nbsp; set。 </ p>
 * <p>
 * public static final int OP_ACCEPT = 1 << 4;
 */
