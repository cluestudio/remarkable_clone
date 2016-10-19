package com.clue.service;

import com.clue.mapper.PlaylogMapper;
import com.clue.model.AsyncResultError;
import com.clue.model.AsyncResultSuccess;
import com.clue.model.Playlog;
import com.clue.util.DebugUtil;
import com.clue.util.VoidHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

import java.util.ArrayList;
import java.util.List;

public class PlaylogServiceImpl implements PlaylogService {
    private final int weekOffset = 1474855200;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PlaylogMapper mapper = new PlaylogMapper();

    public boolean isReady() {
        return true;
    }

    public void initialize(Vertx vertx) {
    }

    int getCurrentTableNo() {
        long time = System.currentTimeMillis() / 1000;
        long diff = time - weekOffset;
        return (int)(diff / (3600 * 24 * 7));
    }

    byte[] compress(byte[] data) {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        final int decompressedLength = data.length;

        LZ4Compressor compressor = factory.fastCompressor();
        int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
        byte[] buffer = new byte[maxCompressedLength];
        int compressedLength = compressor.compress(data, 0, decompressedLength, buffer, 0, maxCompressedLength);

        byte[] compressed = new byte[compressedLength];
        System.arraycopy(buffer, 0, compressed, 0, compressedLength);

        logger.error("compress rate:" + (compressedLength / (float)decompressedLength));
        return compressed;
    }

    public void record(Playlog log, Handler<AsyncResult<Playlog>> handler) {
        Service.vertx.executeBlocking(future -> {
            log.raw = compress(log.raw);
            future.complete(log);
        }, result -> {
            try {
                int shardNo = Service.key.getPlaylogDBShardNo(log.pid);
                String db = Service.ps.getPlaylogDBName(shardNo);
                String sql = mapper.newLog(db, getCurrentTableNo(), log);

                Service.ps.queryAtPlaylogs(shardNo, sql, res -> {
                    run(handler, res, () -> {
                        handler.handle(new AsyncResultSuccess<>(log));
                    });
                });
            } catch (Exception e) {
                logger.error(DebugUtil.getStackTrace(e));
            }
        });
    }

    public void getPlaylogs(String uid, long from, int count, Handler<AsyncResult<List<Playlog>>> handler) {
        int dbCount = Service.ps.getPlaylogsShardCount();
        List<Playlog> playlogs = new ArrayList<>();
        List<Integer> doneDBs = new ArrayList<>();

        try {
            for (int index = 0; index < dbCount; index++) {
                String db = Service.ps.getPlaylogDBName(index);
                String sql = mapper.getAllLogs(db, uid, from, 20);

                Service.ps.queryAtPlaylogs(index, sql, res -> {
                    run(handler, res, () -> {
                        mapper.fetchPlaylogs(res.result(), playlogs);
                        doneDBs.add(res.result().getNumRows());
                        if (doneDBs.size() == Service.ps.getPlaylogsShardCount()) {
                            playlogs.sort((log1, log2) -> (int) (log1.birth - log2.birth));
                            int lastIndex = Math.min(count, playlogs.size());
                            handler.handle(new AsyncResultSuccess<>(playlogs.subList(0, lastIndex)));
                        }
                    });
                });
            }
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
        }
    }

    void run(Handler handler, AsyncResult result, VoidHandler runCallback) {
        if (result.failed()) {
            logger.error(result.cause());
            handler.handle(new AsyncResultError<>(result.cause()));
            return;
        }

        try {
            runCallback.handle();
        } catch (Exception e) {
            logger.error(DebugUtil.getStackTrace(e));
        }
    }
}
