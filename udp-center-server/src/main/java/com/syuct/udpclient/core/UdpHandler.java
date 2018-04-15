package com.syuct.udpclient.core;

import com.google.gson.Gson;
import com.syuct.core_service.protoc.TransMessage;
import com.syuct.udpclient.constant.ServerAddress;
import com.syuct.udpclient.constant.UserContainer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.UUID;


public class UdpHandler extends SimpleChannelInboundHandler<DatagramPacket>{

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        InetSocketAddress sender = datagramPacket.sender();
        System.out.println("shou dao xiaoxi");
        Gson gson = new Gson();
        ByteBuf content = datagramPacket.content();
        TransMessage.Message message = TransMessage.Message.parseFrom(content.nioBuffer());
        TransMessage.Head head = message.getHead();
        switch (head.getType()){
            case HANDSHAKE:
                String body = message.getBody();
                System.out.println(body);
                System.out.println(sender.getHostName()+"\t"+sender.getPort());
                UserContainer.put(body,new ServerAddress(sender.getHostName(),sender.getPort(),body));
                break;
            case ROUTUSER:
                String userId = message.getBody();
                System.out.println("revice : rout : "+userId);
                ServerAddress serverAddress = UserContainer.get(userId);
                TransMessage.Head.Builder head2 = TransMessage.Head.newBuilder();
                head2.setStatus(TransMessage.status.OK);
                head2.setType(TransMessage.type.ROUTUSER);
                head2.setUid(UUID.randomUUID().toString());
                head2.setTime(System.currentTimeMillis());
                TransMessage.Message.Builder message2 = TransMessage.Message.newBuilder();
                message2.setHead(head2);
                message2.setBody(gson.toJson(serverAddress));
                channelHandlerContext.channel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(message2.build().toByteArray()),sender));
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
        super.userEventTriggered(ctx, evt);
    }
}