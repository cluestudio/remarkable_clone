package com.clue.controller;

import com.clue.model.*;
import com.clue.fbs.*;
import com.google.flatbuffers.FlatBufferBuilder;

public class FlatBuilder {
    public static int newString(FlatBufferBuilder builder, String text) {
        if (text == null) {
            return 0;
        }
        return builder.createString(text);
    }

    public static int buildAbnormal(FlatBufferBuilder builder, Abnormal abnormal) {
        return RmmAbnormal.createRmmAbnormal(
            builder,
            abnormal.getType(),
            abnormal.getFrom(),
            abnormal.getTo(),
            abnormal.getAt(),
            abnormal.getDuration(),
            abnormal.getValue());
    }

    public static int buildNeVec2(FlatBufferBuilder builder, Vector2 vec) {
        return RmmVector2.createRmmVector2(builder, vec.getX(), vec.getZ());
    }

    public static int buildNeVecMini2(FlatBufferBuilder builder, Vector2b vec) {
        return RmmVector2b.createRmmVector2b(builder, vec.getX(), vec.getZ());
    }

    public static int buildUnit(FlatBufferBuilder builder, Unit unit, float playTime) {
        int abnormalListOffset = 0;
        if (unit.getAbnormalList().size() > 0) {
            RmmUnit.startAbnormalListVector(builder, unit.getAbnormalList().size());
            for (Abnormal abnormal : unit.getAbnormalList()) {
                FlatBuilder.buildAbnormal(builder, abnormal);
            }
            abnormalListOffset = builder.endVector();
        }

        RmmUnit.startRmmUnit(builder);
        RmmUnit.addKey(builder, unit.getKey());
        RmmUnit.addName(builder, unit.getName());
        RmmUnit.addType(builder, unit.getType());
        RmmUnit.addLevel(builder, unit.getLevel());
        RmmUnit.addExp(builder, unit.getExp());
        RmmUnit.addHp(builder, unit.getHp());
        RmmUnit.addPosition(builder, buildNeVec2(builder, unit.getPosition()));
        RmmUnit.addDirection(builder, buildNeVecMini2(builder, unit.getDirection()));
        RmmUnit.addSkill1FireSec(builder, unit.getSkill1FireSec());
        RmmUnit.addSkill2FireSec(builder, unit.getSkill2FireSec());
        RmmUnit.addSkill3FireSec(builder, unit.getSkill3FireSec());
        RmmUnit.addSkillSpecialFireSec(builder, unit.getSkillSpecialFireSec());
        RmmUnit.addAbnormalList(builder, abnormalListOffset);
        return RmmUnit.endRmmUnit(builder);
    }

    public static int buildPlayer(FlatBufferBuilder builder, Player player, float playTime) {
        int idOffset = builder.createString(player.getUid());
        int nameOffset = builder.createString(player.getName());
        int profileUrlOffset = builder.createString(player.getCountry());
        int unitsOffset = 0;

        if (player.getUnitCount() > 0) {
            int []units = new int[player.getUnitCount()];
            int index = 0;
            for (Unit unit : player.getUnits().values()) {
                units[index++] = buildUnit(builder, unit, playTime);
            }
            unitsOffset = RmmPlayer.createUnitsVector(builder, units);
        }

        RmmPlayer.startRmmPlayer(builder);
        RmmPlayer.addLeaguePoint(builder, player.getLeaguePoint());
        RmmPlayer.addCountry(builder, profileUrlOffset);
        RmmPlayer.addName(builder, nameOffset);
        RmmPlayer.addUid(builder, idOffset);
        RmmPlayer.addPlayerNo(builder, player.getPlayerNo());
        RmmPlayer.addLevel(builder, player.getLevel());
        RmmPlayer.addHero(builder, player.getHero());
        RmmPlayer.addHeroDeadTime(builder, player.getHeroDeadTime());
        RmmPlayer.addUnits(builder, unitsOffset);
        RmmPlayer.addKillHeroCount(builder, player.getKillHeroCount());
        RmmPlayer.addKillPetCount(builder, player.getKillPetCount());
        RmmPlayer.addDeadCount(builder, player.getDeadCount());
        RmmPlayer.addDealToHero(builder, player.getDealToHero());
        RmmPlayer.addDealToPet(builder, player.getDealToPet());
        RmmPlayer.addDamageFromHero(builder, player.getDamageFromHero());
        RmmPlayer.addDamageFromPet(builder, player.getDamageFromPet());
        RmmPlayer.addAi(builder, player.isAi());
        return RmmPlayer.endRmmPlayer(builder);
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
