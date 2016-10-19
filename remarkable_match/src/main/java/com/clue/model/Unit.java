package com.clue.model;

import com.clue.fbs.*;
import com.google.flatbuffers.FlatBufferBuilder;

import java.util.HashSet;

public class Unit {
    public static final int kActorStateAppear = 1;
    public static final int kActorStay = 2;
    public static final int kActorIdle = 3;
    public static final int kActorRun = 4;
    public static final int kActorAttack = 5;
    public static final int kActorDamage = 6;
    public static final int kActorSkillStart = 10;
    public static final int kActorSkill1 = 11;
    public static final int kActorSkill2 = 12;
    public static final int kActorSkill3 = 13;
    public static final int kActorSpellStart = 21;
    public static final int kActorFlash = 20;
    public static final int kActorDead = 127;

    short key;
    short name;
    byte level;
    short exp;
    short hp;
    byte type;
    short target;
    Vector2 position = new Vector2();
    Vector2b direction = new Vector2b();
    Vector2 to = new Vector2();
    short skill1FireSec;
    short skill2FireSec;
    short skill3FireSec;
    short skillSpecialFireSec;
    boolean expDirty = false;

    byte state;
    HashSet<Abnormal> abnormalList = new HashSet<Abnormal>();

    public Unit() {
    }

    public Unit(RmmUnit unit) {
        from(unit);
    }

    public short getName() {
        return name;
    }

    public void setName(short name) {
        this.name = name;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public void addLevel(byte level) {
        this.level += level;
        setExpDirty(true);
    }

    public short getExp() {
        return exp;
    }

    public void setExp(short exp) {
        this.exp = exp;
    }

    public void addExp(short exp) {
        this.exp += exp;
    }

    public boolean isExpDirty() {
        return expDirty;
    }

    public void setExpDirty(boolean expDirty) {
        this.expDirty = expDirty;
    }

    public short getHp() {
        return hp;
    }

    public void setHp(short hp) {
        this.hp = hp;
    }

    public void reduceHp(short hp) {
        this.hp -= hp;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(RmmVector2 position) {
        this.position.x = position.x();
        this.position.z = position.z();
    }

    public Vector2b getDirection() {
        return direction;
    }

    public void setDirection(RmmVector2b direction) {
        this.direction.x = direction.x();
        this.direction.z = direction.z();
    }

    public short getKey() {
        return key;
    }

    public void setKey(short key) {
        this.key = key;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public void setPosition(Vector2 position) {
        this.position.setX(position.getX());
        this.position.setZ(position.getZ());
    }

    public void setDirection(Vector2b direction) {
        this.direction.setX(direction.getX());
        this.direction.setZ(direction.getZ());
    }

    public Vector2 getTo() {
        return to;
    }

    public void setTo(Vector2 to) {
        this.to.setX(to.getX());
        this.to.setZ(to.getZ());
    }

    public void setTo(RmmVector2 to) {
        this.to.setX(to.x());
        this.to.setZ(to.z());
    }

    public byte getPlayerNo() {
        return getPlayerNo(key);
    }

    public int getUnitNo() {
        return getUnitNo(key);
    }

    public void setKey(byte playerNo, int unitNo) {
        this.key = makeKey(playerNo, unitNo);
    }

    static public short makeKey(byte playerNo, int unitNo) {
        return (short)((unitNo * 10) + playerNo);
    }

    static public byte getPlayerNo(int key) {
        return (byte)(key % 10);
    }

    static public int getUnitNo(int key) {
        return (key / 10);
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public short getSkill1FireSec() {
        return skill1FireSec;
    }

    public void setSkill1FireSec(short skill1FireSec) {
        this.skill1FireSec = skill1FireSec;
    }

    public short getSkill2FireSec() {
        return skill2FireSec;
    }

    public void setSkill2FireSec(short skill2FireSec) {
        this.skill2FireSec = skill2FireSec;
    }

    public short getSkill3FireSec() {
        return skill3FireSec;
    }

    public void setSkill3FireSec(short skill3FireSec) {
        this.skill3FireSec = skill3FireSec;
    }

    public short getSkillSpecialFireSec() {
        return skillSpecialFireSec;
    }

    public void setSkillSpecialFireSec(short skillSpecialFireSec) {
        this.skillSpecialFireSec = skillSpecialFireSec;
    }

    public HashSet<Abnormal> getAbnormalList() {
        return abnormalList;
    }

    public short getTarget() {
        return target;
    }

    public void setTarget(short target) {
        this.target = target;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void from(RmmUnit unit) {
        key = unit.key();
        hp = unit.hp();
        level = unit.level();
        exp = unit.exp();
        name = unit.name();
        type = unit.type();
        skill1FireSec = unit.skill1FireSec();
        skill2FireSec = unit.skill2FireSec();
        skill3FireSec = unit.skill3FireSec();
        skillSpecialFireSec = unit.skillSpecialFireSec();

        if (unit.position() != null) {
            position.x = unit.position().x();
            position.z = unit.position().z();
        }

        if (unit.direction() != null) {
            direction.x = unit.direction().x();
            direction.z = unit.direction().z();
        }

        abnormalList.clear();
        for (int index = 0; index < unit.abnormalListLength(); index++) {
            abnormalList.add(new Abnormal(unit.abnormalList(index)));
        }
    }

    public void reverseAll() {
        position.reverse();
        direction.reverse();
        to.reverse();
    }

    public void from(Unit unit) {
        key = unit.getKey();
        hp = unit.getHp();
        level = unit.getLevel();
        exp = unit.getExp();
        name = unit.getName();
        type = unit.getType();
        position.x = unit.getPosition().x;
        position.z = unit.getPosition().z;
        direction.x = unit.getDirection().x;
        direction.z = unit.getDirection().z;
        skill1FireSec = unit.getSkill1FireSec();
        skill2FireSec = unit.getSkill2FireSec();
        skill3FireSec = unit.getSkill3FireSec();
        skillSpecialFireSec = unit.getSkillSpecialFireSec();

        abnormalList.clear();
        for (Abnormal abnormal : getAbnormalList()) {
            abnormalList.add(new Abnormal(abnormal));
        }
    }

    public void syncAbnormal(Abnormal abnormal) {
        abnormalList.add(abnormal);
    }

    public void syncOrder(RmmUnitOrder order, short playTime) {
        setDirection(order.direction());
        setPosition(order.position());
        setTo(order.to());
        state = order.state();
        target = order.target();

        if (state == kActorSkill1) {
            setSkill1FireSec(playTime);
        }
        else if (state == kActorSkill2) {
            setSkill2FireSec(playTime);
        }
        else if (state == kActorSkill3) {
            setSkill3FireSec(playTime);
        }
        else if (state > kActorSpellStart) {
            setSkillSpecialFireSec(playTime);
        }
    }

    public void addOrder(FlatBufferBuilder builder) {
        RmmUnitOrder.createRmmUnitOrder(builder,
                key,
                target,
                position.x,
                position.z,
                to.x,
                to.z,
                direction.x,
                direction.z,
                state
                );
    }
}


