package com.clue.service;

public interface KeyService extends ServiceBase {
    String newDataDBKey();
    String newPlaylogDBKey();
    int getDataDBShardNo(String key);
    int getPlaylogDBTableNo(String key);
    int getPlaylogDBShardNo(String key);
    int getPlaylogCurrentTableNo();
    int getPlaylogBucketSize();
}
