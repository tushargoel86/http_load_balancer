package com.example.server.step2.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//"https://api.restful-api.dev/objects/7"
public final class APICallHandler {
       public HttpResponse<String> makeGetCall(String ip) {
           HttpResponse<String> response = makeRequest(ip);
           assert response != null;
           return response;
        }

    private static HttpResponse<String> makeRequest(String ip) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ip))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return response;
    }

    public int getStatus(String ip) {
        HttpResponse<String> response = makeRequest(ip);
        assert response != null;
        return response.statusCode();
   }
}
