package com.n33.netty.learn.fourthexample.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        /**
         * Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
         * read, write, or both operation for a while.
         * 当{@link Channel}暂时没有执行读，写或两者操作时，触发{@link IdleStateEvent}。
         *
         * public class IdleStateHandler extends ChannelDuplexHandler {
         *
         *
         *      Creates a new instance firing {@link IdleStateEvent}s.
         *      创建一个触发{@link IdleStateEvent}的新实例。
         *
         *      @param readerIdleTimeSeconds
         *            {@link IdleStateEvent}，状态为{@link IdleState #READER_IDLE}未对指定的内容执行读取时将触发一段的时间。指定{@code 0}以禁用。
         *      @param writerIdleTimeSeconds
         *             {@link IdleStateEvent}，状态为{@link IdleState＃WRITER_IDLE}当没有对指定的执行写入时将触发一段的时间。指定{@code 0}以禁用。
         *      @param allIdleTimeSeconds
         *         {@link IdleStateEvent}，状态为{@link IdleState＃ALL_IDLE}既不执行读取也不执行写入将被触发指定的时间段。指定{@code 0}以禁用。
         *
         *     public IdleStateHandler(
         *              int readerIdleTimeSeconds,
         *              int writerIdleTimeSeconds,
         *              int allIdleTimeSeconds){
         *                  this(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds,
         *                  TimeUnit.SECONDS);
         *             }
         * 空闲状态监测处理
         */
        pipeline.addLast(new IdleStateHandler(5, 7, 3, TimeUnit.SECONDS));
        pipeline.addLast(new MyServerHandler());

    }
}
