// automatically generated by the FlatBuffers compiler, do not modify

package com.clue.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public class RmbReqLogin extends Table {
  public static RmbReqLogin getRootAsRmbReqLogin(ByteBuffer _bb) { return getRootAsRmbReqLogin(_bb, new RmbReqLogin()); }
  public static RmbReqLogin getRootAsRmbReqLogin(ByteBuffer _bb, RmbReqLogin obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public RmbReqLogin __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String uid() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer uidAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String country() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer countryAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public String version() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer versionAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public int metaVersion() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createRmbReqLogin(FlatBufferBuilder builder,
      int uidOffset,
      int countryOffset,
      int versionOffset,
      int metaVersion) {
    builder.startObject(4);
    RmbReqLogin.addMetaVersion(builder, metaVersion);
    RmbReqLogin.addVersion(builder, versionOffset);
    RmbReqLogin.addCountry(builder, countryOffset);
    RmbReqLogin.addUid(builder, uidOffset);
    return RmbReqLogin.endRmbReqLogin(builder);
  }

  public static void startRmbReqLogin(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addUid(FlatBufferBuilder builder, int uidOffset) { builder.addOffset(0, uidOffset, 0); }
  public static void addCountry(FlatBufferBuilder builder, int countryOffset) { builder.addOffset(1, countryOffset, 0); }
  public static void addVersion(FlatBufferBuilder builder, int versionOffset) { builder.addOffset(2, versionOffset, 0); }
  public static void addMetaVersion(FlatBufferBuilder builder, int metaVersion) { builder.addInt(3, metaVersion, 0); }
  public static int endRmbReqLogin(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

