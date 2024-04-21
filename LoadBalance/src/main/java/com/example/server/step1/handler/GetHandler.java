package com.example.server.step1.handler;

import com.example.server.step1.http.Request;
import com.example.server.step1.http.Response;

public class GetHandler implements Handler{
    @Override
    public void handle(Request request, Response response) {
		StringBuffer buffer = new StringBuffer();
        buffer.append(request.getMethod() + " " + request.getPath() + " " + request.getProtocol() + "\n");
        buffer.append("Host: " + request.getHeaders("Host") + " \n");
        buffer.append("User-Agent: " + request.getHeaders("User-Agent") + "\n");
        buffer.append("Accept: " + request.getHeaders("Accept") + "\n");

        String html = "It works, \n" + buffer;
        response.setResponseCode(200, "OK");
        response.addHeader("Content-type", "text-html");
        response.addBody(html);
    }
}
