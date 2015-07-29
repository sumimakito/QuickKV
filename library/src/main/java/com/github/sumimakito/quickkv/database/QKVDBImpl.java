/**
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 1.0.0
 */

package com.github.sumimakito.quickkv.database;

public interface QKVDBImpl {
    <K, V> boolean put(K k, V v);

    <K> Object get(K k);

    <K> boolean containsKey(K k);

    <V> boolean containsValue(V v);

    <K> boolean remove(K k);

    <K> boolean remove(K[] k);

    void clear();

    int size();
}
