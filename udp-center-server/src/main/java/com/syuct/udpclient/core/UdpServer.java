package com.syuct.udpclient.core;

import com.syuct.core_service.protoc.TransMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class UdpServer {
    private Channel channel;
    public void run(int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
                        ChannelPipeline pipeline = nioDatagramChannel.pipeline();
                        pipeline.addLast("idleStateHandler", new IdleStateHandler(
                                210, 205, 0));
                        pipeline.addLast(new ProtobufVarint32FrameDecoder());
                        pipeline.addLast("protobufDecoder", new ProtobufDecoder(TransMessage.Message.getDefaultInstance()));
                        pipeline.addLast(new UdpHandler());
                        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                        pipeline.addLast("protobufEncoder", new ProtobufEncoder());
                    }
                });
        channel = b.bind(port).sync().channel();
        channel.closeFuture().await();
    }
}
