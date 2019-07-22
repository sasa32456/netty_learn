package com.n33.netty.learn.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;

/**
 * windows系统下传输要transferTo多
 * 因为windows最大8m
 * 110ms
 *
 * @author N33
 * @date 2019/7/22
 */
public class NewIOClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        /**
         * 调整此通道的阻止模式。
         *
         * <p>如果给定的阻止模式与当前的阻塞模式不同
         * mode然后这个方法调用{@link #implConfigureBlocking
         * implConfigureBlocking}方法，同时持有相应的锁，在
         *为了改变模式。 </ p>
         */
        socketChannel.configureBlocking(true);

        String fileName = "D:/BaiduNetdiskDownload/精通lambda表达式  Java多核编程.pdf";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        long transferCount = 0;

        for (int i = 0; i < fileChannel.size(); ) {
            long num = fileChannel.transferTo(i, fileChannel.size(), socketChannel);
            transferCount += num;
            i += num;
        }

        System.out.println(fileChannel.size());
        System.out.println("发送总字节数：" + transferCount + "，耗时：" + (System.currentTimeMillis() - startTime));

        fileChannel.close();


    }
}

/**
 * 用于读取，写入，映射和操作文件的通道。
 *
 * <p>文件通道是连接到文件的{@link SeekableByteChannel}。它的文件中有一个当前的<i>位置</ i>，
 * 可以是{@link #position（）<i>查询</ i>}和{@link #position（long）<i>修改</ i> I>}。该文件本身包含一个可变长度的字节序列，
 * 可以读取和写入，并且可以查询其当前{@link #size <i> size </ i>}。当字节写入超出其当前大小时，文件的大小会增加;
 * 当{@link #truncate <i>截断</ i>}时，文件的大小会减小。该文件还可以具有一些关联的<i>元数据</ i>，例如访问权限，
 * 内容类型和最后修改时间;此类未定义元数据访问的方法。
 *
 * <p>除了熟悉的字节通道的读，写和关闭操作外，该类还定义了以下特定于文件的操作：</ p>
 *
 * <ul>
 *
 * <li> <p>字节可以是{@link #read（ByteBuffer，long）read}或{@link #write（ByteBuffer，long）<i> write </ i>}在文​​
 * 件中的绝对位置不影响频道当前位置的方式。 </ p> </ LI>
 *
 * <li> <p>文件的某个区域可以{@link #map <i>映射</ i>}直接进入内存;对于大文件，这通常比调用通常的<tt> read </ tt>或<tt> write </ tt>方法更有效。 </ p> </ LI>
 *
 * <li> <p>对文件进行的更新可能是{@link #force <i>强制退出</ i>}到底层存储设备，确保在系统崩溃时数据不会丢失。 </ p> </ LI>
 *
 * <li> <p>字节可以从文件{@link #traferferTo <i>转移到其他某个频道</ i>}，以及{@link #transferFrom <i>反之亦然</ i>}，
 * 一种可以被许多操作系统优化为直接进出文件系统缓存的快速传输方式。 </ p> </ LI>
 *
 * <li> <p>文件的某个区域可能是{@link FileLock <i>已锁定</ i>}，以防其他程序访问。 </ p> </ LI>
 *
 * </ul>
 *
 * <p>文件通道可供多个并发线程使用。 {@link Channel＃close close}方法可以随时调用，如{@link Channel}接口所指定。
 * 在任何给定时间，只有一个涉及通道位置或可以更改其文件大小的操作可能正在进行中;在第一个操作仍在进行时尝试启动第二个此类操作将阻塞，
 * 直到第一个操作完成。其他操作，特别是那些采取明确立场的操作，可以同时进行;他们实际上是否这样做取决于基本的实施，因此没有具体说明。
 *
 * <p>此类实例提供的文件视图保证与同一程序中其他实例提供的同一文件的其他视图一致。然而，由于底层操作系统执行的高速缓存和网络文件系统协议引起的延迟，
 * 该类实例提供的视图可能会也可能不会与其他同时运行的程序所看到的视图一致。无论这些其他程序的编写语言是什么，以及它们是在同一台机器上运行还是在其他机器上运行，都
 * 是如此。任何此类不一致的确切性质都取决于系统，因此未指定。
 *
 * <p>通过调用此类定义的{@link #open open}方法之一来创建文件通道。文件通道也可以从现有的{@link java.io.FileInputStream＃getChannel FileInputStream}，
 * {@link java.io.FileOutputStream＃getChannel FileOutputStream}或{@link java.io.RandomAccessFile＃getChannel RandomAccessFile}对象中获取
 * 通过调用该对象的<tt> getChannel </ tt>方法，该方法返回连接到同一基础文件的文件通道。在从现有流或随机访问文件获得文件通道的情况下，
 * 文件通道的状态与其<tt> getChannel </ tt>方法返回通道的对象的状态紧密相关。无论是显式地还是通过读取或写入字节来改变通道的位置，
 * 都将改变原始对象的文件位置，反之亦然。通过文件通道更改文件长度将改变通过原始对象看到的长度，反之亦然。
 * 通过写入字节来更改文件的内容将更改原始对象看到的内容，反之亦然。
 *
 * <a name="open-mode"> </a> <p>此类在各个点指定“打开以供阅读”，“打开以供写入”或“打开以进行读写”的实例需要。
 * 通过{@link java.io.FileInputStream}实例的{@link java.io.FileInputStream＃getChannel getChannel}方法获得的通道将打开以供阅读。
 * 通过{@link java.io.FileOutputStream}实例的{@link java.io.FileOutputStream＃getChannel getChannel}方法获得的通道将打开以进行写入。
 * 最后，如果使用mode <tt>“r”创建实例，则通过{@link java.io.RandomAccessFile}实例的{@link java.io.RandomAccessFile＃getChannel getChannel}
 * 方法获取的通道将打开以供读取</ tt>如果实例是以模式<tt>“rw”</ tt>创建的，则将打开以进行读写。
 *
 * <a name="append-mode"> </a> <p>打开以进行写入的文件通道可能处于<i>追加模式</ i>，例如，
 * 如果它是从文件输出中获取的通过调用{@link java.io.FileOutputStream＃FileOutputStream
 * （java.io.File，boolean）FileOutputStream（File，boolean）}构造函数并为第二个参数传递<tt> true </ tt>创建的流。
 * 在此模式下，相对写入操作的每次调用首先将位置前进到文件的末尾，然后写入所请求的数据。
 * 位置的推进和数据的写入是在单个原子操作中完成的是系统相关的，因此是未指定的。
 * <pre>
 * @see java.io.FileInputStream#getChannel()
 * @see java.io.FileOutputStream#getChannel()
 * @see java.io.RandomAccessFile#getChannel()
 *
 * @author Mark Reinhold
 * @author Mike McCloskey
 * @author JSR-51 Expert Group
 * @since 1.4
 * public abstract class FileChannel
 *         extends AbstractInterruptibleChannel
 *         implements SeekableByteChannel, GatheringByteChannel, ScatteringByteChannel{
 *      </pre>
 * 将此通道文件中的字节传输到给定的可写字节通道。
 *
 * <p>尝试从该通道文件中的给定<tt>位置</ tt>开始读取<tt> count </ tt>个字节，并将它们写入目标通道。
 * 调用此方法可能会也可能不会传输所有请求的字节;是否这样做取决于渠道的性质和状态。如果此通道的文件包含从给定的<tt>位置开始的<tt> count </ tt>个字节，
 * 或者如果目标通道是非阻塞的，则传输的字节数少于所请求的字节数输出缓冲区中的空闲字节数小于<tt> count </ tt>。
 *
 * <p>此方法不会修改此通道的位置。如果给定位置大于文件的当前大小，则不传输任何字节。如果目标通道有一个位置，
 * 则从该位置开始写入字节，然后该位置按写入的字节数递增。
 *
 * <p>此方法可能比从此通道读取并写入目标通道的简单循环更有效。许多操作系统可以直接从文件系统缓存向目标通道传输字节，
 * 而无需实际复制它们。 </ p>
 *
 * @param position
 * The position within the file at which the transfer is to begin;
 * must be non-negative
 * @param count
 * The maximum number of bytes to be transferred; must be
 * non-negative
 * @param target
 * The target channel
 * @return The number of bytes, possibly zero,
 * that were actually transferred
 * @throws IllegalArgumentException
 * If the preconditions on the parameters do not hold
 * @throws NonReadableChannelException
 * If this channel was not opened for reading
 * @throws NonWritableChannelException
 * If the target channel was not opened for writing
 * @throws ClosedChannelException
 * If either this channel or the target channel is closed
 * @throws AsynchronousCloseException
 * If another thread closes either channel
 * while the transfer is in progress
 * @throws ClosedByInterruptException
 * If another thread interrupts the current thread while the
 * transfer is in progress, thereby closing both channels and
 * setting the current thread's interrupt status
 * @throws IOException
 * If some other I/O error occurs
 * public abstract long transferTo(long position,long count, WritableByteChannel target) throws IOException;
 * <p>
 * <p>
 * <p>
 * 从给定的可读字节通道将字节传输到此通道的文件中。
 *
 * <p>尝试从源通道读取<tt> count </ tt>个字节，并将它们从给定的<tt>位置</ tt>开始写入此通道的文件。
 * 调用此方法可能会也可能不会传输所有请求的字节;是否这样做取决于渠道的性质和状态。
 * 如果源通道剩余少于<tt> count </ tt>个字节，或者源通道非阻塞且小于<tt> count </ tt>字节，则将传输少于请求的字节数立即在其输入缓冲区中可用。
 *
 * <p>此方法不会修改此通道的位置。如果给定位置大于文件的当前大小，则不传输任何字节。
 * 如果源通道有一个位置，那么从该位置开始读取字节，然后该位置按读取的字节数递增。
 *
 * <p>这种方法可能比从源通道读取并写入此通道的简单循环更有效。
 * 许多操作系统可以直接从源通道将字节传输到文件系统缓存中，而无需实际复制它们。 </ p>
 * @param src
 * The source channel
 * @param position
 * The position within the file at which the transfer is to begin;
 * must be non-negative
 * @param count
 * The maximum number of bytes to be transferred; must be
 * non-negative
 * @return The number of bytes, possibly zero,
 * that were actually transferred
 * @throws IllegalArgumentException
 * If the preconditions on the parameters do not hold
 * @throws NonReadableChannelException
 * If the source channel was not opened for reading
 * @throws NonWritableChannelException
 * If this channel was not opened for writing
 * @throws ClosedChannelException
 * If either this channel or the source channel is closed
 * @throws AsynchronousCloseException
 * If another thread closes either channel
 * while the transfer is in progress
 * @throws ClosedByInterruptException
 * If another thread interrupts the current thread while the
 * transfer is in progress, thereby closing both channels and
 * setting the current thread's interrupt status
 * @throws IOException
 * If some other I/O error occurs
 * public abstract long transferFrom(ReadableByteChannel src, long position,long count) throws IOException;
 * <p>
 * }
 */
