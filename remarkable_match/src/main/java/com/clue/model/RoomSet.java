package com.clue.model;

import com.clue.fbs.*;

import java.util.HashMap;
import java.util.HashSet;

public class RoomSet {
    byte league = 1;
    HashMap<String, Room> rooms = new HashMap<String, Room>();
    HashSet<Room> openRooms = new HashSet<Room>();

    public RoomSet(byte league) {
        this.league = league;
    }

    public byte getLeague() {
        return league;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public HashSet<Room> getOpenRooms() {
        return openRooms;
    }

    public void addRoom(Room room) {
        rooms.put(room.getRoomId(), room);
        if (room.getState() == RmmRoomState.Open) {
            openRooms.add(room);
        }
    }

    public Boolean hasRoom(Room room) {
        return rooms.containsKey(room.getRoomId());
    }

    public void removeRoom(Room room) {
        rooms.remove(room.getRoomId());
        openRooms.remove(room);
    }

    public Boolean hasRoom(String key) {
        return rooms.containsKey(key);
    }

    public Room getRoom(String key) {
        return rooms.get(key);
    }

    public void setReady(Room room) {
        openRooms.remove(room);
    }
}
