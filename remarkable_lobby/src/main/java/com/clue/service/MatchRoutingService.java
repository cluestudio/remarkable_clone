package com.clue.service;
import com.clue.model.Account;

public interface MatchRoutingService extends ServiceBase {
    String routeMatchServer(String region, byte league);
}
