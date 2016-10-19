// automatically generated by the FlatBuffers compiler, do not modify

package com.clue.fbs;

public class RmmMessageType {
  private RmmMessageType() { }
  public static final byte ReqJoin = 0;
  public static final byte ResJoin = 1;
  public static final byte NotiJoin = 2;
  public static final byte ReqReJoin = 3;
  public static final byte ResReJoin = 4;
  public static final byte NotiReJoin = 5;
  public static final byte NotiStart = 6;
  public static final byte ReqSync = 7;
  public static final byte NotiSync = 8;
  public static final byte NotiDeploy = 9;
  public static final byte NotiDamage = 10;
  public static final byte NotiAbnormal = 11;
  public static final byte NotiRevival = 12;
  public static final byte NotiLvUp = 13;
  public static final byte NotiLeave = 14;
  public static final byte ResSyncError = 15;
  public static final byte NotiEnd = 16;
  public static final byte NotiPlaylog = 17;
  public static final byte ReqRoomList = 18;
  public static final byte ResRoomList = 19;

  public static final String[] names = { "ReqJoin", "ResJoin", "NotiJoin", "ReqReJoin", "ResReJoin", "NotiReJoin", "NotiStart", "ReqSync", "NotiSync", "NotiDeploy", "NotiDamage", "NotiAbnormal", "NotiRevival", "NotiLvUp", "NotiLeave", "ResSyncError", "NotiEnd", "NotiPlaylog", "ReqRoomList", "ResRoomList", };

  public static String name(int e) { return names[e]; }
}
