package com.clue.model;

import com.clue.fbs.RmmVector2b;

public class Vector2b {
    byte x;
    byte z;

    public Vector2b() {
        x = 0;
        z = 0;
    }

    public Vector2b(byte x, byte z) {
        this.x = x;
        this.z = z;
    }

    public Vector2b(int x, int z) {
        this.x = (byte)x;
        this.z = (byte)z;
    }

    public Vector2b(RmmVector2b vec) {
        this.x = vec.x();
        this.z = vec.z();
    }

    public void reverse() {
        x = (byte)(-x);
        z = (byte)(-z);
    }

    public byte getX() {
        return x;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public byte getZ() {
        return z;
    }

    public void setZ(byte z) {
        this.z = z;
    }
}
