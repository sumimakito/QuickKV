/**
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 1.0.0
 */

package com.github.sumimakito.quickkv.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataProcessor {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static class Basic{
        public static String bytesToHex(byte[] bytes) {
            char[] hexChars = new char[bytes.length * 2];
            for ( int j = 0; j < bytes.length; j++ ) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }

        public static byte[] hexToBytes(String hex) {
            int len = hex.length();
            byte[] bytes = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                        + Character.digit(hex.charAt(i+1), 16));
            }
            return bytes;
        }
    }

    public static class Persistable {
        public static Object dePrefix(String k) throws JSONException {
            if (k.startsWith("S_")) {
                return k.substring("S_".length());
            } else if (k.startsWith("B_")) {
                return Boolean.parseBoolean(k.substring("B_".length()));
            } else if (k.startsWith("I_")) {
                return Integer.parseInt(k.substring("I_".length()));
            } else if (k.startsWith("F_")) {
                return Float.parseFloat(k.substring("F_".length()));
            } else if (k.startsWith("D_")) {
                return Double.parseDouble(k.substring("D_".length()));
            } else if (k.startsWith("L_")) {
                return Long.parseLong(k.substring("L_".length()));
            } else if (k.startsWith("JA_")) {
                return new JSONArray(k.substring("JA_".length()));
            } else if (k.startsWith("JO_")) {
                return new JSONObject(k.substring("JO_".length()));
            } else {
                return null;
            }
        }

        public static boolean isValidDataType(Object obj) {
            if (obj instanceof String
                    || obj instanceof Integer
                    || obj instanceof Boolean
                    || obj instanceof Long
                    || obj instanceof Float
                    || obj instanceof Double
                    || obj instanceof JSONObject
                    || obj instanceof JSONArray) {
                return true;
            } else {
                return false;
            }
        }

        public static String addPrefix(Object obj) {
            if (obj instanceof String) {
                return "S_" + obj.toString();
            } else if (obj instanceof Integer) {
                return "I_" + obj.toString();
            } else if (obj instanceof Boolean) {
                return "B_" + obj.toString();
            } else if (obj instanceof Long) {
                return "L_" + obj.toString();
            } else if (obj instanceof Float) {
                return "F_" + obj.toString();
            } else if (obj instanceof Double) {
                return "D_" + obj.toString();
            } else if (obj instanceof JSONObject) {
                return "JO_" + obj.toString();
            } else if (obj instanceof JSONArray) {
                return "JA_" + obj.toString();
            } else {
                return obj.toString();
            }
        }
    }
}
