package com.example.server.step1.http;


import com.example.server.step1.handler.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SocketHandler implements Runnable {

    private final Socket socket;
    private final Map<String, Map<String, Handler>> methodHandlers;

    public SocketHandler(Socket socket, Map<String, Map<String, Handler>> methodHandlers) {
        this.socket = socket;
        this.methodHandlers = methodHandlers;
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
            // check for handler for a given method
            Map<String, Handler> methodHandler = methodHandlers.get(request.getMethod());
            if (methodHandler == null) {
                respond(405, "Method not supported ", out);
                return;
            }

            boolean isHandlerFound = false;

            // check for handler for a given path
            for(Map.Entry<String, Handler> pathHandler : methodHandler.entrySet()) {
                if (pathHandler.getKey().equals(request.getPath())) {
                    pathHandler.getValue().handle(request, response);
                    response.send();
                    isHandlerFound = true;
                    break;
                }
            }
            // check for default handler
            if (!isHandlerFound) {
                if (methodHandler.get("/*") != null) {
                    methodHandler.get("/*").handle(request, response);
                    response.send();
                } else {
                    respond(404,"Not Found ", out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}