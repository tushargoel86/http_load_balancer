package com.example.server.step2.loadbalancer;

import com.example.server.step2.http.APICallHandler;

public class ServerImpl implements Server {
    private final String ip;
    private final APICallHandler callHandler;

    public ServerImpl(String ip) {
        this.ip = ip;
        callHandler = new APICallHandler();
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
    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "Request received on server=" + ip;
    }
}
