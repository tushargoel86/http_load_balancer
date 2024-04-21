package com.example.server.step1.http;

import com.example.server.step1.handler.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final int port;
    private ExecutorService service = Executors.newFixedThreadPool(10);
    private Map<String, Map<String, Handler>> methodHandlers = new HashMap<>();

    public HttpServer(int port) {
        this.port = port;
    }

    public void addHandler(String method, String path, Handler handler) {
        methodHandlers.putIfAbsent(method, new HashMap<>());
        methodHandlers.get(method).put(path, handler);
    }

    public void start() throws IOException {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Listening to the port: " + port);
            Socket client;
            while ((client = socket.accept()) != null) {
                SocketHandler handler = new SocketHandler(client, methodHandlers);
                service.execute(handler);
            }
        }
    }
}
