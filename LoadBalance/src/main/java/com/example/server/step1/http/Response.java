package com.example.server.step1.http;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Response {
    public static final String CRLF = "\r\n";
    private OutputStream out;
    private int statusCode;
    private String statusMessage;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public Response(OutputStream out) 	{
        this.out = out;
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public void setResponseCode(int code, String statusMessage) {
        this.statusCode = code;
        this.statusMessage = statusMessage;
    }

    public void addBody(String body) {
        this.headers.put("Content-Length", String.valueOf(body.length()));
        this.body = body;
    }

    public void send() throws IOException {
        headers.put("Connection", "Close");
        out.write(("HTTP/1.1 " + statusCode + " " + statusMessage + CRLF).getBytes(StandardCharsets.UTF_8));
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            out.write((entry.getKey() + ": " + entry.getValue() + CRLF).getBytes(StandardCharsets.UTF_8));
        }
        out.write(CRLF.getBytes(StandardCharsets.UTF_8));
        if (body != null) out.write(body.getBytes(StandardCharsets.UTF_8));
    }

}