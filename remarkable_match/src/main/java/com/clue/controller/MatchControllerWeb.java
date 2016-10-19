package com.clue.controller;

import io.vertx.core.http.HttpServer;


public class MatchControllerWeb {
    public void bind(HttpServer server) {
        server.requestHandler(req -> {
            System.out.println(req.uri());
            if (req.uri().equals("/")) {
                req.response().sendFile("webroot/list.html");
            }
            else if (req.uri().indexOf("/log/") == 0) {
                req.response().sendFile("webroot/view.html");
            }
            else if (req.uri().indexOf("/assets/") == 0) {
                System.out.println(req.uri());
                req.response().sendFile("webroot" + req.uri());
            }
        });
    }
}
