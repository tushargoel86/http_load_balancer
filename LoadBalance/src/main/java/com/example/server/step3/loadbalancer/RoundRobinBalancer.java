package com.example.server.step3.loadbalancer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RoundRobinBalancer implements Balancer {
    private final List<Server> servers;
    private final AtomicBoolean isHealthCheckupRunning;
    private final ScheduledExecutorService scheduledExecutorService;
    private final AtomicInteger currentServer;
    private final Set<String> healthServerIds;

    public RoundRobinBalancer(List<Server> servers, long healthCheckupInterval, TimeUnit unit) {
        this.servers = servers;
        isHealthCheckupRunning = new AtomicBoolean(false);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        currentServer = new AtomicInteger(0);
        healthServerIds = Collections.synchronizedSet(new HashSet<>());
        scheduleHealthCheckup(healthCheckupInterval, unit);
        scheduleRecheckServers(healthCheckupInterval, unit);
    }

    private void scheduleRecheckServers(long healthCheckupInterval, TimeUnit unit) {
        scheduledExecutorService.scheduleAtFixedRate(this::recheckServer, 0, healthCheckupInterval, unit);
    }

    private void recheckServer() {
        Set<String> serverIds = servers.parallelStream().map(Server::getId).collect(Collectors.toSet());
		Set<String> removedIds = healthServerIds.parallelStream().filter(id -> !serverIds.contains(id)).collect(Collectors.toSet());
        System.out.println("Following servers are considered not responding: " + removedIds);
        healthServerIds.removeAll(removedIds);
		performHealthCheckup();
    }

    public RoundRobinBalancer(long healthCheckupInterval, TimeUnit unit) {
        this(new ArrayList<>(), healthCheckupInterval, unit);
    }

    private void scheduleHealthCheckup(long healthCheckupInterval, TimeUnit unit) {
        if (isHealthCheckupRunning.compareAndSet(false, true)) {
            System.out.println("scheduling health check");
            scheduledExecutorService.scheduleAtFixedRate(this::performHealthCheckup, 0, healthCheckupInterval, unit);
        }
    }

    private void performHealthCheckup() {
        System.out.println("performing health checkup...");
        List<Server> healthyServers = servers.parallelStream()
                .filter(server -> {
                    boolean isAlive = server.isAlive();
                    if (isAlive && !healthServerIds.contains(server.getId()))
                        healthServerIds.add(server.getId());
;                   return isAlive;
                })
                .collect(Collectors.toList());

        synchronized (servers) {
            System.out.println("Following servers are found healthy: " + healthyServers);
            this.servers.retainAll(healthyServers);
        }
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
