package com.clue.controller;

import com.clue.fbs.*;
import com.clue.model.*;
import com.clue.service.Service;
import com.clue.util.Version;
import com.google.flatbuffers.FlatBufferBuilder;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class BattlePlayController {
    Logger logger = LoggerFactory.getLogger(getClass());
    BattleRewardController rewardController = new BattleRewardController();
    DamageCalculator damageCalculator = new DamageCalculator();

    ArrayList<Vector2> minonSpawnPts = new ArrayList<>();
    Vector2 startingPosition0 = new Vector2(0, -900);
    Vector2 startingPosition1 = new Vector2(0, 900);
    Vector2 towerPosition0 = new Vector2(0, -1400);
    Vector2 towerPosition1 = new Vector2(0, 1400);
    Vector2b startingDirection0 = new Vector2b(0, 100);
    Vector2b startingDirection1 = new Vector2b(0, -100);

    public BattlePlayController() {
        minonSpawnPts.add(new Vector2(100, 0));
        minonSpawnPts.add(new Vector2(-100, 0));
        minonSpawnPts.add(new Vector2(300, 0));
        minonSpawnPts.add(new Vector2(-300, 0));
    }

    public void sendLeaveNoti(Session session) {
        Room room = Service.room.getRoom(session.getRoomId());
        if (room == null) {
            return;
        }

        Player player = room.getPlayer(session);
        if (player == null) {
            return;
        }

        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmNotiLeave.startRmmNotiLeave(builder);
        RmmNotiLeave.addPlayerNo(builder, player.getPlayerNo());
        builder.finish(RmmNotiLeave.endRmmNotiLeave(builder));
        Service.room.broadcast(room, RmmMessageType.NotiLeave, builder.sizedByteArray());
    }

    public void processReqJoin(Session session, ByteBuffer buffer) {
        RmmReqJoin reqJoin = RmmReqJoin.getRootAsRmmReqJoin(buffer);
        long currentMilliSec = System.currentTimeMillis();
        long currentSec = currentMilliSec / 1000;

        Service.account.getAccount(reqJoin.uid(), res-> {
            if (res.succeeded()) {
                Account account = res.result();
                if (account == null) {
                    sendResJoinWithError(session, RmResultCode.NotExist);
                    return;
                }

                // update player info
                Version clientVersion = new Version(reqJoin.version());
                Player player = new Player();
                player.setSessionId(session.getSessionId());
                player.from(account);
                player.setHero(reqJoin.hero());
                player.setVersion(clientVersion);

                if (clientVersion.compareTo(Service.meta.getMinClientVersion()) < 0) {
                    sendResJoinWithError(session, RmResultCode.OldVersion);
                    return;
                }

                Room room = null;
                if (reqJoin.aiPlayer() == null) {
                    room = Service.room.getOpenedRoom(reqJoin.league(), player.getLeaguePoint(), 50);
                }

                if (room == null) {
                    String key = Service.key.newPlaylogDBKey();
                    room = new Room(key, reqJoin.league());
                    Service.room.addRoom(room);
                }

                // join
                if (!Service.room.joinRoom(room, player)) {
                    sendResJoin(player, "");
                    return;
                }

                try {
                    session.setRoomId(room.getRoomId());
                    newTower(player);
                    newHero(player);
                }
                catch (Exception e) {
                    sendResJoinWithError(session, RmResultCode.DBError);
                    return;
                }

                // clone player when play with ai player
                if (reqJoin.aiPlayer() != null) {
                    Player aiPlayer = new Player();
                    aiPlayer.from(reqJoin.aiPlayer());
                    Service.room.joinRoom(room, aiPlayer);
                    newTower(aiPlayer);
                    newHero(aiPlayer);
                }

                // send join response
                sendResJoin(player, room.getRoomId());

                // send join notification
                sendNotiJoin(room);

                // start game
                if (room.isFull()) {
                    RmsVariable variable = Service.meta.getVariable();
                    short timeLimit = variable.playTime();
                    short countDownTime = variable.countDownTime();
                    room.setStartSec(currentSec + countDownTime);
                    room.setTimeLimit(timeLimit);
                    sendNotiStart(room);

                    // timeout check
                    Long timerId = Service.vertx.setTimer((countDownTime + timeLimit) * 1000, id -> {
                        Room timerRoom = Service.room.getRoomByTimerID(id);
                        if (timerRoom != null) {
                            closeRoom(timerRoom, (byte)-1);
                        }
                    });

                    Service.room.setRoomTimerID(room, timerId);
                }
            }
            else {
                sendResJoinWithError(session, RmResultCode.DBError);
            }
        });
    }

    public void processReqReJoin(Session session, ByteBuffer buffer) {
        RmmReqReJoin req = RmmReqReJoin.getRootAsRmmReqReJoin(buffer);
        Room room = Service.room.getRoom(req.roomId());
        Version clientVersion = new Version(req.version());
        long currentMilliSec = System.currentTimeMillis();
        long currentSec = currentMilliSec / 1000;

        if (room == null || room.getState() == RmmRoomState.Closed || room.getEndSec() <= currentSec) {
            sendResReJoinWithError(session, RmResultCode.NotExist);
            logger.info("room id: " + req.roomId() + " is not exist!");
            return;
        }

        Player player = room.getPlayer(req.uid());
        if (player == null) {
            sendResReJoinWithError(session, RmResultCode.NotExist);
            logger.info("player: " + req.uid() + " is not exist!");
            return;
        }

        player.setVersion(clientVersion);
        player.setSessionId(session.getSessionId());
        player.setDisconnected(false);
        if (clientVersion.compareTo(Service.meta.getMinClientVersion()) < 0) {
            sendResReJoinWithError(session, RmResultCode.OldVersion);
            return;
        }

        session.setRoomId(room.getRoomId());
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int playerOffset0 = getPlayerOffset0(room, builder);
        int playerOffset1 = getPlayerOffset1(room, builder);
        int roomIdOffset = builder.createString(room.getRoomId());
        int roomOffset = RmmRoom.createRmmRoom(
                builder,
                roomIdOffset,
                room.getLeague(),
                room.getState(),
                room.getStartSec(),
                room.getTimeLimit(),
                playerOffset0,
                playerOffset1);

        RmmResReJoin.startRmmResReJoin(builder);
        RmmResReJoin.addRoom(builder, roomOffset );
        RmmResReJoin.addTimestamp(builder, currentMilliSec);
        RmmResReJoin.addResultCode(builder, RmResultCode.Success);
        builder.finish(RmmResReJoin.endRmmResReJoin(builder));
        Service.session.send(player.getSessionId(), RmmMessageType.ResReJoin, builder.sizedByteArray());

        if (roomOffset != 0) {
            sendNotiRejoin(room, player);
        }
    }

    private void sendResReJoinWithError(Session session, int errorCode) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmResReJoin.startRmmResReJoin(builder);
        RmmResReJoin.addResultCode(builder, errorCode);
        builder.finish(RmmResReJoin.endRmmResReJoin(builder));
        Service.session.send(session, RmmMessageType.ResReJoin, builder.sizedByteArray());
    }

    private int getRewardOffset(BattleRewardResult reward, FlatBufferBuilder builder) {
        return RmmBattleReward.createRmmBattleReward(builder, 
            reward.exp, 
            reward.league,
            reward.leaguePoint,
            reward.gold);
    }

    private int getPlayerOffset1(Room room, FlatBufferBuilder builder) {
        int playerOffset1 = 0;
        if (room.getPlayer1() != null) {
            playerOffset1 = FlatBuilder.buildPlayer(builder, room.getPlayer1(), room.getTimeLimit());
        }
        return playerOffset1;
    }

    private int getPlayerOffset0(Room room, FlatBufferBuilder builder) {
        int playerOffset0 = 0;
        if (room.getPlayer0() != null) {
            playerOffset0 = FlatBuilder.buildPlayer(builder, room.getPlayer0(), room.getTimeLimit());
        }
        return playerOffset0;
    }

    private void sendNotiRejoin(Room room, Player player) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmNotiReJoin.startRmmNotiReJoin(builder);
        RmmNotiReJoin.addPlayerNo(builder, player.getPlayerNo());
        builder.finish(RmmNotiReJoin.endRmmNotiReJoin(builder));
        Service.room.broadcast(room, RmmMessageType.NotiReJoin, builder.sizedByteArray());
    }

    private void newHero(Player player) {
        RmsUnitBalance balance = Service.meta.getUnitBalance(player.getHero(), 1);
        if (balance == null) {
            logger.error("hero balance can not be null:" + player.getHero());
        }

        Unit unit = new Unit();
        unit.setName(player.getHero());
        unit.setLevel((byte)1);
        unit.setHp((short)balance.health());
        unit.setKey(player.getPlayerNo(), player.getNextUnitNo());
        unit.setPosition(getHeroStartingPosition(player));
        unit.setDirection(getStartingDirection(player));
        player.addUnit(unit);
    }

    Vector2 getHeroStartingPosition(Player player) {
        if (player.getPlayerNo() == 1) {
            return startingPosition0;
        }
        return startingPosition1;
    }

    Vector2b getStartingDirection(Player player) {
        if (player.getPlayerNo() == 1) {
            return startingDirection0;
        }
        return startingDirection1;
    }

    private void newTower(Player player) {
        RmsUnitBalance balance = Service.meta.getUnitBalance(RmUnitName.Tower, 1);
        if (balance == null) {
            logger.error("tower balance can not be null!");
        }

        Unit unit = new Unit();
        unit.setName(RmUnitName.Tower);
        unit.setLevel(player.getLevel());
        unit.setHp((short)balance.health());
        unit.setKey(player.getPlayerNo(), player.getNextUnitNo());
        unit.setPosition(getTowerStartingPosition(player));
        unit.setDirection(getStartingDirection(player));
        player.addUnit(unit);
    }

    Vector2 getTowerStartingPosition(Player player) {
        if (player.getPlayerNo() == 1) {
            return towerPosition0;
        }

        return towerPosition1;
    }

    public void processReqSync(Session session, ByteBuffer buffer) {
        RmmReqSync reqSync = RmmReqSync.getRootAsRmmReqSync(buffer);
        Room room = Service.room.getRoom(session.getRoomId());
        if (room == null) {
            logger.error("no room exist!");
            return;
        }

        Player player = room.getPlayer(session);
        if (player == null) {
            logger.error("no player exist!");
            return;
        }

        long currentMilliSec = System.currentTimeMillis();
        long currentSec = currentMilliSec / 1000;
        float time = (currentMilliSec - room.getStartSec() * 1000) / 1000f;
        short playTime = (short)(time * 100);

        if (currentSec < room.getStartSec()) {
            sendSyncError(player, (byte)0);
            return;
        }

        if (room.getEndSec() < currentSec) {
            closeRoom(room, (byte)-1);
            return;
        }

        sendNotiSync(player, room, reqSync, playTime);
        if (room.getState() == RmmRoomState.Play && room.hasWinner()) {
            closeRoom(room, room.getWinner().getPlayerNo());
            return;
        }
    }

    void closeRoom(Room room, byte winnerNo) {
        long currentMilliSec = System.currentTimeMillis();
        float time = (currentMilliSec - room.getStartSec() * 1000) / 1000f;
        room.setPlayTime((short)time);

        if (room.getState() == RmmRoomState.Play) {
            BattleRewardResultSet result = rewardController.calculateReward(room, winnerNo);
            rewardController.updateAccount(result.account0, room);
            rewardController.updateAccount(result.account1, room);
            if (result.winner != null) {
                int gold = result.gold;
                rewardController.giveReward(result.winner.getUid(), gold, 0);
            }

            room.setState(RmmRoomState.Closed);
            sendNotiEnd(room, winnerNo, result);
            recordPlaylog(room, result, winnerNo);
        }

        Service.room.removeRoom(room);
    }

    void sendNotiEnd(Room room, byte winnerNo, BattleRewardResultSet result) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int playerOffset0 = getPlayerOffset0(room, builder);
        int playerOffset1 = getPlayerOffset1(room, builder);

        RmmNotiEnd.startRmmNotiEnd(builder);
        RmmNotiEnd.addWinner(builder, winnerNo);
        RmmNotiEnd.addPlayer0(builder, playerOffset0);
        RmmNotiEnd.addPlayer1(builder, playerOffset1);
        RmmNotiEnd.addPlayer1(builder, playerOffset1);
        RmmNotiEnd.addReward0(builder, getRewardOffset(result.player0, builder));
        RmmNotiEnd.addReward1(builder, getRewardOffset(result.player1, builder));
        builder.finish(RmmNotiEnd.endRmmNotiEnd(builder));
        Service.room.broadcast(room, RmmMessageType.NotiEnd, builder.sizedByteArray());
    }

    void recordPlaylog(Room room, BattleRewardResultSet rewards, byte winnerNo) {
        Player player0 = room.getPlayer0();
        Player player1 = room.getPlayer1();

        Playlog log = new Playlog();
        log.pid = room.getRoomId();
        log.player0 = player0.getUid();
        log.player1 = player1.getUid();
        log.winner = room.getPlayerUid(winnerNo);
        log.name0 = player0.getName();
        log.leaguePoint0 = player0.getLeaguePoint();
        log.rewardPoint0 = rewards.player0.leaguePoint;
        log.name1 = player1.getName();
        log.leaguePoint1 = player1.getLeaguePoint();
        log.rewardPoint1 = rewards.player1.leaguePoint;
        log.playTime = room.getPlayTime();
        log.birth = room.getStartSec();
        log.ver = Service.meta.getMinClientVersion().get();
        log.raw = room.getRecord();
        room.clearRecord();

        Service.playlog.record(log, res -> {
            if (res.succeeded()) {
                sendNotiPlaylog(room, res.result());
            }
        });
    }

    void sendNotiPlaylog(Room room, Playlog log) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        builder.finish(FlatBuilder.build(builder, log));
        Service.room.broadcastNoRecord(room, RmmMessageType.NotiPlaylog, builder.sizedByteArray());
    }

    void sendSyncError(Player player, byte reason) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmResSyncError.startRmmResSyncError(builder);
        RmmResSyncError.addPlayerNo(builder, player.getPlayerNo());
        RmmResSyncError.addReason(builder, reason);
        builder.finish(RmmResSyncError.endRmmResSyncError(builder));
        Service.session.send(player.getSessionId(), RmmMessageType.ResSyncError, builder.sizedByteArray());
    }

    void sendResJoin(Player player, String roomKey) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int roomIdOffset = builder.createString(roomKey);
        RmmResJoin.startRmmResJoin(builder);
        RmmResJoin.addRoomId(builder, roomIdOffset);
        RmmResJoin.addPlayerNo(builder, player.getPlayerNo());
        RmmResJoin.addTimestamp(builder, System.currentTimeMillis());
        RmmResJoin.addResultCode(builder, RmResultCode.Success);
        builder.finish(RmmResJoin.endRmmResJoin(builder));
        Service.session.send(player.getSessionId(), RmmMessageType.ResJoin, builder.sizedByteArray());
    }

    private void sendResJoinWithError(Session session, int errorCode) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmResJoin.startRmmResJoin(builder);
        RmmResJoin.addResultCode(builder, errorCode);
        builder.finish(RmmResJoin.endRmmResJoin(builder));
        Service.session.send(session, RmmMessageType.ResJoin, builder.sizedByteArray());
    }

    void sendNotiStart(Room room) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmNotiStart.startRmmNotiStart(builder);
        RmmNotiStart.addStartSec(builder, room.getStartSec());
        RmmNotiStart.addPlaySec(builder, room.getTimeLimit());
        builder.finish(RmmNotiStart.endRmmNotiStart(builder));
        Service.room.broadcast(room, RmmMessageType.NotiStart, builder.sizedByteArray());
    }

    void sendNotiJoin(Room room) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int playerOffset0 = getPlayerOffset0(room, builder);
        int playerOffset1 = getPlayerOffset1(room, builder);

        RmmNotiJoin.startRmmNotiJoin(builder);
        RmmNotiJoin.addPlayer0(builder, playerOffset0);
        RmmNotiJoin.addPlayer1(builder, playerOffset1);
        builder.finish(RmmNotiJoin.endRmmNotiJoin(builder));
        Service.room.broadcast(room, RmmMessageType.NotiJoin, builder.sizedByteArray());
    }

    void applyOrder(RmmReqSync reqSync, Room room, short playTime) {
        for (int index = 0; index < reqSync.ordersLength(); index++) {
            RmmUnitOrder order = reqSync.orders(index);
            int from = order.from();
            int unitNo = Unit.getUnitNo(from);

            Player player = room.getPlayer(Unit.getPlayerNo(from));
            Unit unit = player.getUnit(unitNo);

            if (unit != null) {
                unit.syncOrder(order, playTime);
            }
        }
    }

    private Unit newMinion(Player player, short name, int posIndex) {
        RmsUnitBalance balance = Service.meta.getUnitBalance(name, 1);

        Unit unit = new Unit();
        unit.setName(name);
        unit.setLevel((byte)1);
        unit.setHp((short)balance.health());
        unit.setKey(player.getPlayerNo(), player.getNextUnitNo());

        Vector2 pt = minonSpawnPts.get(posIndex);

        if (player.getPlayerNo() == 1) {
            unit.setPosition(new Vector2(pt.getX(), -1100 - pt.getZ()));
            unit.setDirection(startingDirection0);
        }
        else {
            unit.setPosition(new Vector2(-pt.getX(), 1100 + pt.getZ()));
            unit.setDirection(startingDirection1);
        }

        player.addUnit(unit);
        return unit;
    }

    void sapwnMinion(Room room, short playTime) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int [] units = new int[room.getPlayerCount() * 4];
        int index = 0;

        for (byte no = 0; no < room.getPlayerCount(); no++) {
            Player player = room.getPlayer(no);
            Unit unit0 = newMinion(player, RmUnitName.SkeletonBasic, 0);
            Unit unit1 = newMinion(player, RmUnitName.SkeletonBasic, 1);
            Unit unit2 = newMinion(player, RmUnitName.SkeletonBasic, 2);
            Unit unit3 = newMinion(player, RmUnitName.SkeletonBasic, 3);
            units[index++] = FlatBuilder.buildUnit(builder, unit0, playTime);
            units[index++] = FlatBuilder.buildUnit(builder, unit1, playTime);
            units[index++] = FlatBuilder.buildUnit(builder, unit2, playTime);
            units[index++] = FlatBuilder.buildUnit(builder, unit3, playTime);
        }
        room.setSpawnIteration(room.getSpawnIteration()+1);

        int deployOffset = RmmNotiDeploy.createDeployVector(builder, units);
        RmmNotiDeploy.startRmmNotiDeploy(builder);
        RmmNotiDeploy.addSec(builder, playTime);
        RmmNotiDeploy.addDeploy(builder, deployOffset);
        builder.finish(RmmNotiDeploy.endRmmNotiDeploy(builder));
        Service.room.broadcast(room, RmmMessageType.NotiDeploy, builder.sizedByteArray());
    }

    boolean revivalHero(Player player, Unit hero, short playTime) {
        int aliveTime = player.getHeroDeadTime() + Service.meta.getRevivalTime(hero.getLevel()) * 100;
        if (aliveTime < playTime) {
            player.setHeroDeadTime((short)0);
            RmsUnitBalance balance = Service.meta.getUnitBalance(hero.getName(), hero.getLevel());
            hero.setHp((short)balance.health());
            hero.setPosition(getHeroStartingPosition(player));
            hero.setDirection(getStartingDirection(player));
            return true;
        }

        return false;
    }

    void sendRevival(Room room, Unit unit, short playTime) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int heroRevivalOffset = FlatBuilder.buildUnit(builder, unit, playTime);

        RmmNotiRevival.startRmmNotiRevival(builder);
        RmmNotiRevival.addSec(builder, playTime);
        RmmNotiRevival.addHeroRevival(builder, heroRevivalOffset);
        builder.finish(RmmNotiRevival.endRmmNotiRevival(builder));
        Service.room.broadcast(room, RmmMessageType.NotiRevival, builder.sizedByteArray());
    }

    void sendNotiLvUp(Room room, Unit hero) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmNotiLvUp.startRmmNotiLvUp(builder);
        RmmNotiLvUp.addKey(builder, hero.getKey());
        RmmNotiLvUp.addLevel(builder, hero.getLevel());
        RmmNotiLvUp.addExp(builder, hero.getExp());
        builder.finish(RmmNotiLvUp.endRmmNotiLvUp(builder));
        Service.room.broadcast(room, RmmMessageType.NotiLvUp, builder.sizedByteArray());
    }

    void sendNotiSync(Player player, Room room, RmmReqSync reqSync, short playTime) {
        applyOrder(reqSync, room, playTime);

        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int ordersVectorOffset = addOrder(builder, room, reqSync);

        RmmNotiSync.startRmmNotiSync(builder);
        RmmNotiSync.addPlayerNo(builder, player.getPlayerNo());
        RmmNotiSync.addSec(builder, playTime);
        RmmNotiSync.addOrders(builder, ordersVectorOffset);
        builder.finish(RmmNotiSync.endRmmNotiSync(builder));
        Service.room.broadcast(room, RmmMessageType.NotiSync, builder.sizedByteArray());

        syncHit(builder, room, reqSync, playTime);
        syncAbnormal(room, reqSync, playTime);

        // spawn minion
        if (room.isNeedSpawn(playTime)) {
            sapwnMinion(room, playTime);
        }

        syncHero(player, room, playTime);
        if (room.getPlayer0() == player) {
            if (room.getPlayer1().isAi()) {
                syncHero(room.getPlayer1(), room, playTime);
            }
        }
    }

    void syncHero(Player player, Room room, short playTime) {
        Unit hero = player.getHeroUnit();
        if (hero.isDead()) {
            if (revivalHero(player, hero, playTime)) {
                sendRevival(room, hero, playTime);
            }
        }

        if (hero.isExpDirty()) {
            sendNotiLvUp(room, hero);
            hero.setExpDirty(false);
        }
    }


    void syncAbnormal(Room room, RmmReqSync reqSync, short playTime) {
        if (reqSync.hitsLength() == 0) {
            return;
        }

        int abnormalCount = 0;
        ArrayList<Abnormal> abnormalList = null;

        for (int index = 0; index < reqSync.hitsLength(); index++) {
            RmmUnitHit hit = reqSync.hits(index);
            if (hit.skillId() == 0) {
                continue;
            }

            short from = hit.from();
            short to = hit.to();
            int fromUnitNo = Unit.getUnitNo(from);
            int toUnitNo = Unit.getUnitNo(to);

            Player fromPlayer = room.getPlayer(Unit.getPlayerNo(from));
            Player toPlayer = room.getPlayer(Unit.getPlayerNo(to));
            Unit fromUnit = fromPlayer.getUnit(fromUnitNo);
            Unit toUnit = toPlayer.getUnit(toUnitNo);
            if (fromUnit == null || toUnit == null) {
                continue;
            }

            if (fromUnit.isDead() || toUnit.isDead()) {
                continue;
            }

            RmsSkillBalance skillBalance = Service.meta.getSkillBalance(hit.skillId(), 1);
            if (skillBalance.abnormal() == 0) {
                continue;
            }

            RmsAbnormalBalance balance = Service.meta.getAbnormalBalance(skillBalance.abnormal());
            Abnormal neAbnormal = new Abnormal();
            neAbnormal.setAt(playTime);
            neAbnormal.setValue((short)(balance.value() * 100));
            neAbnormal.setDuration((short)(balance.duration() * 100));
            neAbnormal.setFrom(from);
            neAbnormal.setTo(to);
            neAbnormal.setType((byte)balance.type());
            toUnit.syncAbnormal(neAbnormal);

            if (abnormalList == null) {
                abnormalList = new ArrayList<>();
            }

            abnormalList.add(neAbnormal);
            abnormalCount++;
        }

        if (abnormalCount == 0) {
            return;
        }

        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        RmmNotiAbnormal.startAbnormalVector(builder, abnormalCount);
        for (Abnormal abnormal : abnormalList) {
            FlatBuilder.buildAbnormal(builder, abnormal);
        }
        int abnormalOffset = builder.endVector();

        RmmNotiAbnormal.startRmmNotiAbnormal(builder);
        RmmNotiAbnormal.addSec(builder, playTime);
        RmmNotiAbnormal.addAbnormal(builder, abnormalOffset);
        builder.finish(RmmNotiAbnormal.endRmmNotiAbnormal(builder));
        Service.room.broadcast(room, RmmMessageType.NotiAbnormal, builder.sizedByteArray());
    }

    void collectStatics(Player from, Player to, Unit fromUnit, Unit toUnit, int damage) {
        if (fromUnit.getUnitNo() == Player.kHeroUnitNo) {
            if (toUnit.getUnitNo() == Player.kHeroUnitNo) {
                from.addDealToHero(damage);
                if (toUnit.isDead()) {
                    from.addKillHeroCount((short)1);
                    short exp = (short)Service.meta.getHeroExpGet(RmUnitType.Hero);
                    fromUnit.addExp(exp);
                    LevelUp(fromUnit);
                }
            }
            else {
                from.addDealToPet(damage);
                if (toUnit.isDead()) {
                    from.addKillPetCount((short)1);
                    short exp = (short)Service.meta.getHeroExpGet(RmUnitType.Pet);
                    fromUnit.addExp(exp);
                    LevelUp(fromUnit);
                }
            }
        }

        if (toUnit.getUnitNo() == Player.kHeroUnitNo) {
            if (fromUnit.getUnitNo() == Player.kHeroUnitNo) {
                to.addDamageFromHero(damage);
                if (toUnit.isDead()) {
                    to.addDeadCount((byte)1);
                }
            }
            else {
                to.addDamageFromPet(damage);
                if (toUnit.isDead()) {
                    to.addDeadCount((byte)1);
                }
            }
        }
    }

    void LevelUp(Unit unit) {
        if (unit.getLevel() == Service.meta.getMaxHeroLevel()) {
            return;
        }

        int maxExp = Service.meta.getHeroExpUp(unit.getLevel());
        int exp = unit.getExp();

        if (maxExp <= exp) {
            unit.addLevel((byte)1);
            unit.setExp((short)(exp - maxExp));
            if (!unit.isDead()) {
                RmsUnitBalance balance = Service.meta.getUnitBalance(unit.getName(), unit.getLevel());
                unit.setHp((short)balance.health());
            }
            LevelUp(unit);
        }
    }

    void syncHit(FlatBufferBuilder builder, Room room, RmmReqSync reqSync, short playTime) {
        if (reqSync.hitsLength() == 0) {
            return;
        }

        RmmNotiDamage.startDamagesVector(builder, reqSync.hitsLength());
        for (int index = 0; index < reqSync.hitsLength(); index++) {
            RmmUnitHit hit = reqSync.hits(index);
            short from = hit.from();
            short to = hit.to();
            int fromUnitNo = Unit.getUnitNo(from);
            int toUnitNo = Unit.getUnitNo(to);

            Player fromPlayer = room.getPlayer(Unit.getPlayerNo(from));
            Player toPlayer = room.getPlayer(Unit.getPlayerNo(to));
            Unit fromUnit = fromPlayer.getUnit(fromUnitNo);
            Unit toUnit = toPlayer.getUnit(toUnitNo);
            if (fromUnit == null || toUnit == null) {
                short o = 0;
                RmmUnitDamage.createRmmUnitDamage(builder, o, o, o, o, o, o);
                continue;
            }

            if (fromUnit.isDead() || toUnit.isDead()) {
                short o = 0;
                RmmUnitDamage.createRmmUnitDamage(builder, o, o, o, o, o, o);
                continue;
            }

            short damage = damageCalculator.calculate(fromUnit, toUnit, hit.skillId(), hit.rate());
            toUnit.reduceHp(damage);
            if (toUnit.getHp() <= 0) {
                toUnit.setHp((short)0);
                if (toUnitNo == Player.kHeroUnitNo) {
                    toPlayer.setHeroDeadTime(playTime);
                }
                else {
                    toPlayer.removeUnit(toUnitNo);
                }
            }

            collectStatics(fromPlayer, toPlayer, fromUnit, toUnit, damage);
            short hp = toUnit.getHp();
            short x = hit.force().x();
            short z = hit.force().z();
            RmmUnitDamage.createRmmUnitDamage(builder, from, to, damage, hp, x, z);
        }

        int damagesOffset = builder.endVector();
        builder.finish(RmmNotiDamage.createRmmNotiDamage(builder, playTime, damagesOffset));
        Service.room.broadcast(room, RmmMessageType.NotiDamage, builder.sizedByteArray());
    }

    private int addOrder(FlatBufferBuilder builder, Room room, RmmReqSync reqSync) {
        int count = reqSync.ordersLength();
        if (count == 0) {
            return 0;
        }

        RmmNotiSync.startOrdersVector(builder, count);
        for (int index = 0; index < count; index++) {
            RmmUnitOrder order = reqSync.orders(index);
            short from = order.from();

            Player player = room.getPlayer(Unit.getPlayerNo(from));
            int fromUnitNo = Unit.getUnitNo(from);
            Unit unit = player.getUnit(fromUnitNo);
            if (unit == null || unit.isDead()) {
                short o = 0;
                RmmUnitOrder.createRmmUnitOrder(builder, o, o, o, o, o, o, (byte)o, (byte)o,(byte)0);
                continue;
            }

            unit.addOrder(builder);
        }

        return builder.endVector();
    }
}