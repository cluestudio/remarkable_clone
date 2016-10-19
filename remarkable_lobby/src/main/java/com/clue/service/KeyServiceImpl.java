package com.clue.service;

import io.vertx.core.Vertx;

import java.util.Arrays;

public class KeyServiceImpl implements KeyService {
    private final int radix = 36;
    private final long timeOffset = 1451606400000L; // 2016.1.1 00:00
    private final long weekOffset = 1474855200000L;
    private final long dataDBShardMask = 0x1f;
    private final int dataDBShardSize = 5;
    private final int dataDBSeqSize = 10;
    private final int playlogDBShardSize = 5;
    private final int playlogDBSeqSize = 7;
    private final int playlogDBTableSize = 3;
    private final long playlogDBShardMask = 0x1f;
    private final long playlogDBTableMask = 0x7;
    private final int playlogBucketSize = 4;

    private int dataSeqNo = 0;
    private int playlogSeqNo = 0;

    public boolean isReady() {
        return true;
    }

    public void initialize(Vertx vertx) {
    }

    public String newDataDBKey() {
        final int maxSeqNo = (1 << dataDBSeqSize);
        final int keyLength = 12;

        int shardNo = dataSeqNo % Service.ps.getDataShardCount();
        long milliSec = System.currentTimeMillis() - timeOffset;

        long longKey = dataSeqNo;
        longKey |= (shardNo << dataDBSeqSize);
        longKey |= (milliSec << (dataDBSeqSize + dataDBShardSize));

        String key = Long.toString(longKey, radix);
        dataSeqNo = (dataSeqNo + 1) % maxSeqNo;
        return paddingKey(key, keyLength);
    }

    public String newPlaylogDBKey() {
        final int maxSeqNo = (1 << playlogDBSeqSize);
        final int keyLength = 12;

        int shardNo = playlogSeqNo % Service.ps.getPlaylogsShardCount();
        int tableNo = getPlaylogCurrentTableNo();
        long milliSec = System.currentTimeMillis() - timeOffset;

        long longKey = playlogSeqNo;
        longKey |= (tableNo << (playlogDBSeqSize));
        longKey |= (shardNo << (playlogDBSeqSize + playlogDBTableSize));
        longKey |= (milliSec << (playlogDBSeqSize + playlogDBTableSize + playlogDBShardSize));

        String key = Long.toString(longKey, radix);
        playlogSeqNo = (playlogSeqNo + 1) % maxSeqNo;
        return paddingKey(key, keyLength);
    }

    public int getDataDBShardNo(String key) {
        long longKey = Long.parseLong(key, radix);
        longKey >>= dataDBSeqSize;
        return (int)(longKey & dataDBShardMask);
    }

    public int getPlaylogDBShardNo(String key) {
        long longKey = Long.parseLong(key, radix);
        longKey >>= (playlogDBSeqSize + playlogDBTableSize);
        return (int)(longKey & playlogDBShardMask);
    }

    public int getPlaylogDBTableNo(String key) {
        long longKey = Long.parseLong(key, radix);
        longKey >>= (playlogDBSeqSize);
        return (int)(longKey & playlogDBTableMask);
    }

    public int getPlaylogBucketSize() {
        return playlogBucketSize;
    }

    public int getPlaylogCurrentTableNo() {
        long time = (System.currentTimeMillis() - weekOffset) / 1000;
        return ((int)(time / (3600 * 24 * 7)) % playlogBucketSize);
    }

    private String paddingKey(String key, int length) {
        if (key.length() < length) {
            char[] repeat = new char[length - key.length()];
            Arrays.fill(repeat, '0');
            key = new String(repeat) + key;
        }
        return key;
    }
}
