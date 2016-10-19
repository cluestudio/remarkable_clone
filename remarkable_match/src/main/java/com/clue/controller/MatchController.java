package com.clue.controller;

import com.clue.model.*;
import com.clue.fbs.*;
import com.clue.service.Service;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.nio.ByteBuffer;

public class MatchController {
    BattlePlayController battlePlay = null;
    Logger logger = LoggerFactory.getLogger(getClass());


    public void bind(HttpServer server, String path) {
        battlePlay = new BattlePlayController();

        server.websocketHandler(ws -> {
            if (!ws.path().equals(path)) {
                ws.reject();
            }

            Service.session.addSession(ws);
            logger.info("connected:" + ws.binaryHandlerID());
            logger.info(Service.room.debugString());
            logger.info(Service.session.debugString());

            ws.handler(data -> {
                Session session = Service.session.getSession(ws);
                parse(session, data);
            });

            ws.closeHandler(var -> {
                Session session = Service.session.getSession(ws);
                Room room = Service.room.getRoom(session.getRoomId());
                if (room != null) {
                    Player player = room.getPlayer(session);
                    Service.room.leavePlayer(room, player);
                }

                Service.session.removeSession(session);
                logger.info("closed:" + ws.binaryHandlerID());
                logger.info(Service.room.debugString());
                logger.info(Service.session.debugString());
                battlePlay.sendLeaveNoti(session);
            });
        });
    }

    void parse(Session session, Buffer data) {
        byte messageType = data.getByte(0);
        if (session == null) {
            logger.error("no session!");
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(1, data.length()));
        switch (messageType) {
            case RmmMessageType.ReqJoin: battlePlay.processReqJoin(session, buffer); break;
            case RmmMessageType.ReqReJoin: battlePlay.processReqReJoin(session, buffer); break;
            case RmmMessageType.ReqSync: battlePlay.processReqSync(session, buffer); break;
        }
    }
}
