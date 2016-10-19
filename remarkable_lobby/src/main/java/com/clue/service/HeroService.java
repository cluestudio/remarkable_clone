package com.clue.service;

import com.clue.model.Hero;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.ArrayList;

public interface HeroService extends ServiceBase {
    void newHero(String accountId, Hero hero, Handler<AsyncResult<Hero>> handler);
    void getHero(String accountId, short name, Handler<AsyncResult<Hero>> handler);
    void getHeroes(String accountId, Handler<AsyncResult<ArrayList<Hero>>> handler);
}
