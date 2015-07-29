/**
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 1.0.0
 */

package com.github.sumimakito.quickkv.util;

import android.content.Context;

import java.io.File;

public class StorageManager {
    private Context pContext;
    private File workspace;

    public StorageManager(Context context){
        this.pContext = context;
        this.workspace = this.pContext.getFilesDir();
    }

    public void setWorkspace(String fPath){
        this.workspace = new File(fPath);
    }

    public File getWorkspace() {
        return workspace;
    }
}
