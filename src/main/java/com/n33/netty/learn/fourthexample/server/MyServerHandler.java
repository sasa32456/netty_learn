package com.n33.netty.learn.fourthexample.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 适配器，子类超多
 *
 * @author N33
 * @date 2019/5/19
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;


            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    //针对服务器来说
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    //又没读，又没写
                    eventType = "读写空闲";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);

            ctx.channel().close();
        }
    }
}
/**
 * Calls {@link ChannelHandlerContext#fireUserEventTriggered(Object)} to forward to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
 * 调用{@link ChannelHandlerContext＃fireUserEventTriggered（Object）}转发到{@link ChannelPipeline}中的下一个{@link ChannelInboundHandler}。
 * <p>
 * Sub-classes may override this method to change behavior.
 * 子类可以覆盖此方法以更改行为。
 * <p>
 * public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {}
 * <p>
 * {@link Enum}代表{@link Channel}的空闲状态。
 * public enum IdleState {
 * 暂时没有收到任何数据。
 * READER_IDLE,
 * 暂时没有数据发送。
 * WRITER_IDLE,
 * 一段时间内没有收到或发送任何数据。
 * ALL_IDLE
 */
