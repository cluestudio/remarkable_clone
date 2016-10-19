package com.clue.model;

import com.clue.fbs.RmmPlayer;
import com.clue.util.Version;
import io.vertx.core.http.ServerWebSocket;

import java.util.HashMap;

public class Player {
    public static final int kTowerUnitNo = 1;
    public static final int kHeroUnitNo = 2;

    String uid = "";
    String sessionId = "";
    String roomId = "";
    String name = "";
    String country = "";
    byte playerNo = 0;
    short leaguePoint = 0;
    byte level = 1;
    int exp = 0;
    short hero = 0;
    short heroDeadTime = 0;
    boolean ai = false;
    boolean observer = false;
    boolean disconnected = false;
    HashMap<Integer, Unit> units = new HashMap<>();
    int currentUnitNo = 1;
    short killHeroCount = 0;
    short killPetCount = 0;
    byte deadCount = 0;
    int dealToHero = 0;
    int dealToPet = 0;
    int damageFromHero = 0;
    int damageFromPet = 0;
    Version version = new Version();
    Account account = null;

    public Player() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte getPlayerNo() {
        return playerNo;
    }

    public void setPlayerNo(byte playerNo) {
        this.playerNo = playerNo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public static String getKey(ServerWebSocket socket) {
        return socket.binaryHandlerID();
    }

    public short getLeaguePoint() {
        return leaguePoint;
    }

    public void setLeaguePoint(short leaguePoint) {
        this.leaguePoint = leaguePoint;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public short getHero() {
        return hero;
    }

    public void setHero(short hero) {
        this.hero = hero;
    }

    public short getHeroDeadTime() {
        return heroDeadTime;
    }

    public void setHeroDeadTime(short heroDeadTime) {
        this.heroDeadTime = heroDeadTime;
    }

    public boolean isObserver() {
        return observer;
    }

    public void setObserver(boolean observer) {
        this.observer = observer;
    }

    public HashMap<Integer, Unit> getUnits() {
        return units;
    }

    public short getKillHeroCount() {
        return killHeroCount;
    }

    public void setKillHeroCount(short killHeroCount) {
        this.killHeroCount = killHeroCount;
    }

    public void addKillHeroCount(short killHeroCount) {
        this.killHeroCount += killHeroCount;
    }

    public short getKillPetCount() {
        return killPetCount;
    }

    public void setKillPetCount(short killPetCount) {
        this.killPetCount = killPetCount;
    }

    public void addKillPetCount(short killPetCount) {
        this.killPetCount += killPetCount;
    }

    public byte getDeadCount() {
        return deadCount;
    }

    public void setDeadCount(byte deadCount) {
        this.deadCount = deadCount;
    }

    public void addDeadCount(byte deadCount) {
        this.deadCount += deadCount;
    }

    public int getDealToHero() {
        return dealToHero;
    }

    public void setDealToHero(int dealToHero) {
        this.dealToHero = dealToHero;
    }

    public void addDealToHero(int dealToHero) {
        this.dealToHero += dealToHero;
    }

    public int getDealToPet() {
        return dealToPet;
    }

    public void setDealToPet(int dealToPet) {
        this.dealToPet = dealToPet;
    }

    public void addDealToPet(int dealToPet) {
        this.dealToPet += dealToPet;
    }

    public int getDamageFromHero() {
        return damageFromHero;
    }

    public void setDamageFromHero(int damageFromHero) {
        this.damageFromHero = damageFromHero;
    }

    public void addDamageFromHero(int damageFromHero) {
        this.damageFromHero += damageFromHero;
    }

    public int getDamageFromPet() {
        return damageFromPet;
    }

    public void setDamageFromPet(int damageFromPet) {
        this.damageFromPet = damageFromPet;
    }

    public void addDamageFromPet(int damageFromPet) {
        this.damageFromPet += damageFromPet;
    }

    public Unit getUnit(int no) {
        return units.get(no);
    }

    public Unit getTowerUnit() {
        return units.get(kTowerUnitNo);
    }

    public Unit getHeroUnit() {
        return units.get(kHeroUnitNo);
    }

    public int getUnitCount() { return units.size(); }

    public int getNextUnitNo() { return currentUnitNo++; }

    public void addUnit(Unit unit) {
        units.put(unit.getUnitNo(), unit);
    }

    public void removeUnit(int key) {
        units.remove(key);
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public boolean isAi() {
        return ai;
    }

    public void setAi(boolean ai) {
        this.ai = ai;
    }

    public int getCurrentUnitNo() {
        return currentUnitNo;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void from(Account account) {
        this.account = account;

        uid = avoidNull(account.uid);
        ai = false;
        name = avoidNull(account.name);
        country = avoidNull(account.country);
        leaguePoint = (short)account.leaguePoint;
        level = (byte)account.level;
        exp = account.exp;
    }

    public void from(RmmPlayer player) {
        uid = avoidNull(player.uid());
        ai = player.ai();
        name = avoidNull(player.name());
        country = avoidNull(player.country());
        leaguePoint = player.leaguePoint();
        playerNo = player.playerNo();
        level = player.level();
        hero = player.hero();
        heroDeadTime = player.heroDeadTime();
        killHeroCount = player.killHeroCount();
        killPetCount = player.killPetCount();
        deadCount = player.deadCount();
        dealToHero = player.dealToHero();
        dealToPet = player.dealToPet();
        damageFromHero = player.damageFromHero();
        damageFromPet = player.damageFromPet();

        for (int index=0; index < player.unitsLength(); index++) {
            Unit unit = new Unit();
            unit.from(player.units(index));
            addUnit(unit);
        }
    }

    String avoidNull(String text) {
        if (text == null) {
            return "";
        }

        return text;
    }
}
