package com.clue.service;

import com.clue.fbs.RmbMessageType;
import com.clue.model.Account;
import com.clue.model.Session;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;

public class SessionServiceImpl implements SessionService {
    private HashMap<String, Session> sessions = new HashMap<>();
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
        sessions.put(player.getKey(), player);
    }

    public void removeSession(Session player) {
        sessions.remove(player.getKey());
    }

    public Session getSession(String key) {
        return sessions.get(key);
    }

    public Session getSession(ServerWebSocket ws) {
        return getSession(ws.binaryHandlerID());
    }

    public void send(Session session, byte seqNo, byte messageType, byte[] msg) {
        if (session.getSocket() == null) {
            return;
        }

        Buffer data = Buffer.buffer();
        data.appendByte(seqNo);
        data.appendByte(messageType);
        if (msg != null) {
            data.appendBytes(msg);
        }

        session.getSocket().writeFinalBinaryFrame(data);
        if (messageType != RmbMessageType.Pong) {
            String key = "";
            Account account = session.getAccount();
            if (account != null) {
                key = account.uid;
            }

            logger.info("send " + messageType + " message to " + key);
        }
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
