// automatically generated by the FlatBuffers compiler, do not modify

package com.clue.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public class RmsAnimationInfo extends Struct {
  public RmsAnimationInfo __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public short name() { return bb.getShort(bb_pos + 0); }
  public float appearLength() { return bb.getFloat(bb_pos + 4); }
  public float stayLength() { return bb.getFloat(bb_pos + 8); }
  public float idleLength() { return bb.getFloat(bb_pos + 12); }
  public float runLength() { return bb.getFloat(bb_pos + 16); }
  public float damageLength() { return bb.getFloat(bb_pos + 20); }
  public float victoryLength() { return bb.getFloat(bb_pos + 24); }
  public float deadLength() { return bb.getFloat(bb_pos + 28); }
  public float attackLength() { return bb.getFloat(bb_pos + 32); }
  public float skill1Length() { return bb.getFloat(bb_pos + 36); }
  public float skill2Length() { return bb.getFloat(bb_pos + 40); }
  public float skill3Length() { return bb.getFloat(bb_pos + 44); }
  public byte attackCount() { return bb.get(bb_pos + 48); }
  public byte skill1Count() { return bb.get(bb_pos + 49); }
  public byte skill2Count() { return bb.get(bb_pos + 50); }
  public byte skill3Count() { return bb.get(bb_pos + 51); }

  public static int createRmsAnimationInfo(FlatBufferBuilder builder, short name, float appearLength, float stayLength, float idleLength, float runLength, float damageLength, float victoryLength, float deadLength, float attackLength, float skill1Length, float skill2Length, float skill3Length, byte attackCount, byte skill1Count, byte skill2Count, byte skill3Count) {
    builder.prep(4, 52);
    builder.putByte(skill3Count);
    builder.putByte(skill2Count);
    builder.putByte(skill1Count);
    builder.putByte(attackCount);
    builder.putFloat(skill3Length);
    builder.putFloat(skill2Length);
    builder.putFloat(skill1Length);
    builder.putFloat(attackLength);
    builder.putFloat(deadLength);
    builder.putFloat(victoryLength);
    builder.putFloat(damageLength);
    builder.putFloat(runLength);
    builder.putFloat(idleLength);
    builder.putFloat(stayLength);
    builder.putFloat(appearLength);
    builder.pad(2);
    builder.putShort(name);
    return builder.offset();
  }
}

