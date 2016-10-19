package com.clue.mapper;

import com.clue.model.Playlog;
import com.clue.service.Service;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;

import java.util.List;

public class PlaylogMapper {
    private static final String HEXES = "0123456789ABCDEF";

    public String newLog(String db, int tableNo, Playlog log) {
        return String.format(
                "INSERT INTO `%s`.`playlog%d` (`pid`, `player0`, `player1`, `winner`, `name0`, `leaguePoint0`, `rewardPoint0`, `name1`, `leaguePoint1`, `rewardPoint1`, `birth`, `playTime`, `ver`, `raw`) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%d', '%d', '%s', '%d', '%d', '%d', '%d', '%s', x'%s');",
                db,
                tableNo,
                log.pid,
                log.player0,
                log.player1,
                log.winner,
                log.name0,
                log.leaguePoint0,
                log.rewardPoint0,
                log.name1,
                log.leaguePoint1,
                log.rewardPoint1,
                log.birth,
                log.playTime,
                log.ver,
                toHexString(log.raw)
        );
    }

    private String toHexString(byte[] raw) {
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for ( final byte b : raw ) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public String getAllLogs(String db, String uid, long from, int limitCount) {
        int tableNo0 = Service.key.getPlaylogCurrentTableNo();
        int tableNo1 = (tableNo0-1);
        if (tableNo1 < 0) {
            tableNo1 += Service.key.getPlaylogBucketSize();
        }

        return String.format(
                "SELECT * FROM `%s`.`playlog%d` WHERE (`player0`='%s' or `player1`='%s') AND `birth` > %d " +
                        "UNION ALL " +
                        "SELECT * FROM `%s`.`playlog%d` WHERE (`player0`='%s' or `player1`='%s') AND `birth` > %d " +
                        "ORDER BY `birth` DESC " +
                        "LIMIT %d;",
                db,
                tableNo0,
                uid,
                uid,
                from,
                db,
                tableNo1,
                uid,
                uid,
                from,
                limitCount
        );
    }

    public void fetchPlaylogs(ResultSet resultSet, List<Playlog> targetList) {
        if (resultSet.getNumRows() == 0) {
            return;
        }

        for (JsonArray row: resultSet.getResults()) {
            int index = 1;
            Playlog item = new Playlog();
            item.pid = row.getString(index++);
            item.player0 = row.getString(index++);
            item.player1 = row.getString(index++);
            item.winner = row.getString(index++);
            item.name0 = row.getString(index++);
            item.leaguePoint0 = (short)(int)row.getInteger(index++);
            item.rewardPoint0 = (short)(int)row.getInteger(index++);
            item.name1 = row.getString(index++);
            item.leaguePoint1 = (short)(int)row.getInteger(index++);
            item.rewardPoint1 = (short)(int)row.getInteger(index++);
            item.birth = row.getLong(index++);
            item.playTime = row.getInteger(index++);
            item.ver = row.getString(index++);
            item.raw = row.getBinary(index++);
            targetList.add(item);
        }
    }
}
