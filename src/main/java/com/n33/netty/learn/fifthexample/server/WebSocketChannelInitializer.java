package com.n33.netty.learn.fifthexample.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());
        /**
         * 以块的方式写
         */
        pipeline.addLast(new ChunkedWriteHandler());

        /**
         * 对http消息聚合,将后续聚合成一个FullHttpRequest
         */
        pipeline.addLast(new HttpObjectAggregator(8192));

        /**
         * 用于处理webSocketFrame六种类型
         * ws://localhost:9999/ws
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws/example"));

        pipeline.addLast(new TextWebSocketFrameHandler());

    }
}
/**
 * A {@link ChannelHandler} that adds support for writing a large data stream
 * asynchronously neither spending a lot of memory nor getting
 * {@link OutOfMemoryError}.  Large data streaming such as file
 * transfer requires complicated state management in a {@link ChannelHandler}
 * implementation.  {@link ChunkedWriteHandler} manages such complicated states
 * so that you can send a large data stream without difficulties.
 * 一个{@link ChannelHandler}，它增加了对异步写入大数据流的支持，既不花费大量内存也不获取{@link OutOfMemoryError}。
 * 诸如文件传输之类的大数据流需要在{@link ChannelHandler}实现中进行复杂的状态管理。
 * {@link ChunkedWriteHandler}管理这些复杂的状态，以便您可以毫无困难地发送大型数据流。
 * public class ChunkedWriteHandler extends ChannelDuplexHandler {
 * ---------------------------------
 * A {@link ChannelHandler} that aggregates an {@link HttpMessage}
 * and its following {@link HttpContent}s into a single {@link FullHttpRequest}
 * or {@link FullHttpResponse} (depending on if it used to handle requests or responses)
 * with no following {@link HttpContent}s.  It is useful when you don't want to take
 * care of HTTP messages whose transfer encoding is 'chunked'.  Insert this
 * handler after {@link HttpResponseDecoder} in the {@link ChannelPipeline} if being used to handle
 * responses, or after {@link HttpRequestDecoder} and {@link HttpResponseEncoder} in the
 * {@link ChannelPipeline} if being used to handle requests.
 * 一个{@link ChannelHandler}，它将{@link HttpMessage}及其后续{@link HttpContent}聚合成一个{@link FullHttpRequest}或{@link FullHttpResponse}
 * （取决于它是否用于处理请求或响应）没有关注{@link HttpContent}。当您不想处理传输编码为“chunked”的HTTP消息时，它非常有用。
 * 如果用于处理响应，则在@@link ChannelPipeline}中的{@link HttpResponseDecoder}之后插入此处理程序，如果用于处理请求，
 * 则在{@link ChannelPipeline}中的{@link HttpRequestDecoder}和{@link HttpResponseEncoder}之后插入此处理程序。
 * public class HttpObjectAggregator
 * extends MessageAggregator<HttpObject, HttpMessage, HttpContent, FullHttpMessage> {
 * <p>
 * -----------------------------------------
 * This handler does all the heavy lifting for you to run a websocket server.
 * <p>
 * It takes care of websocket handshaking as well as processing of control frames (Close, Ping, Pong). Text and Binary
 * data frames are passed to the next handler in the pipeline (implemented by you) for processing.
 * <p>
 * See <tt>io.netty.example.http.websocketx.html5.WebSocketServer</tt> for usage.
 * <p>
 * The implementation of this handler assumes that you just want to run  a websocket server and not process other types
 * HTTP requests (like GET and POST). If you wish to support both HTTP requests and websockets in the one server, refer
 * to the <tt>io.netty.example.http.websocketx.server.WebSocketServer</tt> example.
 * <p>
 * To know once a handshake was done you can intercept the
 * {@link ChannelInboundHandler#userEventTriggered(ChannelHandlerContext, Object)} and check if the event was instance
 * of {@link HandshakeComplete}, the event will contain extra information about the handshake such as the request and
 * selected subprotocol.
 * <p>
 * 这个处理程序为您运行websocket服务器做了所有繁重的工作。
 * 它负责websocket握手以及控制框架的处理（Close，Ping，Pong）。文本和二进制数据帧将传递给管道中的下一个处理程序（由您实现）进行处理。
 * 请参阅<tt> io.netty.example.http.websocketx.html5.WebSocketServer </ tt>以了解用法。
 * 此处理程序的实现假定您只想运行websocket服务器而不处理其他类型的HTTP请求（如GET和POST）。
 * 如果您希望在一台服务器中同时支持HTTP请求和websockets，请参阅<tt> io.netty.example.http.websocketx.server.WebSocketServer </ tt>示例。
 * 要知道握手完成后你可以截取{@link ChannelInboundHandler＃userEventTriggered（ChannelHandlerContext，Object）}
 * 并检查事件是否是{@link HandshakeComplete}的实例，该事件将包含有关握手的额外信息，例如请求和选定的子协议。
 * public class WebSocketServerProtocolHandler extends WebSocketProtocolHandler {
 */
