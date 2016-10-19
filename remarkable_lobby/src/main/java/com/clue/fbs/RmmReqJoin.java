// automatically generated by the FlatBuffers compiler, do not modify

package com.clue.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public class RmmReqJoin extends Table {
  public static RmmReqJoin getRootAsRmmReqJoin(ByteBuffer _bb) { return getRootAsRmmReqJoin(_bb, new RmmReqJoin()); }
  public static RmmReqJoin getRootAsRmmReqJoin(ByteBuffer _bb, RmmReqJoin obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public RmmReqJoin __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String uid() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer uidAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public short hero() { int o = __offset(6); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public byte league() { int o = __offset(8); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public byte roomType() { int o = __offset(10); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public String version() { int o = __offset(12); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer versionAsByteBuffer() { return __vector_as_bytebuffer(12, 1); }
  public RmmPlayer aiPlayer() { return aiPlayer(new RmmPlayer()); }
  public RmmPlayer aiPlayer(RmmPlayer obj) { int o = __offset(14); return o != 0 ? obj.__init(__indirect(o + bb_pos), bb) : null; }

  public static int createRmmReqJoin(FlatBufferBuilder builder,
      int uidOffset,
      short hero,
      byte league,
      byte roomType,
      int versionOffset,
      int aiPlayerOffset) {
    builder.startObject(6);
    RmmReqJoin.addAiPlayer(builder, aiPlayerOffset);
    RmmReqJoin.addVersion(builder, versionOffset);
    RmmReqJoin.addUid(builder, uidOffset);
    RmmReqJoin.addHero(builder, hero);
    RmmReqJoin.addRoomType(builder, roomType);
    RmmReqJoin.addLeague(builder, league);
    return RmmReqJoin.endRmmReqJoin(builder);
  }

  public static void startRmmReqJoin(FlatBufferBuilder builder) { builder.startObject(6); }
  public static void addUid(FlatBufferBuilder builder, int uidOffset) { builder.addOffset(0, uidOffset, 0); }
  public static void addHero(FlatBufferBuilder builder, short hero) { builder.addShort(1, hero, 0); }
  public static void addLeague(FlatBufferBuilder builder, byte league) { builder.addByte(2, league, 0); }
  public static void addRoomType(FlatBufferBuilder builder, byte roomType) { builder.addByte(3, roomType, 0); }
  public static void addVersion(FlatBufferBuilder builder, int versionOffset) { builder.addOffset(4, versionOffset, 0); }
  public static void addAiPlayer(FlatBufferBuilder builder, int aiPlayerOffset) { builder.addOffset(5, aiPlayerOffset, 0); }
  public static int endRmmReqJoin(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
