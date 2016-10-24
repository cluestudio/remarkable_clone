package com.clue.model;

import io.vertx.core.AsyncResult;

public class AsyncResultError<T> implements AsyncResult<T> {
    int resultCode;
    Throwable error = null;

    public AsyncResultError(int resultCode, String errorMessage) {
        this.error = new Throwable(errorMessage);
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    @Override
    public T result() {
        return null;
    }

    @Override
    public Throwable cause() {
        return error;
    }

    @Override
    public boolean succeeded() {
        return false;
    }

    @Override
    public boolean failed() {
        return true;
    }
}
