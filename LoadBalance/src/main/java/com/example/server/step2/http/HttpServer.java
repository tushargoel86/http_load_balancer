package com.example.server.step2.http;

import com.example.server.step2.loadbalancer.Balancer;
import com.example.server.step2.loadbalancer.RoundRobinBalancer;
import com.example.server.step2.loadbalancer.ServerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpServer {

    private final int port;
    private final ExecutorService service = Executors.newFixedThreadPool(10);
    private final Balancer balancer;

    public HttpServer(int port) {
        this.port = port;
        balancer = new RoundRobinBalancer(10000L, TimeUnit.MILLISECONDS);
    }

    public void addServer(String ip) {
        balancer.addServer(new ServerImpl(ip));
    }

    public void start() throws IOException {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Listening to the port: " + port);
            Socket client;
            while ((client = socket.accept()) != null) {
                SocketHandler handler = new SocketHandler(client, balancer);
                service.execute(handler);
            }
        }
    }
}
