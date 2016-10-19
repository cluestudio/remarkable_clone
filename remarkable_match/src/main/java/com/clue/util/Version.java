package com.clue.util;

import java.util.StringTokenizer;

public class Version implements Comparable<Version> {
    private int major = 0;
    private int minor = 0;
    private int patch = 0;

    public Version() {
    }

    public Version(String version) {
        int index = 0;
        int[] numbers = new int[3];

        StringTokenizer tokens = new StringTokenizer(version, ".");
        while (tokens.hasMoreElements()) {
            numbers[index++] = Integer.parseInt(tokens.nextToken());
            if (index == 3) {
                break;
            }
        }

        major = numbers[0];
        minor = numbers[1];
        patch = numbers[2];
    }

    public final String get() {
        StringBuilder builder = new StringBuilder();
        builder.append(major);
        builder.append(".");
        builder.append(minor);
        builder.append(".");
        builder.append(patch);
        return builder.toString();
    }

    @Override
    public int compareTo(Version rhs) {
        if (major < rhs.major) {
            return -1;
        }
        else if (major > rhs.major) {
            return 1;
        }

        if (minor < rhs.minor) {
            return -1;
        }
        else if (minor > rhs.minor) {
            return 1;
        }

        if (patch < rhs.patch) {
            return -1;
        }
        else if (patch > rhs.patch) {
            return 1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) {
            return true;
        }

        if(that == null) {
            return false;
        }

        if(this.getClass() != that.getClass()) {
            return false;
        }

        return this.compareTo((Version) that) == 0;
    }
}