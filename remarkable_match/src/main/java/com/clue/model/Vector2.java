package com.clue.model;

import com.clue.fbs.RmmVector2;

public class Vector2 {
    short x;
    short z;

    public Vector2() {
        x = 0;
        z = 0;
    }

    public Vector2(short x, short z) {
        this.x = x;
        this.z = z;
    }

    public Vector2(int x, int z) {
        this.x = (short)x;
        this.z = (short)z;
    }

    public Vector2(RmmVector2 vec) {
        this.x = vec.x();
        this.z = vec.z();
    }

    public void reverse() {
        x = (short)(-x);
        z = (short)(-z);
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getZ() {
        return z;
    }

    public void setZ(short z) {
        this.z = z;
    }
}

