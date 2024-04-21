package com.example.server.step1.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String url;
    private String path;
    private String protocol;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();
    private final BufferedReader in;

    public Request(BufferedReader in) {
        this.in = in;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getParam(String key) {
        return queryParams.get(key);
    }

    public String getHeaders(String key) {
        return headers.get(key);
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean parse() throws IOException {
        String initialLine = in.readLine();
        if (initialLine.isEmpty()) return false;

        String []tokens = initialLine.split("\\s");
        if (tokens.length != 3) { return false; }

        method = tokens[0];
        url = tokens[1];
        protocol = tokens[2];

        int index = tokens[1].indexOf("?");
        if (index == -1) {
            path = tokens[1];
        } else {
            path = tokens[1].substring(0, index);
            parseQueryParams(tokens, index);
        }

        if (!processHeaders()) return false;
        System.out.println(headers);
        //if ("/".equals(path)) path = "/index.html";
        return true;
    }

    private boolean processHeaders() throws IOException {
        while (true) {
            String line = in.readLine();
            if (line.isEmpty()) break;
            int index = line.indexOf(":");
            if (index == -1) return false;
            headers.put(line.substring(0, index), line.substring(index + 1));
        }
        return true;
    }

    private void parseQueryParams(String[] tokens, int index) {
        String queries = tokens[1].substring(index + 1);
        for(String query : queries.split("&")) {
            String []indvQuery = query.split("=");
            if (indvQuery.length > 0) queryParams.put(indvQuery[0], indvQuery[1]);
            else queryParams.put(query, null);
        }
    }

    @Override
    public String toString() {
        return method + " " + path + " " + queryParams;
    }
}
