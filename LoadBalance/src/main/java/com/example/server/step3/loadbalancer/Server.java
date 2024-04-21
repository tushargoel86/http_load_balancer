package com.example.server.step3.loadbalancer;

public interface Server {
    boolean isAlive();
    String makeRequest();
    String getId();
    String getIp();
}
