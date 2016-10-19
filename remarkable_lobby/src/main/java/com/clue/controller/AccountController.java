package com.clue.controller;

import com.clue.fbs.*;
import com.clue.model.*;
import com.clue.service.Service;
import com.google.flatbuffers.FlatBufferBuilder;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.nio.ByteBuffer;

public class AccountController extends ControllerBase {
    Logger logger = LoggerFactory.getLogger(getClass());

    public void processReqLogin(Session session, byte seqNo, ByteBuffer buffer) {
        RmbReqLogin reqLogin = RmbReqLogin.getRootAsRmbReqLogin(buffer);
        String uid = reqLogin.uid();
        if (uid == null || uid.length() == 0) {
            uid = Service.key.newDataDBKey();
            String countryCode = reqLogin.country();
            newAccount(session, seqNo, uid, countryCode, reqLogin.metaVersion());
            return;
        }

        Service.account.getAccount(reqLogin.uid(), res -> {
            runIfExist(session, seqNo, res, () -> {
                Account account = res.result();
                session.setAccount(account);
                getAssets(session, seqNo, account, reqLogin.metaVersion());
            });
        });
    }

    void newAccount(Session session, byte seqNo, String uid, String countryCode, int metaVersion) {
        if (countryCode == null || countryCode.isEmpty()) {
            countryCode = Service.geoIP.getCountryCode(session.getSocket().remoteAddress().host());
        }

        Service.account.newAccount(uid, countryCode, res -> {
            runIfExist(session, seqNo, res, () -> {
                session.setAccount(res.result());
                newAssets(session, seqNo, res.result(), metaVersion);
            });
        });
    }

    void getAssets(Session session, byte seqNo, Account account, int metaVersion) {
        Service.account.getAssets(account.uid, res -> {
            runAlways(session, seqNo, res, () -> {
                Assets assets = res.result();
                if (assets == null) {
                    newAssets(session, seqNo, account, metaVersion);
                    return;
                }

                sendResLogin(session, seqNo, account, assets, metaVersion);
            });
        });
    }

    void newAssets(Session session, byte seqNo, Account account, int metaVersion) {
        Assets assets = new Assets();
        assets.uid = account.uid;
        assets.gold = 1000; //#todo
        assets.ruby = 10; //#todo

        Service.account.newAssets(assets, res -> {
            runIfExist(session, seqNo, res, () -> {
                sendResLogin(session, seqNo, account, res.result(), metaVersion);
            });
        });
    }

    void sendResLogin(Session session, byte seqNo, Account account, Assets assets, int metaVersion) {
        RmsMetaSet metaSet = Service.meta.getMetaSet();

        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int accountOffset = FlatBuilder.build(builder, account);
        int metaSetOffset = RmbResLogin.createMetaSetVector(builder, Service.meta.getMetaSetBytes());

        RmbResLogin.startRmbResLogin(builder);
        RmbResLogin.addAccount(builder, accountOffset);
        RmbResLogin.addAssets(builder, FlatBuilder.build(builder, assets));
        RmbResLogin.addMetaVersion(builder, Service.meta.getVersion());
        RmbResLogin.addMetaSet(builder, metaSetOffset);

        builder.finish(RmbResLogin.endRmbResLogin(builder));
        Service.session.send(session, seqNo, RmbMessageType.ResLogin, builder.sizedByteArray());
    }

    public void processSetName(Session session, byte seqNo, ByteBuffer buffer) {
        RmbReqSetName req = RmbReqSetName.getRootAsRmbReqSetName(buffer);
        Account account = session.getAccount();
        if (account == null) {
            sendError(session, seqNo, RmResultCode.NotExist, "no account exist");
            return;
        }

        account.name = req.name();
        logger.error(account.name);
        Service.account.updateAccount(account, res -> {
            runIfExist(session, seqNo, res, () -> {
                sendResName(session, seqNo, req.name());
            });
        });
    }

    void sendResName(Session session, byte seqNo, String name) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int nameOffset = builder.createString(name);

        RmbResSetName.startRmbResSetName(builder);
        RmbResSetName.addName(builder, nameOffset);
        builder.finish(RmbResSetName.endRmbResSetName(builder));
        Service.session.send(session, seqNo, RmbMessageType.ResSetName, builder.sizedByteArray());
    }
}
