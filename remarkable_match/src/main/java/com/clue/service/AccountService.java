package com.clue.service;

import com.clue.model.Account;
import com.clue.model.Assets;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface AccountService extends ServiceBase {
    void newAccount(String uid, String country, Handler<AsyncResult<Account>> resultHandler);
    void updateAccount(Account account, Handler<AsyncResult<Account>> handler);
    void getAccount(String uid, Handler<AsyncResult<Account>> resultHandler);
    void newAssets(Assets asset, Handler<AsyncResult<Assets>> resultHandler);
    void getAssets(String uid, Handler<AsyncResult<Assets>> resultHandler);
    void updateAsset(String uid, Assets assets, Handler<AsyncResult<Assets>> handler);
    void increaseAsset(String uid, int gold, int ruby, Handler<AsyncResult<Boolean>> handler);
}
