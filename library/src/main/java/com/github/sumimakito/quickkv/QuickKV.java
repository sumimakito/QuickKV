/**
 * QucikKV
 * Copyright 2014-2016 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 */

package com.github.sumimakito.quickkv;

import android.content.Context;

import com.github.sumimakito.quickkv.database.HCKeyValueDatabase;
import com.github.sumimakito.quickkv.database.KeyValueDatabase;
import com.github.sumimakito.quickkv.util.StorageManager;

import java.util.HashMap;

public class QuickKV {
    private Context pContext;
    private HashMap<String, KeyValueDatabase> sKVDB;
    private StorageManager storageManager;

    public QuickKV(Context context) {
        this.pContext = context;
        this.sKVDB = new HashMap<String, KeyValueDatabase>();
        this.storageManager = new StorageManager(pContext);
    }

    public void setWorkspace(String fPath){
        this.storageManager.setWorkspace(fPath);
    }

    public StorageManager getStorageManager(){
        return this.storageManager;
    }

    public KeyValueDatabase getDatabase() {
        if (!this.sKVDB.containsKey(QKVConfig.KVDB_FILE_NAME)) {
            this.sKVDB.put(QKVConfig.KVDB_FILE_NAME, new KeyValueDatabase(this, pContext));
        }
        return this.sKVDB.get(QKVConfig.KVDB_FILE_NAME);
    }

    public KeyValueDatabase getDatabase(boolean enableCompression) {
        if (!this.sKVDB.containsKey(QKVConfig.KVDB_FILE_NAME)) {
            this.sKVDB.put(QKVConfig.KVDB_FILE_NAME, new KeyValueDatabase(this, pContext, enableCompression));
        }
        return this.sKVDB.get(QKVConfig.KVDB_FILE_NAME);
    }


    public KeyValueDatabase getDatabase(String dbAlias) {
        if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME)) {
            return getDatabase();
        }
        if (dbAlias == null) {
            return null;
        } else {
            if (dbAlias.length() == 0) {
                dbAlias = QKVConfig.KVDB_FILE_NAME;
            }
            dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        }
        if (!this.sKVDB.containsKey(dbAlias)) {
            this.sKVDB.put(dbAlias, new KeyValueDatabase(this, pContext, dbAlias));
        }

        return this.sKVDB.get(dbAlias);
    }

    public KeyValueDatabase getDatabase(String dbAlias, String key) {
        if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME)) {
            return getDatabase();
        }
        if (dbAlias == null) {
            return null;
        } else {
            if (dbAlias.length() == 0) {
                dbAlias = QKVConfig.KVDB_FILE_NAME;
            }
            dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        }
        if (!this.sKVDB.containsKey(dbAlias)) {
            this.sKVDB.put(dbAlias, new KeyValueDatabase(this, pContext, dbAlias, key));
        }

        return this.sKVDB.get(dbAlias);
    }

    public KeyValueDatabase getDatabase(String dbAlias, boolean enableCompression) {
        if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME)) {
            return getDatabase();
        }
        if (dbAlias == null) {
            return null;
        } else {
            if (dbAlias.length() == 0) {
                dbAlias = QKVConfig.KVDB_FILE_NAME;
            }
            dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        }
        if (!this.sKVDB.containsKey(dbAlias)) {
            this.sKVDB.put(dbAlias, new KeyValueDatabase(this, pContext, dbAlias, enableCompression));
        }

        return this.sKVDB.get(dbAlias);
    }

    public KeyValueDatabase getDatabase(String dbAlias, String key, boolean enableCompression) {
        if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME)) {
            return getDatabase();
        }
        if (dbAlias == null) {
            return null;
        } else {
            if (dbAlias.length() == 0) {
                dbAlias = QKVConfig.KVDB_FILE_NAME;
            }
            dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        }
        if (!this.sKVDB.containsKey(dbAlias)) {
            this.sKVDB.put(dbAlias, new KeyValueDatabase(this, pContext, dbAlias, key, enableCompression));
        }

        return this.sKVDB.get(dbAlias);
    }

    public HCKeyValueDatabase getHCKVDB(String pDBName){
        return new HCKeyValueDatabase(this, pDBName);
    }

    public boolean isDatabaseOpened() {
        return this.sKVDB.containsKey(QKVConfig.KVDB_FILE_NAME);
    }

    public boolean isDatabaseOpened(String dbAlias) {
        if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME)) {
            return isDatabaseOpened();
        }
        if (dbAlias == null) {
            return false;
        } else {
            if (dbAlias.length() == 0) {
                dbAlias = QKVConfig.KVDB_FILE_NAME;
            }
            dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        }
        return this.sKVDB.containsKey(dbAlias);
    }

    public boolean releaseDatabase() {
        if (isDatabaseOpened()) {
            this.sKVDB.remove(QKVConfig.KVDB_FILE_NAME);
            return true;
        }
        return false;
    }

    public boolean releaseDatabase(String dbAlias) {
        if (isDatabaseOpened(dbAlias)) {
            dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
            this.sKVDB.remove(dbAlias);
            return true;
        }
        return false;
    }

    public void releaseAllDatabases() {
        this.sKVDB.clear();
    }
}
