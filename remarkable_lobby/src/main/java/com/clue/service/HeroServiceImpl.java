package com.clue.service;

import com.clue.util.DebugUtil;
import com.clue.util.VoidHandler;
import com.clue.mapper.HeroMapper;
import com.clue.model.AsyncResultError;
import com.clue.model.AsyncResultSuccess;
import com.clue.model.Hero;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;

public class HeroServiceImpl implements HeroService {
    Logger logger = LoggerFactory.getLogger(getClass());
    HeroMapper mapper = new HeroMapper();

    public boolean isReady() {
        return true;
    }

    public void initialize(Vertx vertx) {
    }

    public void newHero(String uid, Hero hero, Handler<AsyncResult<Hero>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.newHeroQuery(db, uid, hero), res -> {
                run(handler, res, () -> {
                    handler.handle(new AsyncResultSuccess<>(hero));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(new Throwable(e.toString())));
        }
    }

    public void getHero(String uid, short name, Handler<AsyncResult<Hero>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.getHeroQuery(db, uid, name), res -> {
                run(handler, res, () -> {
                    Hero hero = mapper.parseHero(res.result());
                    handler.handle(new AsyncResultSuccess<>(hero));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(new Throwable(e.toString())));
        }
    }

    public void getHeroes(String uid, Handler<AsyncResult<ArrayList<Hero>>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.getHeroesQuery(db, uid), res -> {
                run(handler, res, () -> {
                    ArrayList<Hero> heroes = mapper.parseHeroes(res.result());
                    handler.handle(new AsyncResultSuccess<>(heroes));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(new Throwable(e.toString())));
        }
    }

    void run(Handler handler, AsyncResult result, VoidHandler runCallback) {
        if (result.failed()) {
            logger.error(result.cause());
            handler.handle(new AsyncResultError<>(result.cause()));
            return;
        }

        try {
            runCallback.handle();
        }
        catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
        }
    }
}
