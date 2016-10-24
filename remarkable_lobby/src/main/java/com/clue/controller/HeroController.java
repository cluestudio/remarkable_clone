package com.clue.controller;

import com.clue.fbs.*;
import com.clue.model.Account;
import com.clue.model.Assets;
import com.clue.model.Hero;
import com.clue.model.Session;
import com.clue.service.Service;
import com.google.flatbuffers.FlatBufferBuilder;
import io.vertx.core.AsyncResult;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class HeroController extends ControllerBase {
    Random random = new Random();
    ArrayList<RmsUnitStatic> initHeroCandidates = null;

    public void processReqHeroes(Session session, byte seqNo, ByteBuffer buffer) {
        Service.hero.getHeroes(session.getAccount().uid, res -> {
            runAlways(session, seqNo, res, () -> {
                ArrayList<Hero> heroes = res.result();
                if (heroes.size() == 0) {
                    newHero(session, seqNo);
                } else {
                    sendHeroes(session, seqNo, heroes);
                }
            });
        });
    }

    void newHero(Session session, byte seqNo) {
        Hero hero = makeBeginnersHero();
        ArrayList<Hero> heroes = new ArrayList<>();
        heroes.add(hero);

        Service.hero.newHero(session.getAccount().uid, hero, res -> {
            runIfExist(session, seqNo, res, () -> {
                sendHeroes(session, seqNo, heroes);
            });
        });
    }

    Hero makeBeginnersHero() {
        if (initHeroCandidates == null) {
            int length = Service.meta.getMetaSet().unitStaticLength();
            initHeroCandidates = new ArrayList<>();
            for (int index = 0; index < length; index++) {
                RmsUnitStatic item = Service.meta.getMetaSet().unitStatic(index);
                if (item.minUserLevel() == 1 && item.type() == RmUnitType.Hero) {
                    initHeroCandidates.add(item);
                }
            }
        }

        if (initHeroCandidates.size() == 0) {
            return null;
        }

        RmsUnitStatic unit = initHeroCandidates.get(random.nextInt(initHeroCandidates.size()));
        return generateHero(unit.name());
    }

    Hero generateHero(short unitName) {
        long birth = System.currentTimeMillis() / 1000L;
        Hero hero = new Hero();
        hero.name = unitName;
        hero.skin = 0;
        hero.birth = birth;
        hero.lastPlay = birth;
        return hero;
    }

    void sendHeroes(Session session, byte seqNo, ArrayList<Hero> heroes) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmbResHeroes.startHeroesVector(builder, heroes.size());
        for (int i = heroes.size() - 1; i >= 0; i--) {
            FlatBuilder.build(builder, heroes.get(i));
        }
        int heroesOffset = builder.endVector();

        RmbResHeroes.startRmbResHeroes(builder);
        RmbResHeroes.addHeroes(builder, heroesOffset);
        builder.finish(RmbResHeroes.endRmbResHeroes(builder));
        Service.session.send(session, seqNo, RmbMessageType.ResHeroes, builder.sizedByteArray());
    }

    public void processReqBuyHero(Session session, byte seqNo, ByteBuffer buffer) {
        RmbReqBuyHero req = RmbReqBuyHero.getRootAsRmbReqBuyHero(buffer);

        Service.account.getAccount(session.getAccount().uid, res -> {
            runIfExist(session, seqNo, res, () -> {
                Account account = res.result();
                session.setAccount(account);

                // check min level
                RmsUnitStatic info = Service.meta.getUnitStatic(req.name());
                if (account.level < info.minUserLevel()) {
                    sendError(session, seqNo, RmResultCode.InsufficientLevel, "need " + info.minUserLevel() + " level to buy");
                    return;
                }

                payNewHero(session, seqNo, req, account, info);
            });
        });
    }

    private void payNewHero(Session session, byte seqNo, RmbReqBuyHero req, Account account, RmsUnitStatic info) {
        // check sufficient gold
        Service.account.getAssets(account.uid, assetRes-> {
            runIfExist(session, seqNo, assetRes, () -> {
                Assets assets = assetRes.result();
                if (assets.gold < info.price()) {
                    sendError(session, seqNo, RmResultCode.NotEnoughGold, "need " + info.price() + " gold to buy");
                    return;
                }

                // reduce gold
                assets.gold -= info.price();
                Service.account.updateAsset(account.uid, assets, payRes -> {
                    runAlways(session, seqNo, assetRes, () -> {
                        addNewHero(session, seqNo, req, assets);
                    });
                });
            });
        });
    }

    private void addNewHero(Session session, byte seqNo, RmbReqBuyHero req, Assets assets) {
        Hero hero = generateHero(req.name());
        Service.hero.newHero(session.getAccount().uid, hero, newHeroRes -> {
            runAlways(session, seqNo, newHeroRes, () -> {
                sendBuyHero(session, seqNo, hero, assets);
            });
        });
    }

    void sendBuyHero(Session session, byte seqNo, Hero hero, Assets assets) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        RmbResBuyHero.startRmbResBuyHero(builder);
        int assetsOffset = FlatBuilder.build(builder, assets);
        RmbResBuyHero.addAssets(builder, assetsOffset);
        int heroOffset = FlatBuilder.build(builder, hero);
        RmbResBuyHero.addHero(builder, heroOffset);
        builder.finish(RmbResBuyHero.endRmbResBuyHero(builder));
        Service.session.send(session, seqNo, RmbMessageType.ResBuyHero, builder.sizedByteArray());
    }

    public void processReqUseHero(Session session, byte seqNo, ByteBuffer buffer) {
        RmbReqUseHero req = RmbReqUseHero.getRootAsRmbReqUseHero(buffer);
        long timestamp = System.currentTimeMillis() / 1000L;

        Service.hero.useHero(session.getAccount().uid, req.name(), timestamp, res -> {
            runAlways(session, seqNo, res, () -> {
                sendUseHero(session, seqNo, req.name(), timestamp);
            });
        });
    }

    void sendUseHero(Session session, byte seqNo, short name, long lastPlay) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        builder.finish(RmbResUseHero.createRmbResUseHero(builder, name, lastPlay));
        Service.session.send(session, seqNo, RmbMessageType.ResUseHero, builder.sizedByteArray());
    }
}