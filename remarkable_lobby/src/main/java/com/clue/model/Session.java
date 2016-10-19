package com.clue.model;

import io.vertx.core.http.ServerWebSocket;

public class Session {
    ServerWebSocket socket;
    String key;
    Account account;

    public Session() {
    }

    public Session(ServerWebSocket ws) {
        this.key = ws.binaryHandlerID();
        this.socket = ws;
    }

    public ServerWebSocket getSocket() {
        return socket;
    }

    public void setSocket(ServerWebSocket socket) {
        this.socket = socket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("key:");
        sb.append(key);
        if (account != null) {
            sb.append(", id:");
            sb.append(account.uid);
            sb.append(", name:");
            sb.append(account.name);
        }
        return sb.toString();
    }

}
