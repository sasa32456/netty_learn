package com.n33.netty.learn.sixthexample.server;

import com.n33.netty.learn.sixthexample.MyDataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author N33
 * @date 2019/6/25
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        //此方法获取要传输的类型
        pipeline.addLast(new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));

        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());

        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new TestServerHandler());

    }
}
/**
 * 将收到的{@link ByteBuf}解码为<a href="https://github.com/google/protobuf"> Google协议缓冲区</a> {@link Message}和{@link MessageLite}。
 * 请注意，如果您使用的是基于流的传输（如TCP / IP），则此解码器必须与正确的{@link ByteToMessageDecoder}一起使用，
 * 例如{@link ProtobufVarint32FrameDecoder}或{@link LengthFieldBasedFrameDecoder}。 TCP / IP的典型设置是：
 * <pre>
 * {@link ChannelPipeline} pipeline = ...;
 *
 * // Decoders
 * pipeline.addLast("frameDecoder",
 *                  new {@link LengthFieldBasedFrameDecoder}(1048576, 0, 4, 0, 4));
 * pipeline.addLast("protobufDecoder",
 *                  new {@link ProtobufDecoder}(MyMessage.getDefaultInstance()));
 *
 * // Encoder
 * pipeline.addLast("frameEncoder", new {@link LengthFieldPrepender}(4));
 * pipeline.addLast("protobufEncoder", new {@link ProtobufEncoder}());
 * </pre>
 * 然后你可以使用{@code MyMessage}而不是{@link ByteBuf}作为消息：
 * <pre>
 * void channelRead({@link ChannelHandlerContext} ctx, Object msg) {
 *     MyMessage req = (MyMessage) msg;
 *     MyMessage res = MyMessage.newBuilder().setText(
 *                               "Did you say '" + req.getText() + "'?").build();
 *     ch.write(res);
 * }
 *
 * @Sharable
 *public class ProtobufDecoder扩展MessageToMessageDecoder <ByteBuf> {
 * </ pre>
 *
 *
 * Protocol Message对象实现的抽象接口。
 *
 * <p>此接口由所有协议消息对象实现。非lite消息还实现了Message接口，它是MessageLite的子类。
 * 当您只需要它支持的功能子集时，请使用MessageLite  - 即，不使用描述符或反射。
 * 您可以通过将以下行添加到.proto文件来指示协议编译器生成仅实现MessageLite的类，而不是完整的Message接口：
 *
 * <pre>
 * option optimize_for = LITE_RUNTIME;
 * </ pre>
 *
 * <p>这在资源受限的系统上特别有用，在这些系统中，完整协议缓冲区运行时库太大。
 *
 * <p>请注意，在需要链接大量协议定义时，在非约束系统（例如服务器）上，减少总代码占用量的更好方法是使用{@code optimize_for = CODE_SIZE}。
 * 这将使生成的代码更小，同时仍支持所有相同的功能（以牺牲速度为代价）。
 * 当您只有少量链接到二进制文件的消息类型时，{@code optimize_for = LITE_RUNTIME}是最好的，
 * 在这种情况下，协议缓冲区运行时本身的大小是最大的问题。
 *
 * @author kenton@google.com Kenton Varda
 * <pre>public interface MessageLite extends MessageLiteOrBuilder {</pre>
 */

