package com.example.server.step3.loadbalancer;

import com.example.server.step3.http.APICallHandler;

import java.util.UUID;

public class ServerImpl implements Server {
    private final String ip;
    private final APICallHandler callHandler;
    private final String id;

    public ServerImpl(String ip) {
        this.ip = ip;
        this.callHandler = new APICallHandler();
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean isAlive() {
        return callHandler.getStatus(ip) == 200;
    }

    @Override
    public String makeRequest() {
        System.out.println("Making a request on server: " + ip);
		return this.callHandler.makeGetCall(ip).body();
    }

    @Override
    public String getId() {
		return id;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "Request received on server=" + ip;
    }
}
