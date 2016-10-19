package com.clue.model;

import com.clue.fbs.*;
import com.clue.util.IntegerMBSerializer;
import io.vertx.core.buffer.Buffer;

import java.util.HashSet;

public class Room {
    public static final byte kSpawnInterval = 30;
    final int recordBufferSize = 1024 * 150;

    String roomId = "";
    Player player0 = null;
    Player player1 = null;
    HashSet<Player> observers = new HashSet<Player>();
    byte league = 1;
    byte state = RmmRoomState.Open;
    long startSec = 0;
    short timeLimit = 0;
    short playTime = 0;
    int spawnIteration = 0;
    Buffer bb = null;
    long timerID = 0;

    public Room(String key, byte league) {
        this.roomId = key;
        this.league = league;
    }

    public String getRoomId() {
        return roomId;
    }

    public byte getLeague() {
        return league;
    }

    public void setLeague(byte league) {
        this.league = league;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public long getStartSec() {
        return startSec;
    }

    public void setStartSec(long startSec) {
        this.startSec = startSec;
    }

    public long getEndSec() {
        return startSec + timeLimit;
    }

    public long getTimerID() {
        return timerID;
    }

    public void setTimerID(long timerID) {
        this.timerID = timerID;
    }

    public short getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(short timeLimit) {
        this.timeLimit = timeLimit;
    }

    public short getPlayTime() {
        return playTime;
    }

    public void setPlayTime(short playTime) {
        this.playTime = playTime;
    }

    public boolean isEmpty() {
        return (player0 == null && player1 == null);
    }

    public boolean isFull() {
        return (player0 != null && player1 != null);
    }

    public int getSpawnIteration() {
        return spawnIteration;
    }

    public void setSpawnIteration(int spawnIteration) {
        this.spawnIteration = spawnIteration;
    }

    public void record(byte messageType, byte[] data) throws Exception {
        if (bb == null) {
            bb = Buffer.buffer();
        }

        bb.appendBytes(IntegerMBSerializer.encodeLength(data.length + 1));
        bb.appendByte(messageType);
        bb.appendBytes(data);
    }

    public byte[] getRecord() {
        return bb.getBytes();
    }

    public void clearRecord() {
        bb = null;
    }

    public String getPlayerUid(byte playerNo) {
        Player player = getPlayer(playerNo);
        if (player == null) {
            return "";
        }

        return player.uid;
    }

    public Player getPlayer0() {
        return player0;
    }

    public void setPlayer0(Player player) {
        player0 = player;
        player.setPlayerNo((byte)0);
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player) {
        player1 = player;
        player.setPlayerNo((byte)1);
    }

    public Player getPlayer(byte playerNo) {
        if (player0 != null && player0.getPlayerNo() == playerNo) {
            return player0;
        }

        if (player1 != null && player1.getPlayerNo() == playerNo) {
            return player1;
        }

        return null;
    }

    public Player getPlayer(String uid) {
        if (player0 != null && player0.getUid().equals(uid)) {
            return player0;
        }

        if (player1 != null && player1.getUid().equals(uid)) {
            return player1;
        }

        return null;
    }

    public Player getPlayer(Session session) {
        if (player0 != null && player0.getSessionId() == session.getSessionId()) {
            return player0;
        }

        if (player1 != null && player1.getSessionId() == session.getSessionId()) {
            return player1;
        }

        return null;
    }

    public int getPlayerCount() {
        int count = 0;
        if (player0 != null) {
            count++;
        }
        if (player1 != null) {
            count++;
        }

        return count;
    }

    public boolean isNeedSpawn(short playTime) {
        int iteration = Math.round(playTime / (kSpawnInterval*100));
        if (iteration > spawnIteration) {
            return true;
        }
        return false;
    }

    public boolean hasWinner() {
        if (player0 == null || player1 == null) {
            return false;
        }

        return (player0.getTowerUnit() == null || player1.getTowerUnit() == null);
    }

    public Player getWinner() {
        if (player0 == null || player1 == null) {
            return null;
        }

        if (player0.getTowerUnit() != null) {
            return player0;
        }

        if (player1.getTowerUnit() != null) {
            return player1;
        }

        return null;
    }
}
