package com.clue.controller;

import com.clue.fbs.RmbMessageType;
import com.clue.fbs.RmbReqPlaylogList;
import com.clue.fbs.RmbResPlaylogList;
import com.clue.model.Playlog;
import com.clue.model.Session;
import com.clue.service.Service;
import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;
import java.util.List;

public class PlaylogController extends ControllerBase {
    public void processReqPlaylogList(Session session, byte seqNo, ByteBuffer buffer) {
        RmbReqPlaylogList req = RmbReqPlaylogList.getRootAsRmbReqPlaylogList(buffer);
        Service.playlog.getPlaylogs(session.getAccount().uid, req.from(), req.count(), res -> {
            runAlways(session, seqNo, res, () -> {
                sendPlaylogs(session, seqNo, res.result());
            });
        });
    }

    void sendPlaylogs(Session session, byte seqNo, List<Playlog> playlogs) {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int [] allLogsOffset = new int[playlogs.size()];
        for (int index = 0; index < playlogs.size(); index++) {
            Playlog playlog = playlogs.get(index);
            allLogsOffset[index] = FlatBuilder.build(builder, playlog);
        }

        int logsOffset = RmbResPlaylogList.createLogsVector(builder, allLogsOffset);
        builder.finish(RmbResPlaylogList.createRmbResPlaylogList(builder, logsOffset));
        Service.session.send(session, seqNo, RmbMessageType.ResPlaylogList, builder.sizedByteArray());
    }
}
