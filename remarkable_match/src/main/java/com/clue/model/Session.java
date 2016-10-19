package com.clue.model;

import io.vertx.core.http.ServerWebSocket;

public class Session {
    ServerWebSocket socket;
    String sessionId;
    String roomId;

    public Session() {
    }

    public Session(ServerWebSocket ws) {
        this.sessionId = ws.binaryHandlerID();
        this.socket = ws;
    }

    public ServerWebSocket getSocket() {
        return socket;
    }

    public void setSocket(ServerWebSocket socket) {
        this.socket = socket;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sessionId:");
        sb.append(sessionId);
        return sb.toString();
    }
}
