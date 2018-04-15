package com.syuct.udpclient.constant;

import java.util.concurrent.ConcurrentHashMap;

public class UserContainer {
    private static final ConcurrentHashMap<String , ServerAddress> container = new ConcurrentHashMap<>();

    public static void put(String key , ServerAddress serverAddress){
        container.put(key,serverAddress);
    }

    public static ServerAddress get(String key){
        return container.get(key);
    }
}
