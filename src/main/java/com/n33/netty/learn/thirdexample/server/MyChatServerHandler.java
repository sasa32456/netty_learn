package com.n33.netty.learn.thirdexample.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * XXX上线
 * XXX下线
 *
 * @author N33
 * @date 2019/5/15
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 用来保存一个个channel对象
     */
    /**
     * Single-thread singleton { EventExecutor}.  It starts the thread automatically and stops it when there is no
     * task pending in the task queue for 1 second.  Please note it is not scalable to schedule large number of tasks to
     * this executor; use a dedicated executor.
     * 单线程单例{ EventExecutor}。它自动启动线程并在没有时停止它
     * 任务队列中的任务挂起1秒。请注意，安排大量任务无法扩展
     * 执行人;使用专门的Executor。例子中可以用，实际。。。
     * <p>
     * public interface ChannelGroup extends Set<Channel>, Comparable<ChannelGroup> {
     * 唯一
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        final Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (channel != ch) {
                //注意一定要有\n，不然对方不会读。。。估计是readline或者什么原因？
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息：" + msg + "\n");
            } else {
                //channel.writeAndFlush("【自己】" + msg + "\n");//一样
                ch.writeAndFlush("【自己】" + msg + "\n");
            }
        });

    }

    /**
     * 连接建立
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();

        /**
         * Writes the specified {@code message} to all {@link Channel}s in this
         * group. If the specified {@code message} is an instance of
         * {@link ByteBuf}, it is automatically
         * {@linkplain ByteBuf#duplicate() duplicated} to avoid a race
         * condition. The same is true for {@link ByteBufHolder}. Please note that this operation is asynchronous as
         * {@link Channel#write(Object)} is.
         *  将指定的{@code message}写入此中的所有{@link Channel}组。如果指定的{@code message}是。的实例{@link ByteBuf}，
         *  它是自动的{@linkplain ByteBuf＃duplicate（）duplicated}以避免比赛条件。 {@link ByteBufHolder}也是如此。请注意，此操作是异步的
         *  {@link Channel＃write（Object）}是。
         *
         * @return itself
         * ChannelGroupFuture write(Object message);
         *
         * Flush all {@link Channel}s in this
         * group. If the specified {@code messages} are an instance of
         * {@link ByteBuf}, it is automatically
         * {@linkplain ByteBuf#duplicate() duplicated} to avoid a race
         * condition. Please note that this operation is asynchronous as
         * {@link Channel#write(Object)} is.
         * 刷新所有{@link Channel}组。如果指定的{@code messages}是。的实例{@link ByteBuf}，它是自动的{@linkplain ByteBuf＃duplicate（）duplicated}
         * 以避免竞争条件。请注意，此操作是异步的{@link Channel＃write（Object）}是。
         *
         * @return the {@link ChannelGroupFuture} instance that notifies when
         *         the operation is done for all channels
         *   ChannelGroup flush();
         */
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 加入\n");

        //这句话在后面是为了避免自己广播自己
        channelGroup.add(channel);
    }

    /**
     * 断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();

        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 离开\n");

        //其实加不加无所谓，netty会自动移除
        //channelGroup.remove(channel);
        System.out.println(channelGroup.size());
    }

    /**
     * 连接活动状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线");
    }

    /**
     * 连接待用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
