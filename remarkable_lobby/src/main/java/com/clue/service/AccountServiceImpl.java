package com.clue.service;

import com.clue.fbs.RmResultCode;
import com.clue.util.DebugUtil;
import com.clue.util.VoidHandler;
import com.clue.mapper.AccountMapper;
import com.clue.model.Account;
import com.clue.model.Assets;
import com.clue.model.AsyncResultError;
import com.clue.model.AsyncResultSuccess;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Random;

public class AccountServiceImpl implements AccountService {
    Logger logger = LoggerFactory.getLogger(getClass());
    Random random = new Random();
    AccountMapper mapper = new AccountMapper();

    public boolean isReady() {
        return true;
    }

    public void initialize(Vertx vertx) {
    }

    public void newAccount(String uid, String country, Handler<AsyncResult<Account>> handler) {
        int token = randomToken();
        long birth = System.currentTimeMillis() / 1000L;

        Account account = new Account();
        account.uid = uid;
        account.country = country;
        account.token = token;
        account.birth = birth;
        account.level = 1;
        account.exp = 0;

        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.newAccountQuery(db, account), res -> {
                run(handler, res, () -> {
                    logger.info("new account created: " + account.uid);
                    handler.handle(new AsyncResultSuccess<>(account));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
        }
    }

    public void updateAccount(Account account, Handler<AsyncResult<Account>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(account.uid);
            logger.error(shardNo);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.updateAccount(db, account), res -> {
                run(handler, res, () -> {
                    handler.handle(new AsyncResultSuccess<>(account));
                });
            });
        }
        catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
        }
    }

    public void getAccount(String uid, Handler<AsyncResult<Account>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);
            Service.ps.queryAtData(shardNo, mapper.getAccountQuery(db, uid), res -> {
                run(handler, res, () -> {
                    Account account = mapper.parseAccount(res.result());
                    handler.handle(new AsyncResultSuccess<>(account));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(RmResultCode.InternalError, e.toString()));
        }
    }

    public void newAssets(Assets assets, Handler<AsyncResult<Assets>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(assets.uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.newAssetsQuery(db, assets), res -> {
                run(handler, res, () -> {
                    handler.handle(new AsyncResultSuccess<>(assets));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(RmResultCode.InternalError, e.toString()));
        }
    }

    public void getAssets(String uid, Handler<AsyncResult<Assets>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.getAssetsQuery(db, uid), res -> {
                run(handler, res, () -> {
                    Assets assets = mapper.parseAssets(res.result());
                    handler.handle(new AsyncResultSuccess<>(assets));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(RmResultCode.InternalError, e.toString()));
        }
    }

    public void updateAsset(String uid, Assets assets, Handler<AsyncResult<Assets>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.updateAssetQuery(db, uid, assets), res -> {
                run(handler, res, () -> {
                    handler.handle(new AsyncResultSuccess<>(assets));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(RmResultCode.InternalError, e.toString()));
        }
    }

    public void increaseAsset(String uid, int gold, int ruby, Handler<AsyncResult<Boolean>> handler) {
        try {
            int shardNo = Service.key.getDataDBShardNo(uid);
            String db = Service.ps.getDataDBName(shardNo);

            Service.ps.queryAtData(shardNo, mapper.increseAssetQuery(db, uid, gold, ruby), res -> {
                run(handler, res, () -> {
                    handler.handle(new AsyncResultSuccess<>(true));
                });
            });
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
            handler.handle(new AsyncResultError<>(RmResultCode.InternalError, e.toString()));
        }
    }

    int randomToken() {
        return random.nextInt(1000);
    }

    void run(Handler handler, AsyncResult result, VoidHandler runCallback) {
        if (result.failed()) {
            logger.error(result.cause());
            handler.handle(new AsyncResultError<>(RmResultCode.DBError, result.cause().toString()));
            return;
        }

        try {
            runCallback.handle();
        }
        catch (Exception e) {
            handler.handle(new AsyncResultError<>(RmResultCode.InternalError, result.cause().toString()));
            logger.error(DebugUtil.getStackTrace(e));
        }
    }
}
