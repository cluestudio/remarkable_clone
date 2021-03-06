// automatically generated by the FlatBuffers compiler, do not modify

package com.clue.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public class RmmUnitHit extends Struct {
  public RmmUnitHit __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public short from() { return bb.getShort(bb_pos + 0); }
  public short to() { return bb.getShort(bb_pos + 2); }
  public short skillId() { return bb.getShort(bb_pos + 4); }
  public byte rate() { return bb.get(bb_pos + 6); }
  public RmmVector2 force() { return force(new RmmVector2()); }
  public RmmVector2 force(RmmVector2 obj) { return obj.__init(bb_pos + 8, bb); }

  public static int createRmmUnitHit(FlatBufferBuilder builder, short from, short to, short skillId, byte rate, short force_x, short force_z) {
    builder.prep(2, 12);
    builder.prep(2, 4);
    builder.putShort(force_z);
    builder.putShort(force_x);
    builder.pad(1);
    builder.putByte(rate);
    builder.putShort(skillId);
    builder.putShort(to);
    builder.putShort(from);
    return builder.offset();
  }
}

