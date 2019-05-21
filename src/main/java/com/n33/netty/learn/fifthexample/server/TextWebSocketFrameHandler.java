package com.n33.netty.learn.fifthexample.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * 处理WebSocket文本
 *
 * @author N33
 * @date 2019/5/22
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到消息： " + msg.text());
        //必须TextWebSocketFrame因为要要WebSocket解析
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间： " + LocalDateTime.now()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //ctx.channel().id()，全局唯一id
        System.out.println("handerAdded: " + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRomved: " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生");
        ctx.close();
    }
}
    /**
     * Returns the short but globally non-unique string representation of the {@link ChannelId}.
     *     String asShortText();
     */


    /**
     * Returns the long yet globally unique string representation of the {@link ChannelId}.
     *     String asLongText();
     */
