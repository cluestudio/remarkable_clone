package com.clue.controller;

import com.clue.service.Service;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;

public class MatchVerticle extends AbstractVerticle {
    Service service = new Service();
    MatchController controller = new MatchController();
    MatchControllerWeb controllerWeb = new MatchControllerWeb();

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        service.Initialize(vertx, config());
        controller.bind(server, "/match");
        controllerWeb.bind(server);
        server.listen(Service.port);
    }
}