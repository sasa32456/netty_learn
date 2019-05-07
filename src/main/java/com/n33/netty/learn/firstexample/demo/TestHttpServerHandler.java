package com.n33.netty.learn.firstexample.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.net.URI;

/**
 * {@link ChannelInboundHandlerAdapter} which allows to explicit only handle a specific type of messages.
 * {@link ChannelInboundHandlerAdapter}，它允许显式只处理特定类型的消息。 *
 * For example here is an implementation which only handle {@link String} messages.
 *
 * <pre>
 *     public class StringHandler extends
 *             {@link SimpleChannelInboundHandler}&lt;{@link String}&gt; {
 *
 *         {@code @Override}
 *         protected void channelRead0({@link ChannelHandlerContext} ctx, {@link String} message)
 *                 throws {@link Exception} {
 *             System.out.println(message);
 *         }
 *     }
 * </pre>
 * <p>
 * Be aware that depending of the constructor parameters it will release all handled messages by passing them to
 * {@link ReferenceCountUtil#release(Object)}. In this case you may need to use
 * {@link ReferenceCountUtil#retain(Object)} if you pass the object to the next handler in the {@link ChannelPipeline}.
 * 请注意，根据构造函数参数，它将通过传递它们来释放所有处理过的消息
 * {@link ReferenceCountUtil＃release（Object）}。在这种情况下，您可能需要使用
 * {@link ReferenceCountUtil＃retain（Object）}如果将对象传递给{@link ChannelPipeline}中的下一个处理程序。 *
 * <h3>Forward compatibility notice</h3>
 * <p>
 * Please keep in mind that { #channelRead0(ChannelHandlerContext, I)} will be renamed to
 * 请记住，{@ link＃channelRead0（ChannelHandlerContext，I）}将被重命名为
 * 5.0中的@code messageReceived收到消息（ChannelHandlerContext，I）}。
 * </p>
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * Enables a {@link ChannelHandler} to interact with its {@link ChannelPipeline}
     * * and other handlers. Among other things a handler can notify the next {@link ChannelHandler} in the
     * * {@link ChannelPipeline} as well as modify the {@link ChannelPipeline} it belongs to dynamically.
     * 允许{@link ChannelHandler}与其{@link ChannelPipeline}进行互动
     * *和其他处理程序。处理程序可以通知下一个{@link ChannelHandler}
     * * {@link ChannelPipeline}以及动态修改它所属的{@link ChannelPipeline}。
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;

            System.out.println("请求方法名: " + httpRequest.method().name());

            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
                return;
            }

            //System.out.println("执行channelRead0");

            //向客户端返回的相应内容
            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
            //反馈
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //write放入缓冲，flush提交
            ctx.writeAndFlush(response);
        }

    }
}
