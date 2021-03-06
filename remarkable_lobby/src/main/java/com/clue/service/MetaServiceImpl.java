package com.clue.service;

import com.clue.fbs.*;
import com.clue.mapper.MetaMapper;
import com.clue.model.*;
import com.clue.util.Version;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;

public class MetaServiceImpl implements MetaService {
    Logger logger = LoggerFactory.getLogger(getClass());
    MetaSet metaSet = null;
    MetaMapper mapper = new MetaMapper();
    HashMap<Short, HashMap<Integer, RmsUnitBalance>> unitBalance = new HashMap<>();
    HashMap<Short, HashMap<Integer, RmsSkillBalance>> skillBalance = new HashMap<>();
    HashMap<Integer, RmsAbnormalBalance> abnormalBalance = new HashMap<>();
    HashMap<Byte, RmsHeroExpGet> heroExpGet = new HashMap<>();
    HashMap<Integer, RmsHeroExpUp> heroExpUp = new HashMap<>();
    HashMap<Integer, RmsHeroRevival> heroRevival = new HashMap<>();
    HashMap<Integer, RmsExp4LevelUp> exp4LevelUp = new HashMap<>();
    HashMap<Integer, RmsLeagueBalance> leagueBalance= new HashMap<>();
    HashMap<Short, AnimationInfo> animationInfo = new HashMap<>();
    HashMap<Short, RmsUnitStatic> unitStatic= new HashMap<>();
    final int kMaxHeroLevel = 6;
    boolean ready = false;
    Version minClientVersion = new Version();
    final String metaChanel = "remarkable-meta-channel";

    public boolean isReady() {
        return ready;
    }

    public void initialize(Vertx vertx) {
        queryMeta();
        vertx.eventBus().<JsonObject>consumer("io.vertx.redis." + metaChanel, received -> {
            queryMeta();
        });

        Service.ps.getRedis().subscribe(metaChanel, res -> {
            logger.info("subscribe success!");
        });
    }

    public byte[] getMetaSetBytes() {
        return metaSet.binary;
    }

    void queryMeta() {
        String db = Service.ps.getMetaDBName();
        Service.ps.queryAtMeta(mapper.getAllQuery(db), res -> {
            if (res.succeeded()) {
                metaSet = mapper.parseMeta(res.result());
                hashing();
                logger.info("meta updated!");
                ready = true;
            }
        });
    }

    void hashing() {
        RmsMetaSet set = metaSet.data;

        // hash exp levelUp
        for (int index=0; index < set.exp4LevelUpLength(); index++) {
            RmsExp4LevelUp balance = set.exp4LevelUp(index);
            exp4LevelUp.put(balance.level(), balance);
        }

        // hash unit balance
        for (int index=0; index < set.unitBalanceLength(); index++) {
            RmsUnitBalance balance = set.unitBalance(index);
            if (!unitBalance.containsKey(balance.name())) {
                unitBalance.put(balance.name(), new HashMap<>());
            }

            HashMap<Integer, RmsUnitBalance> balanceSet = unitBalance.get(balance.name());
            balanceSet.put(balance.level(), balance);
        }

        // hash skill balance
        for (int index=0; index < set.skillBalanceLength(); index++) {
            RmsSkillBalance balance = set.skillBalance(index);
            if (!skillBalance.containsKey(balance.skillType())) {
                skillBalance.put(balance.skillType(), new HashMap<>());
            }

            HashMap<Integer, RmsSkillBalance> balanceSet = skillBalance.get(balance.skillType());
            balanceSet.put(balance.level(), balance);
        }

        // hero exp get
        for (int index=0; index < set.heroExpGetLength(); index++) {
            RmsHeroExpGet balance = set.heroExpGet(index);
            heroExpGet.put(balance.type(), balance);
        }

        // hero exp up
        for (int index=0; index < set.heroExpUpLength(); index++) {
            RmsHeroExpUp balance = set.heroExpUp(index);
            heroExpUp.put(balance.level(), balance);
        }

        // abnormal
        for (int index=0; index < set.abnormalBalanceLength(); index++) {
            RmsAbnormalBalance balance = set.abnormalBalance(index);
            abnormalBalance.put(balance.key(), balance);
        }

        // revival time
        for (int index=0; index < set.heroRevivalLength(); index++) {
            RmsHeroRevival balance = set.heroRevival(index);
            heroRevival.put(balance.level(), balance);
        }

        // leagueBalance hash
        for (int index=0; index < set.leagueBalanceLength(); index++) {
            RmsLeagueBalance balance = set.leagueBalance(index);
            leagueBalance.put(balance.league(), balance);
        }

        // unit static
        for (int index=0; index < set.unitStaticLength(); index++) {
            RmsUnitStatic balance = set.unitStatic(index);
            unitStatic.put(balance.name(), balance);
        }

        // animation info
        for (int index=0; index < set.animationInfoLength(); index++) {
            RmsAnimationInfo balance = set.animationInfo(index);
            AnimationInfo info = new AnimationInfo(balance);
            animationInfo.put(balance.name(), info);
        }

        minClientVersion = new Version(set.variable().minVersion());
    }

    public int getVersion() {
        return metaSet.version;
    }

    public RmsMetaSet getMetaSet() {
        return metaSet.data;
    }

    public RmsUnitBalance getUnitBalance(short name, int level) {
        if (!unitBalance.containsKey(name)) {
            return null;
        }

        return unitBalance.get(name).get(level);
    }

    public RmsUnitStatic getUnitStatic(short name) {
        if (unitStatic.containsKey(name)) {
            return unitStatic.get(name);
        }

        return null;
    }

    public RmsSkillBalance getSkillBalance(short skillType, int level) {
        if (!skillBalance.containsKey(skillType)) {
            return null;
        }

        return skillBalance.get(skillType).get(level);
    }

    public int getHeroExpGet(byte type) {
        return heroExpGet.get(type).exp();
    }

    public int getHeroExpUp(int level) {
        return heroExpUp.get(level).exp();
    }

    public RmsAbnormalBalance getAbnormalBalance(int key) {
        return abnormalBalance.get(key);
    }

    public int getRevivalTime(int level) {
        return heroRevival.get(level).sec();
    }

    public int getMaxHeroLevel() {
        return kMaxHeroLevel;
    }

    public RmsVariable getVariable() {
        return metaSet.data.variable();
    }

    public Version getMinClientVersion() {
        return minClientVersion;
    }

    public int getExp(int level) {
        if (exp4LevelUp.containsKey(level)) {
            return exp4LevelUp.get(level).exp();
        }

        return 0;
    }

    public int calculateExp(int level, double rate) {
        if (exp4LevelUp.containsKey(level)) {
            return (int)Math.round(exp4LevelUp.get(level).exp() * rate);
        }

        return 0;
    }

    public double calculateExpRate(int level, int exp) {
        if (exp4LevelUp.containsKey(level)) {
            return exp / (double)exp4LevelUp.get(level).exp();
        }

        return 0.0;
    }

    public RmsLeagueBalance getLeagueBalance(int league) {
        if (leagueBalance.containsKey(league)) {
            return leagueBalance.get(league);
        }

        return null;
    }

    public AnimationInfo getAnimationInfo(short name) {
        if (animationInfo.containsKey(name)) {
            return animationInfo.get(name);
        }

        return null;
    }
}
