package com.example.server.step1.handler;

import com.example.server.step1.http.Request;
import com.example.server.step1.http.Response;

public interface Handler {
    void handle(Request request, Response response);
}
