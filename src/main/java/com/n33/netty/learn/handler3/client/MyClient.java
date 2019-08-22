package com.n33.netty.learn.handler3.client;

import com.n33.netty.learn.handler3.mydecoder.MyPersonDecoder;
import com.n33.netty.learn.handler3.mydecoder.MyPersonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 *
 * @author N33
 * @date 2019/8/22
 */
public class MyClient {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            //服务器端用ServerBootStrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(
                    //服务器用NioServerSocketChannel
                    NioSocketChannel.class)
                    //服务器用ChildHandler,handler和childHandler区别，前者针对bossGroup,后者对应workGroup
                    //可以直接匿名内部类
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new MyPersonEncoder());
                            pipeline.addLast(new MyPersonDecoder());

                            pipeline.addLast(new MyClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost",8899).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }




    }
}
