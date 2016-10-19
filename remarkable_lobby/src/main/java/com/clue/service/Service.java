package com.clue.service;

import com.clue.model.Staging;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;

public class Service extends Thread {
    public static Staging staging = Staging.beta;
    public static int port = 0;
    public static SessionService session = null;
    public static MetaService meta = null;
    public static PersistenceService ps = null;
    public static AccountService account = null;
    public static HeroService hero = null;
    public static KeyService key = null;
    public static GeoIPService geoIP = null;
    public static PlaylogService playlog = null;
    public static MatchRoutingService matchRouting = null;
    public static Vertx vertx = null;

    public void run() {
        initializeSerivce(session);
        initializeSerivce(ps);
        initializeSerivce(meta);
        initializeSerivce(key);
        initializeSerivce(account);
        initializeSerivce(matchRouting);
        initializeSerivce(hero);
        initializeSerivce(geoIP);
        initializeSerivce(playlog);
        System.out.println(staging.toString() + " lobby server started with success!");
    }

    private void initializeSerivce(ServiceBase service) {
        service.initialize(vertx);
        while (!service.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void Initialize(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.staging = Staging.valueOf(config.getString("staging"));
        this.port = config.getInteger("port");

        session = new SessionServiceImpl();
        ps = new PersistenceServiceImpl();
        meta = new MetaServiceImpl();
        key = new KeyServiceImpl();
        account = new AccountServiceImpl();
        hero = new HeroServiceImpl();
        matchRouting = new MatchRoutingServiceImpl();
        geoIP = new GeoIPServiceImpl();
        playlog = new PlaylogServiceImpl();

        start();
    }
}
