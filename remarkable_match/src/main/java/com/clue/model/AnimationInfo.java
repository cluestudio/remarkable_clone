package com.clue.model;

import com.clue.fbs.RmsAnimationInfo;

public class AnimationInfo {
    public short name;
    public float attackLength;
    public float skill1Length;
    public float skill2Length;
    public float skill3Length;
    public byte attackCount;
    public byte skill1Count;
    public byte skill2Count;
    public byte skill3Count;

    public AnimationInfo(RmsAnimationInfo rhs) {
        name = rhs.name();
        attackLength = rhs.attackLength();
        skill1Length = rhs.skill1Length();
        skill2Length = rhs.skill2Length();
        skill3Length = rhs.skill3Length();
        attackCount = rhs.attackCount();
        skill1Count = rhs.skill1Count();
        skill2Count = rhs.skill2Count();
        skill3Count = rhs.skill3Count();
    }
}
