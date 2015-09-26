package com.github.sumimakito.quickkv.database;

public interface SpecificGettersImpl {
    <K> String getString(K k);
    <K> long getLong(K k);
    <K> int getInt(K k);
    <K> double getDouble(K k);
    <K> float getFloat(K k);
    <K> boolean getBoolean(K k);
    <K> org.json.JSONObject getJSONObject(K k);
    <K> org.json.JSONArray getJSONArray(K k);
}
