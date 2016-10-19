package com.clue.model;

import com.clue.fbs.RmAccount;
import io.vertx.core.http.ServerWebSocket;

public class Account {
    public ServerWebSocket socket;
    public String uid = "";
    public String name = "";
    public String profileUrl = "";
    public String facebook = "";
    public String google = "";
    public String gameCenter = "";
    public int token = 0;
    public long birth = 0;
    public int level = 0;
    public int exp = 0;
    public byte league = 0;
    public int leaguePoint = 0;
    public String country = "";
    public long lastPlayTime = 0;

    public Account() {
    }

    public Account(ServerWebSocket ws) {
        this.uid = ws.binaryHandlerID();
        this.socket = ws;
    }

    public void from(RmAccount account) {
        this.uid = account.uid();
        this.name = account.name();
        this.profileUrl = account.profileUrl();
        this.facebook = account.facebook();
        this.google = account.google();
        this.gameCenter = account.gameCenter();
        this.token = account.token();
        this.birth = account.birth();
        this.level = account.level();
        this.exp = account.exp();
        this.league = account.league();
        this.leaguePoint = account.leaguePoint();
        this.country = account.country();
    }
}
