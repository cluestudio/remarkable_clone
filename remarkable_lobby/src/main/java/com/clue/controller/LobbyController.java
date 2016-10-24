package com.clue.controller;

import com.clue.fbs.RmbMessageType;
import com.clue.model.*;
import com.clue.service.Service;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.nio.ByteBuffer;


public class LobbyController {
    Logger logger = LoggerFactory.getLogger(getClass());
    AccountController accountController = new AccountController();
    HeroController heroController = new HeroController();
    MatchRoutingController matchRouteController = new MatchRoutingController();
    PlaylogController playlogController = new PlaylogController();

    public void bind(HttpServer server, String path) {
        server.websocketHandler(ws -> {
            if (!ws.path().equals(path)) {
                ws.reject();
            }

            Service.session.addSession(ws);
            logger.info("connected:" + ws.binaryHandlerID());
            logger.info(debugString());

            ws.handler(data -> {
                Session session = Service.session.getSession(ws);
                parse(session, data);
            });

            ws.closeHandler(var -> {
                Session session = Service.session.getSession(ws);
                Service.session.removeSession(session);
                logger.info("closed:" + ws.binaryHandlerID());
                logger.info(debugString());
            });
        });
    }

    void parse(Session session, Buffer data) {
        byte seqNo = data.getByte(0);
        byte messageType = data.getByte(1);
        if (session == null) {
            logger.error("no session!");
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(2, data.length()));
        switch (messageType) {
            case RmbMessageType.Ping: sendPing(session); break;
            case RmbMessageType.ReqLogin: accountController.processReqLogin(session, seqNo, buffer); break;
            case RmbMessageType.ReqHeroes: heroController.processReqHeroes(session, seqNo, buffer); break;
            case RmbMessageType.ReqBuyHero: heroController.processReqBuyHero(session, seqNo, buffer); break;
            case RmbMessageType.ReqRouteMatch: matchRouteController.processReqRouteMatch(session, seqNo, buffer); break;
            case RmbMessageType.ReqPlaylogList: playlogController.processReqPlaylogList(session, seqNo, buffer); break;
            case RmbMessageType.ReqSetName: accountController.processSetName(session, seqNo, buffer); break;
            case RmbMessageType.ReqUseHero: heroController.processReqUseHero(session, seqNo, buffer); break;
        }
    }

    void sendPing(Session session) {
        Service.session.send(session, (byte)0, RmbMessageType.Pong, null);
    }

    String debugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Session]\n");
        sb.append(Service.session.debugString());
        return sb.toString();
    }
}
