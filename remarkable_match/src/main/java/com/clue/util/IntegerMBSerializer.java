package com.clue.util;

public class IntegerMBSerializer {
    public static byte[] encodeLength(int length) throws Exception {
        if (length <= 0x7F) {
            byte[] res = new byte[1];
            res[0] = (byte)length;
            return res;
        }
        else if (length <= 0x7FF) {
            byte[] res = new byte[2];
            res[0] = (byte)(((length >> 6) & 0x1F) | 0xC0);
            res[1] = (byte)((length & 0x3F) | 0x80);
            return res;
        }
        else if (length <= 0xFFFF) {
            byte[] res = new byte[3];
            res[0] = (byte)(((length >> 12) & 0xF) | 0xE0);
            res[1] = (byte)(((length >> 6) & 0x3F) | 0x80);
            res[2] = (byte)((length & 0x3F) | 0x80);
            return res;
        }

        throw new Exception("length is too big");
    }

    public static int decodeLength(byte[] length) throws Exception {
        if ((length[0] & 0xE0) == 0xE0) {
            int result = 0;
            result |= length[0] & 0xF;
            result <<= 6;
            result |= length[1] & 0x3F;
            result <<= 6;
            result |= length[2] & 0x3F;
            return result;
        }
        else if ((length[0] & 0xC0) == 0xC0) {
            int result = 0;
            result |= (length[0] & 0x1F);
            result <<= 6;
            result |= length[1] & 0x3F;
            return result;
        }
        else if ((length[0] & 0x80) == 0) {
            return (int)length[0];
        }

        throw new Exception("not supported encoding:");
    }
}
