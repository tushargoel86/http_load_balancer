package com.example.server;

import com.example.server.step2.http.HttpServer;

import java.io.IOException;

public class HttpMockServer {
    public static void main(String[] args) throws IOException {
//        HttpServer server = new HttpServer(8080);
//        server.addHandler("GET", "/", new GetHandler());
//        server.start();

        HttpServer server = new HttpServer(9090);
        server.addServer("https://api.restful-api.dev/objects/7");
        server.addServer("https://api.restful-api.dev/objects");

        server.start();
    }
}
