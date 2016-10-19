package com.clue.controller;

import com.clue.fbs.RmsLeagueBalance;
import com.clue.fbs.RmsVariable;
import com.clue.model.*;
import com.clue.service.MetaService;
import com.clue.service.Service;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class BattleRewardController {
    private final int divideBase = 12;
    private final int baseScore = 30;
    private final int loseReduce = 2000;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MetaService metaService = null;

    public BattleRewardController() {
        metaService = Service.meta;
    }

    public void setMetaService(MetaService metaService) {
        this.metaService = metaService;
    }

    public BattleRewardResultSet calculateReward(Room room, byte winnerPlayerNo) {
        BattleRewardResultSet result = new BattleRewardResultSet();

        Player player0 = room.getPlayer0();
        Player player1 = room.getPlayer1();
        Account account0 = player0.getAccount();
        Account account1 = player1.getAccount();
        Player winner = room.getPlayer(winnerPlayerNo);
        RmsLeagueBalance leagueBalance = metaService.getLeagueBalance(room.getLeague());
        int gold = leagueBalance.gold();

        RmsVariable variable = metaService.getVariable();
        int exp0 = variable.drawExp();
        int exp1 = variable.drawExp();
        int leaguePoint0 = 0;
        int leaguePoint1 = 0;
        int gold0 = 0;
        int gold1 = 0;

        if (winner == player0) {
            leaguePoint0 = getWinPoint(player0, player1);
            leaguePoint1 = getLosePoint(player1, player0);
            exp0 = variable.winExp();
            exp1 = variable.loseExp();
            gold0 = gold;
        }
        else if (winner == player1) {
            leaguePoint0 = getLosePoint(player0, player1);
            leaguePoint1 = getWinPoint(player1, player0);
            exp0 = variable.loseExp();
            exp1 = variable.winExp();
            gold1 = gold;
        }
        else {
            exp0 = variable.drawExp();
            exp1 = variable.drawExp();
        }

        addExp(account0, exp0);
        addExp(account1, exp1);
        result.account0 = account0;
        result.account1 = account1;
        result.player0.league = addLeaguePoint(account0, leaguePoint0);
        result.player1.league = addLeaguePoint(account1, leaguePoint1);
        result.player0.exp = (short)exp0;
        result.player0.leaguePoint = (short)leaguePoint0;
        result.player0.gold = (short)gold0;
        result.player1.exp = (short)exp1;
        result.player1.leaguePoint = (short)leaguePoint1;
        result.player1.gold = (short)gold1;
        result.winner = winner;
        result.gold = gold;
        return result;
    }

    private int getWinPoint(Player me, Player enemy) {
        return (int)((enemy.getLeaguePoint() - me.getLeaguePoint()) / (float)divideBase) + baseScore;
    }

    private int getLosePoint(Player me, Player enemy) {
        int result = -getWinPoint(me, enemy);
        if (me.getLeaguePoint() < loseReduce) {
            result = (int)(result * (me.getLeaguePoint() / (float)loseReduce));
        }

        return result;
    }

    public void giveReward(String uid, int gold, int ruby) {
        Service.account.increaseAsset(uid, gold, ruby, res -> {
            if (res.failed()) {
                logger.error("increase asset failed!");
            }
        });
    }

    public void updateAccount(Account account, Room room) {
        if (account != null) {
            account.lastPlayTime = room.getStartSec();
            Service.account.updateAccount(account, res -> {
                if (res.failed()) {
                    logger.error("update actor failed!");
                }
            });
        }
    }

    private byte addLeaguePoint(Account account, int leaguePoint) {
        if (account == null) {
            return 0;
        }

        account.leaguePoint += leaguePoint;
        if (account.leaguePoint < 0) {
            account.leaguePoint = 0;
        }

        RmsVariable variable = metaService.getVariable();
        if (account.league >= variable.maxLeague()) {
            return account.league;
        }

        int currentLeague = 0;
        int leaguePointSum = 0;
        for (int league = 1; league <= variable.maxLeague(); league++) {
            int neededPoint = metaService.getLeagueBalance(league).point();
            leaguePointSum += neededPoint;
            if (account.leaguePoint >= leaguePointSum) {
                currentLeague = league;
                continue;
            }

            break;
        }

        account.league = (byte) currentLeague;
        return account.league;
    }

    private void addExp(Account account, int exp) {
        if (account == null) {
            return;
        }

        RmsVariable variable = metaService.getVariable();
        if (account.level >= variable.maxLevel()) {
            return;
        }

        account.exp += exp;
        for (int level = account.level + 1; level <= variable.maxLevel(); level++) {
            int neededExp = metaService.getExp(level);
            if (neededExp <= account.exp) {
                account.level = level;
                account.exp -= neededExp;
                if (account.level == variable.maxLevel()) {
                    account.exp = 0;
                }
                continue;
            }
            break;
        }
    }
}
