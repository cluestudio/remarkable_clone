package com.clue.controller;

import com.clue.fbs.*;
import com.clue.model.Hero;
import com.clue.model.Session;
import com.clue.service.Service;
import com.google.flatbuffers.FlatBufferBuilder;

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
                }
                else {
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
            for (int index=0; index<length; index++) {
                RmsUnitStatic item = Service.meta.getMetaSet().unitStatic(index);
                if (item.minUserLevel() == 0 && item.type() == RmUnitType.Hero) {
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
        return hero;
    }

    void sendHeroes(Session session, byte seqNo, ArrayList<Hero> heroes) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmbResHeroes.startHeroesVector(builder, heroes.size());
        for (int i = heroes.size()-1; i >= 0; i--) {
            FlatBuilder.build(builder, heroes.get(i));
        }
        int heroesOffset = builder.endVector();

        RmbResHeroes.startRmbResHeroes(builder);
        RmbResHeroes.addHeroes(builder, heroesOffset);
        builder.finish(RmbResHeroes.endRmbResHeroes(builder));
        Service.session.send(session, seqNo, RmbMessageType.ResHeroes, builder.sizedByteArray());
    }

    public void processReqBuyHero(Session session, byte seqNo, ByteBuffer buffer) {
        // It's not implemented yet.
    }
}
