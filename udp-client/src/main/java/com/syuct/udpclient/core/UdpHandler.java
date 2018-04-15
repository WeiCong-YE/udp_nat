package com.syuct.udpclient.core;

import com.google.gson.Gson;
import com.syuct.core_service.protoc.TransMessage;
import com.syuct.udpclient.constant.ServerAddress;
import com.syuct.udpclient.constant.ServerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.UUID;


public class UdpHandler extends SimpleChannelInboundHandler<DatagramPacket>{


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        TransMessage.Head.Builder head = TransMessage.Head.newBuilder();
        head.setStatus(TransMessage.status.REQ);
        head.setType(TransMessage.type.HANDSHAKE);
        head.setUid(UUID.randomUUID().toString());
        head.setTime(System.currentTimeMillis());
        TransMessage.Message.Builder message = TransMessage.Message.newBuilder();
        message.setHead(head);
        message.setBody(ServerConfig.self);
        ctx.writeAndFlush(
                new DatagramPacket(Unpooled.copiedBuffer(message.build().toByteArray()),
                        new InetSocketAddress(ServerConfig.Server, ServerConfig.port)));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        InetSocketAddress sender = datagramPacket.sender();
        Gson gson = new Gson();
        ByteBuf content = datagramPacket.content();
        TransMessage.Message message = TransMessage.Message.parseFrom(content.nioBuffer());
        TransMessage.Head head = message.getHead();
        switch (head.getType()){
            case ROUTUSER:
                String body = message.getBody();
                System.out.println(body);
                ServerAddress serverAddress = gson.fromJson(body, ServerAddress.class);
                channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("hello".getBytes()),new InetSocketAddress(serverAddress.getIp(),serverAddress.getPort())));
                break;
            case USER:
                break;
            case SYSTEM:
                break;
            default:
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("heart beat");
    }
}