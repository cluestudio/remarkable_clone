package com.clue.service;

import com.clue.model.Account;
import com.clue.model.Session;
import io.vertx.core.http.ServerWebSocket;

public interface SessionService extends ServiceBase {
    void addSession(Session ws);
    void addSession(ServerWebSocket ws);
    void removeSession(Session session);
    Session getSession(String key);
    Session getSession(ServerWebSocket ws);
    void send(Session session, byte seqNo, byte messageType, byte[] msg);
    String debugString();
}
