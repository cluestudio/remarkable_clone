package com.clue.controller;

import com.clue.fbs.*;
import com.clue.model.*;
import com.google.flatbuffers.FlatBufferBuilder;

public class FlatBuilder {
    public static int newString(FlatBufferBuilder builder, String text) {
        if (text == null) {
            return 0;
        }
        return builder.createString(text);
    }

    public static int build(FlatBufferBuilder builder, Account account) {
        int uidOffset = newString(builder, account.uid);
        int nameOffset  = newString(builder, account.name);
        int profileUrlOffset = newString(builder, account.profileUrl);
        int facebookOffset = newString(builder, account.facebook);
        int googleOffset = newString(builder, account.google);
        int gameCenterOffset = newString(builder, account.gameCenter);
        int countryOffset = newString(builder, account.country);

        return RmAccount.createRmAccount(builder,
            uidOffset,
            account.token,
            nameOffset,
            profileUrlOffset,
            account.birth,
            facebookOffset,
            googleOffset,
            gameCenterOffset,
            account.level,
            account.exp,
            account.league,
            account.leaguePoint,
            countryOffset,
            account.lastPlayTime);
    }

    public static int build(FlatBufferBuilder builder, Hero hero) {
        return RmHero.createRmHero(builder,
                hero.name,
                hero.skin,
                hero.birth,
                hero.lastPlay);
    }

    public static int build(FlatBufferBuilder builder, Assets assets) {
        return RmAssets.createRmAssets(builder,
                assets.gold,
                assets.ruby);
    }
    
    public static int build(FlatBufferBuilder builder, Playlog playlog) {
        int pidOffset = newString(builder, playlog.pid);
        int player0Offset  = newString(builder, playlog.player0);
        int player1Offset = newString(builder, playlog.player1);
        int winnerOffset = newString(builder, playlog.winner);
        int name0Offset  = newString(builder, playlog.name0);
        int name1Offset  = newString(builder, playlog.name1);
        int verOffset = newString(builder, playlog.ver);
        int rawOffset = RmPlaylog.createRawVector(builder, playlog.raw);

        return RmPlaylog.createRmPlaylog(builder,
                pidOffset,
                player0Offset,
                player1Offset,
                winnerOffset,
                name0Offset,
                playlog.leaguePoint0,
                playlog.rewardPoint0,
                name1Offset,
                playlog.leaguePoint1,
                playlog.rewardPoint1,
                playlog.birth,
                playlog.playTime,
                verOffset,
                rawOffset);
    }
}
