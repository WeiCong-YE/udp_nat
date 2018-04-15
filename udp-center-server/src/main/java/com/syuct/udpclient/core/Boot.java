package com.syuct.udpclient.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by misnearzhang on 5/16/17.
 */
@Component
public class Boot implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        try {
            new UdpServer().run(9877);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
