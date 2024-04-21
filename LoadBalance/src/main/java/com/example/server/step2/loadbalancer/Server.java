package com.example.server.step2.loadbalancer;

public interface Server {
    boolean isAlive();
    String makeRequest();
    String getIp();
}
