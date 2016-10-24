package com.clue.controller;

import com.clue.fbs.RmbMessageType;
import com.clue.fbs.RmbResError;
import com.clue.fbs.RmbResLogin;
import com.clue.fbs.RmResultCode;
import com.clue.model.AsyncResultError;
import com.clue.model.Session;
import com.clue.service.Service;
import com.clue.util.VoidHandler;
import com.google.flatbuffers.FlatBufferBuilder;
import io.vertx.core.AsyncResult;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class ControllerBase {
    Logger logger = LoggerFactory.getLogger(getClass());

    void sendError(Session session, byte seqNo, int code, String message) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int messageOffset = 0;
        if (message != null) {
            messageOffset = builder.createString(message);
        }

        RmbResError.startRmbResError(builder);
        RmbResError.addCode(builder, code);
        RmbResError.addMessage(builder, messageOffset);
        builder.finish(RmbResLogin.endRmbResLogin(builder));
        Service.session.send(session, seqNo, RmbMessageType.Error, builder.sizedByteArray());
    }

    void runAlways(Session session, byte seqNo, AsyncResult result, VoidHandler handler) {
        if (result.failed()) {
            String message = result.cause().getMessage();
            AsyncResultError errorResult = (AsyncResultError)result;
            sendError(session, seqNo, errorResult.getResultCode(), message);
            logger.error(message);
        }
        else {
            handler.handle();
        }
    }    

    void runIfExist(Session session, byte seqNo, AsyncResult result, VoidHandler handler) {
        if (result.failed()) {
            String message = result.cause().getMessage();
            AsyncResultError errorResult = (AsyncResultError)result;
            sendError(session, seqNo, errorResult.getResultCode(), message);
            logger.error(message);
        }
        else {
            if (result.result() == null) {
                String message = "nothing exist!";
                sendError(session, seqNo, RmResultCode.NotExist, message);
                logger.error(message);
                return;
            }

            handler.handle();
        }
    }

    void runIfEmpty(Session session, byte seqNo, AsyncResult result, VoidHandler handler) {
        if (result.failed()) {
            String message = result.cause().getMessage();
            AsyncResultError errorResult = (AsyncResultError)result;
            sendError(session, seqNo, errorResult.getResultCode(), message);
            logger.error(message);
        }
        else {
            if (result.result() != null) {
                String message = "already exist!";
                sendError(session, seqNo, RmResultCode.AlreadyExist, message);
                logger.error(message);
                return;
            }

            handler.handle();
        }
    }
}
