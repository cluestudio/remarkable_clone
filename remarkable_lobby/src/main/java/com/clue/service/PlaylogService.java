package com.clue.service;

import com.clue.model.Playlog;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

public interface PlaylogService extends ServiceBase {
    void record(Playlog log, Handler<AsyncResult<Playlog>> handler);
    void getPlaylogs(String uid, long from, int count, Handler<AsyncResult<List<Playlog>>> handler);
}
