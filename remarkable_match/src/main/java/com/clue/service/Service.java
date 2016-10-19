package com.clue.service;

import com.clue.model.Staging;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Service extends Thread {
    public static Staging staging = Staging.beta;
    public static String host;
    public static int port = 0;
    public static SessionService session = null;
    public static AccountService account = null;
    public static RoomService room = null;
    public static PersistenceService ps = null;
    public static MetaService meta = null;
    public static KeyService key = null;
    public static PlaylogService playlog = null;
    public static Vertx vertx = null;

    public void run() {
        initializeSerivce(session);
        initializeSerivce(account);
        initializeSerivce(room);
        initializeSerivce(ps);
        initializeSerivce(key);
        initializeSerivce(meta);
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
        this.host = config.getString("host");

        session = new SessionServiceImpl();
        account = new AccountServiceImpl();
        room = new RoomServiceImpl();
        ps = new PersistenceServiceImpl();
        key = new KeyServiceImpl();
        meta = new MetaServiceImpl();
        playlog = new PlaylogServiceImpl();
        start();
    }
}
