package com.clue.service;

import com.clue.fbs.*;
import com.clue.model.AnimationInfo;
import com.clue.util.Version;

public interface MetaService extends ServiceBase {
    int getVersion();
    RmsMetaSet getMetaSet();
    RmsUnitStatic getUnitStatic(short name);
    RmsUnitBalance getUnitBalance(short name, int level);
    RmsSkillBalance getSkillBalance(short skillType, int level);
    RmsAbnormalBalance getAbnormalBalance(int key);
    RmsLeagueBalance getLeagueBalance(int league);
    AnimationInfo getAnimationInfo(short name);
    int getHeroExpGet(byte type);
    int getHeroExpUp(int level);
    int getRevivalTime(int level);
    int getMaxHeroLevel();
    RmsVariable getVariable();
    Version getMinClientVersion();
    int getExp(int level);
    int calculateExp(int level, double rate);
    double calculateExpRate(int level, int exp);
}
