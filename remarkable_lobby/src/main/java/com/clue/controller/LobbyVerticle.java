package com.clue.controller;

import com.clue.service.Service;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;

public class LobbyVerticle extends AbstractVerticle {
    Service service = new Service();
    LobbyController controller = new LobbyController();
    LobbyWebController controllerWeb = new LobbyWebController();

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        service.Initialize(vertx, config());
        controller.bind(server, "/lobby");
        controllerWeb.bind(server);
        server.listen(Service.port);
    }
}