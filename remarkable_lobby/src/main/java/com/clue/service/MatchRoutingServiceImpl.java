package com.clue.service;

import com.clue.model.MatchServerInfo;
import com.clue.model.Staging;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.op.RangeLimitOptions;

import java.util.HashMap;

public class MatchRoutingServiceImpl implements MatchRoutingService {
    final private long watchDogTimeout = 10;
    final private long pollingInterval = 5 * 1000;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private HashMap<String, MatchServerInfo> servers = new HashMap<>();

    public boolean isReady() {
        return true;
    }

    public void initialize(Vertx vertx) {
        refreshMatchServers();
    }

    public String routeMatchServer(String region, byte league) {
        if (Service.staging == Staging.dev) {
            return "ws://localhost:8040/match";
        }

        for (MatchServerInfo info : servers.values()) {
            // todo need implementation for smart routing
            return info.url;
        }
        return "";
    }

    private void refreshMatchServers() {
        Long sec = System.currentTimeMillis() / 1000;
        Long minSec = sec - watchDogTimeout;
        Service.ps.getRedis().zrangebyscore("rm_match_servers", minSec.toString(), "+inf", RangeLimitOptions.NONE, res-> {
            if (res.succeeded()) {
                servers.clear();
                JsonArray dataSet = res.result();
                for (int index = 0; index < dataSet.size(); index++) {
                    String name = dataSet.getString(index);
                    MatchServerInfo info = new MatchServerInfo();
                    info.url = name;
                    servers.put(name, info);
                    logger.error(dataSet.getString(index));
                }
            }

            Service.vertx.setTimer(pollingInterval, id -> {
                refreshMatchServers();
            });
        });
    }
}
