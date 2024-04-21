package com.example.server.step2.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinBalancer implements Balancer {
    private final List<Server> servers;
    private final AtomicInteger currentServer;

    public RoundRobinBalancer(List<Server> servers) {
        this.servers = servers;
        currentServer = new AtomicInteger(0);
    }

    public RoundRobinBalancer() {
        this(new ArrayList<>());
    }

    @Override
    public void addServer(Server server) {
        synchronized (servers) {
            System.out.println("Adding server: " + server.getIp());
            servers.add(server);
        }
    }

    @Override
    public Server getNextServer() {
		if (servers.isEmpty()) throw new RuntimeException("No server available");

		int current = 0;
		do {
            current = currentServer.get();
        } while (!this.currentServer.compareAndSet(current, (current + 1) % servers.size()));

        return servers.get(current);
    }

}
