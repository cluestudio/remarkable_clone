package com.clue.service;

import com.clue.fbs.*;
import com.clue.model.Player;
import com.clue.model.Room;
import com.clue.model.RoomSet;
import com.clue.util.DebugUtil;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import scala.collection.DebugUtils;

import java.util.HashMap;
import java.util.HashSet;

public class RoomServiceImpl implements RoomService {
    HashMap<Byte, RoomSet> roomSet = new HashMap<>();
    HashMap<Long, Room> timerRoomMap = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(getClass());

    public boolean isReady() {
        return true;
    }

    public void initialize(Vertx vertx) {
    }

    public void addRoom(Room room) {
        if (room != null) {
            RoomSet set = roomSet.get(room.getLeague());
            if (set == null) {
                set = new RoomSet(room.getLeague());
                roomSet.put(room.getLeague(), set);
            }

            if (set.hasRoom(room) == false) {
                set.addRoom(room);
                logger.info("add room:" + room.getRoomId());
            }
        }
    }

    public void removeRoom(Room room) {
        if (room != null) {
            RoomSet set = roomSet.get(room.getLeague());
            if (set == null) {
                return;
            }

            set.removeRoom(room);
            timerRoomMap.remove(room.getTimerID());
            logger.info("remove room:" + room.getRoomId());
        }
    }

    public RoomSet getRoomSet(byte league) {
        RoomSet result = roomSet.get(league);
        if (result == null) {
            result = new RoomSet(league);
            roomSet.put(league, result);
        }

        return result;
    }

    public Room getRoom(String key) {
        for (RoomSet set : roomSet.values()) {
            if (set.hasRoom(key)) {
                return set.getRoom(key);
            }
        }
        return null;
    }

    public Room getRoomByTimerID(Long timerId) {
        if (timerRoomMap.containsKey(timerId)) {
            return timerRoomMap.get(timerId);
        }

        return null;
    }

    public void setRoomTimerID(Room room, Long timerId) {
        room.setTimerID(timerId);
        timerRoomMap.put(timerId, room);
    }

    public Room getOpenedRoom(byte league, int matchScore, int range) {
        RoomSet set = roomSet.get(league);
        if (set == null) {
            return null;
        }

        HashSet<Room> rooms = set.getOpenRooms();
        for (Room room : rooms) {
            if (room.getPlayerCount() != 1) {
                logger.error("invalid member count!");
                continue;
            }

            Player user = room.getPlayer0();
            int diff = Math.abs(user.getLeaguePoint() - matchScore);
            if (diff < range) {
                return room;
            }
        }

        return null;
    }

    public Boolean joinRoom(Room room, Player palyer) {
        if (room.getState() != RmmRoomState.Open) {
            logger.error("can not join to closed room!");
            return false;
        }

        RoomSet set = roomSet.get(room.getLeague());
        if (set == null) {
            logger.error("no room set");
            return false;
        }

        if (room.isFull()) {
            logger.error("can not join to full room!");
            return false;
        }

        palyer.setRoomId(room.getRoomId());
        if (room.isEmpty()) {
            room.setPlayer0(palyer);
        }
        else {
            room.setPlayer1(palyer);
        }

        if (room.isFull()) {
            room.setState(RmmRoomState.Play);
            set.setReady(room);
        }

        logger.info("player: " + palyer.getUid() + " has joined room: " + room.getRoomId());
        return true;
    }

    public void leavePlayer(Room room, Player user) {
        if (room == null) {
            return;
        }

        RoomSet set = roomSet.get(room.getLeague());
        if (set == null) {
            logger.error("no room set");
            return;
        }

        if (room.getState() == RmmRoomState.Open) {
            set.removeRoom(room);
        }
        else {
            user.setDisconnected(true);
        }

        logger.info("player: " + user.getUid() + " has left room: " + room.getRoomId());
    }

    public void broadcast(Room room, byte messageType, byte[] msg) {
        try {
            broadcastNoRecord(room, messageType, msg);
            room.record(messageType, msg);
        } catch (Exception e) {
            DebugUtil.getStackTrace(e);
        }
    }

    public void broadcastNoRecord(Room room, byte messageType, byte[] msg) {
        Player player0 = room.getPlayer0();
        Player player1 = room.getPlayer1();

        if (player0 != null && !player0.isDisconnected() && !player0.isAi()) {
            Service.session.send(player0.getSessionId(), messageType, msg);
        }

        if (player1 != null && !player1.isDisconnected() && !player1.isAi()) {
            Service.session.send(player1.getSessionId(), messageType, msg);
        }
    }

    public String debugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("all roomSet count:");
        sb.append(roomSet.size());
        sb.append("\n");
        return sb.toString();
    }
}
