package com.clue.util;

public class DebugUtil {
    public static String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stack : e.getStackTrace()) {
            sb.append(stack.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
