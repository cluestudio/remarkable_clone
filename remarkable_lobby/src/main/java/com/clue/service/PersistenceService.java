package com.clue.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.sql.ResultSet;
import io.vertx.redis.RedisClient;

public interface PersistenceService extends ServiceBase {
    int getDataShardCount();
    int getPlaylogsShardCount();
    void queryAtMeta(String sql, Handler<AsyncResult<ResultSet>> resultHandler);
    void queryAtData(int shardNo, String sql, Handler<AsyncResult<ResultSet>> resultHandler);
    void queryAtPlaylogs(int shardNo, String sql, Handler<AsyncResult<ResultSet>> resultHandler);
    String getDataDBName(int shardNo) throws Exception;
    String getPlaylogDBName(int shardNo) throws Exception;
    String getMetaDBName();
    RedisClient getRedis();
}
