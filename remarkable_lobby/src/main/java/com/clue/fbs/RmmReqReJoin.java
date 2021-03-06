// automatically generated by the FlatBuffers compiler, do not modify

package com.clue.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public class RmmReqReJoin extends Table {
  public static RmmReqReJoin getRootAsRmmReqReJoin(ByteBuffer _bb) { return getRootAsRmmReqReJoin(_bb, new RmmReqReJoin()); }
  public static RmmReqReJoin getRootAsRmmReqReJoin(ByteBuffer _bb, RmmReqReJoin obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public RmmReqReJoin __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String roomId() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer roomIdAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String uid() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer uidAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public String version() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer versionAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }

  public static int createRmmReqReJoin(FlatBufferBuilder builder,
      int roomIdOffset,
      int uidOffset,
      int versionOffset) {
    builder.startObject(3);
    RmmReqReJoin.addVersion(builder, versionOffset);
    RmmReqReJoin.addUid(builder, uidOffset);
    RmmReqReJoin.addRoomId(builder, roomIdOffset);
    return RmmReqReJoin.endRmmReqReJoin(builder);
  }

  public static void startRmmReqReJoin(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addRoomId(FlatBufferBuilder builder, int roomIdOffset) { builder.addOffset(0, roomIdOffset, 0); }
  public static void addUid(FlatBufferBuilder builder, int uidOffset) { builder.addOffset(1, uidOffset, 0); }
  public static void addVersion(FlatBufferBuilder builder, int versionOffset) { builder.addOffset(2, versionOffset, 0); }
  public static int endRmmReqReJoin(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

