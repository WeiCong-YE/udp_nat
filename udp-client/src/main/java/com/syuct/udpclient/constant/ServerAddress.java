package com.syuct.udpclient.constant;

import java.io.Serializable;

public class ServerAddress implements Serializable {
    private String ip;
    private Integer port;
    private String userId;


    public ServerAddress(String ip, Integer port, String userId) {
        this.ip = ip;
        this.port = port;
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
