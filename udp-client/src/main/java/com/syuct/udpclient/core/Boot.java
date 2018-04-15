package com.syuct.udpclient.core;

import com.google.gson.Gson;
import com.syuct.core_service.protoc.TransMessage;
import com.syuct.udpclient.constant.ServerAddress;
import com.syuct.udpclient.constant.ServerConfig;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Created by misnearzhang on 5/16/17.
 */
@Component
public class Boot implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Gson gson = new Gson();
        try {

            UdpClient client = new UdpClient();
            client.run(10010);
            TransMessage.Head.Builder head2 = TransMessage.Head.newBuilder();
            head2.setStatus(TransMessage.status.REQ);
            head2.setType(TransMessage.type.ROUTUSER);
            head2.setUid(UUID.randomUUID().toString());
            head2.setTime(System.currentTimeMillis());
            TransMessage.Message.Builder message2 = TransMessage.Message.newBuilder();
            message2.setHead(head2);
            message2.setBody("1039075891");
            client.channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(message2.build().toByteArray()),new InetSocketAddress(ServerConfig.Server,ServerConfig.port)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
