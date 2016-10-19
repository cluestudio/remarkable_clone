package com.clue.model;

import com.clue.fbs.RmmAbnormal;

public class Abnormal {
    byte type;
    short at;
    short duration;
    short value;
    short from;
    short to;

    public Abnormal() {
    }

    public Abnormal(RmmAbnormal rhs) {
        from(rhs);
    }

    public Abnormal(Abnormal rhs) {
        from(rhs);
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public short getAt() {
        return at;
    }

    public void setAt(short at) {
        this.at = at;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public short getFrom() {
        return from;
    }

    public void setFrom(short from) {
        this.from = from;
    }

    public short getTo() {
        return to;
    }

    public void setTo(short to) {
        this.to = to;
    }

    public void from(RmmAbnormal rhs) {
        this.type = rhs.type();
        this.at = rhs.at();
        this.duration = rhs.duration();
        this.value = rhs.value();
        this.from = rhs.from();
        this.to = rhs.to();
    }

    public void from(Abnormal rhs) {
        this.type = rhs.getType();
        this.at = rhs.getAt();
        this.duration = rhs.getDuration();
        this.value = rhs.getValue();
        this.from = rhs.getFrom();
        this.to = rhs.getTo();
    }

    public boolean isExpired(float playTime) {
        return ((at + duration) < playTime);
    }
}
