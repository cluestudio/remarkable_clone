package com.clue.service;

import com.clue.model.Player;
import com.clue.model.Room;
import com.clue.model.RoomSet;

public interface RoomService extends ServiceBase {
    void addRoom(Room room);
    void removeRoom(Room room);
    Room getRoom(String key);
    Room getOpenedRoom(byte league, int matchScore, int range);
    void setRoomTimerID(Room room, Long timerId);
    Room getRoomByTimerID(Long timerId);
    RoomSet getRoomSet(byte league);
    Boolean joinRoom(Room room, Player user);
    void leavePlayer(Room room, Player user);
    void broadcast(Room room, byte messageType, byte[] msg);
    void broadcastNoRecord(Room room, byte messageType, byte[] msg);
    String debugString();
}
