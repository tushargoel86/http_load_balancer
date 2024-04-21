package com.example.server.step3.loadbalancer;

public interface Balancer {
    Server getNextServer();
    void addServer(Server server);
}
