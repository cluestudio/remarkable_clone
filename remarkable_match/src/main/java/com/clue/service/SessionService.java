package com.clue.service;

import com.clue.model.Session;
import io.vertx.core.http.ServerWebSocket;

public interface SessionService extends ServiceBase {
    void addSession(Session ws);
    void addSession(ServerWebSocket ws);
    void removeSession(Session session);
    Session getSession(String key);
    Session getSession(ServerWebSocket ws);
    void send(String sessionId, byte messageType, byte[] msg);
    void send(Session session, byte messageType, byte[] msg);
    String debugString();
}
