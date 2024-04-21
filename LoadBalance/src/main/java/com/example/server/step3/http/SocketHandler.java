package com.example.server.step3.http;


import com.example.server.step1.http.Request;
import com.example.server.step1.http.Response;
import com.example.server.step3.loadbalancer.Balancer;
import com.example.server.step3.loadbalancer.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketHandler implements Runnable {

    private final Socket socket;
    private final Balancer balancer;
    private final APICallHandler apiCallHandler;

    public SocketHandler(Socket socket, Balancer balancer) {
        this.socket = socket;
        this.balancer = balancer;
        this.apiCallHandler = new APICallHandler();
    }

    private void respond(int code, String message, OutputStream out) throws IOException {
        String response = "HTTP /1.1 " + code + " " +  message + " \r\n";
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream()) {

            // parse request
            Request request = new Request(in);
            Response response = new Response(out);

            // send error if an error
            if (!request.parse()) {
                respond(500 ,"Unable to parse request ", out);
                return;
            }

            Server server = balancer.getNextServer();
            System.out.println("Retrieve server: " + server);
			String serverResponse = server.makeRequest();

            response.addBody(serverResponse);
            response.send();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}