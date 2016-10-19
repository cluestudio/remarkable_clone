package com.clue.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.sql.ResultSet;
import io.vertx.redis.RedisClient;

public interface PersistenceService extends ServiceBase {
    int getDataShardCount();
    int getPlaylogsShardCount();
    void queryAtConfig(String sql, Handler<AsyncResult<ResultSet>> resultHandler);
    void queryAtData(int shardNo, String sql, Handler<AsyncResult<ResultSet>> resultHandler);
    void queryAtMeta(String sql, Handler<AsyncResult<ResultSet>> resultHandler);
    void queryAtPlaylogs(int shardNo, String sql, Handler<AsyncResult<ResultSet>> resultHandler);
    String getDataDBName(int shardNo);
    String getPlaylogDBName(int shardNo);
    String getMetaDBName();
    String getConfigDBName();
    RedisClient getRedis();
}
