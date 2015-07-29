/**
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 1.0.0
 */

package com.github.sumimakito.quickkv.database;

import android.content.Context;

import com.github.sumimakito.quickkv.QuickKV;

public abstract class QKVDB {
    protected Context pContext;
    protected QuickKV pInstance;
    protected String dbAlias;
    protected String pKey;
    protected boolean isGZipEnabled = false;

    public QKVDB(QuickKV quickKV, Context context) {
        this.pInstance = quickKV;
        this.pContext = context;
    }
}
