package com.clue.service;

import com.clue.model.Hero;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.ArrayList;

public interface HeroService extends ServiceBase {
    void newHero(String uid, Hero hero, Handler<AsyncResult<Hero>> handler);
    void getHero(String uid, short name, Handler<AsyncResult<Hero>> handler);
    void getHeroes(String uid, Handler<AsyncResult<ArrayList<Hero>>> handler);
    void useHero(String uid, short name, long timestamp, Handler<AsyncResult<Long>> handler);
}
