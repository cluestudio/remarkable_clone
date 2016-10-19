package com.clue.service;

import io.vertx.core.Vertx;

public interface ServiceBase {
    boolean isReady();
    void initialize(Vertx vertx);
}
