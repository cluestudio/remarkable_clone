package com.clue.service;

import com.clue.fbs.RmResultCode;
import com.clue.mapper.ConfigMapper;
import com.clue.model.AsyncResultError;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.HashMap;

public class PersistenceServiceImpl implements PersistenceService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private HashMap<Integer, JsonObject> dataDBOpt = new HashMap<>();
    private HashMap<Integer, JsonObject> playlogDBOpt = new HashMap<>();
    private HashMap<Integer, String> dataDBNames = new HashMap<>();
    private HashMap<Integer, String> playlogDBNames = new HashMap<>();
    private JsonObject configDBOpt = null;
    private JsonObject metaDBOpt = null;
    private String configDbName = "";
    private String metaDBName = "";
    private int lastConfigNo = 0;
    private RedisClient redis = null;
    private boolean ready = false;

    public boolean isReady() {
        return ready;
    }

    private AsyncSQLClient configDB() {
        return MySQLClient.createShared(Service.vertx, configDBOpt);
    }

    private AsyncSQLClient metaDB() {
        return MySQLClient.createShared(Service.vertx, metaDBOpt);
    }

    private AsyncSQLClient dataDB(int shardNo) {
        return MySQLClient.createShared(Service.vertx, dataDBOpt.get(shardNo));
    }

    private AsyncSQLClient playlogDB(int shardNo) {
        return MySQLClient.createShared(Service.vertx, playlogDBOpt.get(shardNo));
    }

    public void initialize(Vertx vertx) {
        String staging = Service.staging.toString();

        // initialize db
        String dbJson = vertx.fileSystem().readFileBlocking("config/db_" + staging + ".json").toString();
        configDBOpt = new JsonObject(dbJson);
        configDbName = configDBOpt.getString("database");
        updateDatabase(vertx);

        // initialize redis
        String redisJson = vertx.fileSystem().readFileBlocking("config/redis_" + staging + ".json").toString();
        JsonObject redisOpt = new JsonObject(redisJson);
        RedisOptions option = new RedisOptions();
        option.setHost(redisOpt.getString("host"));
        option.setPort(redisOpt.getInteger("port"));
        option.setAuth(redisOpt.getString("auth"));
        redis = RedisClient.create(vertx, option);
    }

    private void updateDatabase(Vertx vertx) {
        ConfigMapper configMapper = new ConfigMapper();
        String configDbName = configDBOpt.getString("database");
        queryAtConfig(configMapper.getDatabases(configDbName), res -> {
            if (res.succeeded()) {
                parseDatabase(vertx, res.result());
                ready = true;
            }

            vertx.setTimer(5000, id -> {
                updateDatabase(vertx);
            });
        });
    }

    private void parseDatabase(Vertx vertx, ResultSet resultSet) {
        if (resultSet.getNumRows() == 0) {
            logger.error("there is no config for database in release table.");
            return;
        }

        JsonObject result = resultSet.getRows().get(0);
        int no = result.getInteger("no");
        if (no == lastConfigNo) {
            return;
        }

        logger.info("update db connection");
        String json = result.getString("data");
        JsonArray configList = new JsonArray(json);

        for (int index = 0; index < configList.size(); index++) {
            JsonObject config = configList.getJsonObject(index);

            if (config.getString("type").equals("meta")) {
                initializeMetaDB(vertx, config);
            }
            else if (config.getString("type").equals("data")) {
                initializeDataDB(vertx, config);
            }
            else if (config.getString("type").equals("playlogs")) {
                initializePlaylogsDB(vertx, config);
            }
        }

        lastConfigNo = no;
    }

    private void initializePlaylogsDB(Vertx vertx, JsonObject config) {
        Integer shardNo = config.getInteger("shardNo");
        String name = config.getString("database");

        playlogDBNames.put(shardNo, name);
        playlogDBOpt.put(shardNo, config);
    }

    private void initializeDataDB(Vertx vertx, JsonObject config) {
        Integer shardNo = config.getInteger("shardNo");
        String name = config.getString("database");

        dataDBNames.put(shardNo, name);
        dataDBOpt.put(shardNo, config);
    }

    private void initializeMetaDB(Vertx vertx, JsonObject config) {
        metaDBName = config.getString("database");
        metaDBOpt = config;
    }

    public int getDataShardCount() {
        return dataDBOpt.size();
    }

    public int getPlaylogsShardCount() {
        return playlogDBOpt.size();
    }

    public void queryAtData(int shardNo, String sql, Handler<AsyncResult<ResultSet>> resultHandler) {
        if (shardNo < 0 || shardNo >= dataDBOpt.size()) {
            resultHandler.handle(new AsyncResultError<>(RmResultCode.InternalError, "invalid shard no"));
            return;
        }

        queryAt(dataDB(shardNo), sql, true, resultHandler);
    }

    public void updateAtData(int shardNo, String sql, Handler<AsyncResult<UpdateResult>> resultHandler) {
        if (shardNo < 0 || shardNo >= dataDBOpt.size()) {
            resultHandler.handle(new AsyncResultError<>(RmResultCode.InternalError, "invalid shard no"));
            return;
        }

        updateAt(dataDB(shardNo), sql, true, resultHandler);
    }

    public void queryAtPlaylogs(int shardNo, String sql, Handler<AsyncResult<ResultSet>> resultHandler) {
        if (shardNo < 0 || shardNo >= playlogDBOpt.size()) {
            resultHandler.handle(new AsyncResultError<>(RmResultCode.InternalError, "invalid shard no"));
            return;
        }

        queryAt(playlogDB(shardNo), sql, true, resultHandler);
    }

    public void queryAtMeta(String sql, Handler<AsyncResult<ResultSet>> resultHandler) {
        queryAt(metaDB(), sql, false, resultHandler);
    }

    public String getDataDBName(int shardNo) throws Exception {
        if (shardNo < 0 || shardNo >= dataDBNames.size()) {
            throw new Exception("invalid shard no");
        }

        return dataDBNames.get(shardNo);
    }

    public String getPlaylogDBName(int shardNo) throws Exception {
        if (shardNo < 0 || shardNo >= playlogDBNames.size()) {
            throw new Exception("invalid shard no");
        }

        return playlogDBNames.get(shardNo);
    }

    public String getMetaDBName() {
        return metaDBName;
    }

    public RedisClient getRedis() {
        return redis;
    }

    void queryAtConfig(String sql, Handler<AsyncResult<ResultSet>> resultHandler) {
        queryAt(configDB(), sql, false, resultHandler);
    }

    void queryAt(AsyncSQLClient db, String sql, boolean needLog, Handler<AsyncResult<ResultSet>> resultHandler) {
        if (needLog) {
            logger.info(sql);
        }

        db.getConnection(res -> {
            if (res.succeeded()) {
                SQLConnection connection = res.result();
                connection.query(sql, resultSetAsyncResult-> {
                    resultHandler.handle(resultSetAsyncResult);
                    connection.close();
                });


            } else {
                logger.error("get connection failed:" + res.cause().toString());
                resultHandler.handle(new AsyncResultError<>(RmResultCode.DBError, res.cause().toString()));
            }
        });
    }

    void updateAt(AsyncSQLClient db, String sql, boolean needLog, Handler<AsyncResult<UpdateResult>> resultHandler) {
        if (needLog) {
            logger.info(sql);
        }

        db.getConnection(res -> {
            if (res.succeeded()) {
                SQLConnection connection = res.result();
                connection.update(sql, resultSetAsyncResult-> {
                    resultHandler.handle(resultSetAsyncResult);
                    connection.close();
                });


            } else {
                logger.error("get connection failed:" + res.cause().toString());
                resultHandler.handle(new AsyncResultError<>(RmResultCode.DBError, res.cause().toString()));
            }
        });
    }
}
