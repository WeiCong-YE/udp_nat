package com.syuct.udpclient;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Test {
    public static void main(String[] args) {
        InetSocketAddress recipient=new InetSocketAddress("45.32.10.203", 9000);
        //InetSocketAddress recipient=new InetSocketAddress("127.0.0.1", 9000);
        DatagramPacket pack=new DatagramPacket(("1111111111111".getBytes()), "1111111111111".getBytes().length);
        try {
            DatagramSocket datagramSocket =new DatagramSocket();
            datagramSocket.connect(recipient);
            datagramSocket.send(pack);
            byte[] b = new byte[1000];
            while (true){
                datagramSocket.receive(new DatagramPacket(b,1000));
                System.out.println(new String(b));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
