/**
 * QucikKV
 * Copyright 2014-2016 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 */

package com.github.sumimakito.quickkv.database;

@Deprecated
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
