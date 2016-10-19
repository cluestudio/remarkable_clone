package com.clue.controller;

import com.clue.fbs.*;
import com.clue.model.Assets;
import com.clue.model.Hero;
import com.clue.model.Session;
import com.clue.service.Service;
import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class MatchRoutingController extends ControllerBase {
    public void processReqRouteMatch(Session session, byte seqNo, ByteBuffer buffer) {
        RmbReqRouteMatch req = RmbReqRouteMatch.getRootAsRmbReqRouteMatch(buffer);
        String host = Service.matchRouting.routeMatchServer(req.region(), req.league());

        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int hostOffset = 0;
        if (host != null) { 
            hostOffset = builder.createString(host);
        }

        RmbResRouteMatch.startRmbResRouteMatch(builder);
        RmbResRouteMatch.addHost(builder, hostOffset);
        builder.finish(RmbResRouteMatch.endRmbResRouteMatch(builder));
        Service.session.send(session, seqNo, RmbMessageType.ResRouteMatch, builder.sizedByteArray());
    }
}
