package com.example.server.step2.loadbalancer;

public interface Balancer {
    Server getNextServer();
    void addServer(Server server);
}
