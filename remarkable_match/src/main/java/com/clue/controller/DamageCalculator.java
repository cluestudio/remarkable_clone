package com.clue.controller;

import com.clue.fbs.RmsSkillBalance;
import com.clue.fbs.RmsUnitBalance;
import com.clue.fbs.RmsUnitStatic;
import com.clue.model.AnimationInfo;
import com.clue.model.Unit;
import com.clue.service.Service;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class DamageCalculator {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final int defenseBase = 1000;

    public short calculate(Unit from, Unit to, short skillId, int rate) {
        RmsUnitBalance myBalance = Service.meta.getUnitBalance(from.getName(), from.getLevel());
        RmsUnitBalance enemyBalance = Service.meta.getUnitBalance(to.getName(), to.getLevel());
        RmsUnitStatic unitInfo = Service.meta.getUnitStatic(from.getName());
        AnimationInfo aniInfo = Service.meta.getAnimationInfo(from.getName());

        float scalar = calculateAttackScalar(skillId, unitInfo, aniInfo, rate);
        if (skillId != 0) {
            RmsSkillBalance skillBalance = Service.meta.getSkillBalance(skillId, 1);
            scalar *= skillBalance.attackScalar();
        }

        float damagePhysical = 0;
        damagePhysical = myBalance.physicalAttack() * scalar;
        damagePhysical *= getReduceRate(enemyBalance.physicalDefense());

        float damageMagical = 0;
        damageMagical = myBalance.magicalAttack() * scalar;
        damageMagical *= getReduceRate(enemyBalance.magicalDefense());
        return (short)(damagePhysical + damageMagical);
    }

    private float calculateAttackScalar(short skillId, RmsUnitStatic unitInfo, AnimationInfo aniInfo, int rate) {
        int attackCount = 0;
        float aniLength = 0f;

        if (unitInfo == null) {
            logger.error("no unit static");
            return 0;
        }

        if (aniInfo == null) {
            logger.error("no ani info");
            return 0;
        }

        if (skillId == 0) {
            attackCount = aniInfo.attackCount;
            aniLength = aniInfo.attackLength;
        }
        else if (skillId == unitInfo.skill1()) {
            attackCount = aniInfo.skill1Count;
            aniLength = aniInfo.skill1Length;
        }
        else if (skillId == unitInfo.skill2()) {
            attackCount = aniInfo.skill2Count;
            aniLength = aniInfo.skill2Length;
        }
        else if (skillId == unitInfo.skill3()) {
            attackCount = aniInfo.skill3Count;
            aniLength = aniInfo.skill3Length;
        }

        float scalar = 0;
        if (attackCount > 0 && aniLength > 0f) {
            scalar = (1f / attackCount) * aniLength;
        }

        return scalar * rate;
    }

    public float getReduceRate(float defense) {
        defense = Math.max(defense, 0);
        defense = Math.min(defense, defenseBase);
        return defense / (defense + defenseBase);
    }
}
