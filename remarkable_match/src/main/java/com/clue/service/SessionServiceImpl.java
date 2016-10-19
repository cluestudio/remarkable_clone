package com.clue.service;

import com.clue.model.Session;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;

public class SessionServiceImpl implements SessionService {
    private HashMap<String, Session> sessions = new HashMap();
    private Logger logger = LoggerFactory.getLogger(getClass());

    public boolean isReady() {
        return true;
    }

    public void initialize(Vertx vertx) {
    }

    public void addSession(ServerWebSocket ws) {
        sessions.put(ws.binaryHandlerID(), new Session(ws));
    }

    public void addSession(Session player) {
        sessions.put(player.getSessionId(), player);
    }

    public void removeSession(Session player) {
        sessions.remove(player.getSessionId());
    }

    public Session getSession(String key) {
        return sessions.get(key);
    }

    public Session getSession(ServerWebSocket ws) {
        return getSession(ws.binaryHandlerID());
    }

    public void send(String sessionId, byte messageType, byte[] msg) {
        Session session = getSession(sessionId);
        if (session == null) {
            logger.error("can not send to null session");
            return;
        }

        send(session, messageType, msg);
    }

    public void send(Session session, byte messageType, byte[] msg) {
        if (session.getSocket() == null) {
            return;
        }

        Buffer data = Buffer.buffer();
        data.appendByte(messageType);
        if (msg != null) {
            data.appendBytes(msg);
        }

        session.getSocket().writeFinalBinaryFrame(data);
    }

    public String debugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("all session count:");
        sb.append(sessions.size());
        sb.append("\n");

        for (Session session : sessions.values()) {
            sb.append(session.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
