package com.clue.model;

import io.vertx.core.AsyncResult;

public class AsyncResultError<T> implements AsyncResult<T> {
    Throwable error = null;
    public AsyncResultError(Throwable error) {
        this.error = error;
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
