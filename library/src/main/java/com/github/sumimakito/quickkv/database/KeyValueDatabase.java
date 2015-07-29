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

import com.github.sumimakito.maglevio.MaglevReader;
import com.github.sumimakito.maglevio.MaglevWriter;
import com.github.sumimakito.quickkv.QKVConfig;
import com.github.sumimakito.quickkv.QuickKV;
import com.github.sumimakito.quickkv.security.AES256;
import com.github.sumimakito.quickkv.util.CompressHelper;
import com.github.sumimakito.quickkv.util.DataProcessor;
import com.github.sumimakito.quickkv.util.KVDBProperties;
import com.github.sumimakito.quickkv.util.QKVLogger;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KeyValueDatabase extends QKVDB implements QKVDBImpl {
    private HashMap<Object, Object> dMap;

    public KeyValueDatabase(QuickKV quickKV, Context context) {
        super(quickKV, context);
        this.dbAlias = QKVConfig.KVDB_FILE_NAME;
        this.dMap = new HashMap<Object, Object>();
        this.sync(false);
        QKVLogger.log("i", "KVDB Initialized!");
    }

    public KeyValueDatabase(QuickKV quickKV, Context context, boolean enableGZip) {
        super(quickKV, context);
        this.isGZipEnabled = enableGZip;
        this.dbAlias = QKVConfig.KVDB_FILE_NAME;
        this.dMap = new HashMap<Object, Object>();
        this.sync(false);
        QKVLogger.log("i", "KVDB Initialized!");
    }

    public KeyValueDatabase(QuickKV quickKV, Context context, String dbAlias) {
        super(quickKV, context);
        this.pContext = context;
        this.dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        this.dMap = new HashMap<Object, Object>();
        this.sync(false);
        QKVLogger.log("i", "KVDB Initialized!");
    }

    public KeyValueDatabase(QuickKV quickKV, Context context, String dbAlias, boolean enableGZip) {
        super(quickKV, context);
        this.isGZipEnabled = enableGZip;
        this.pContext = context;
        this.dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        this.dMap = new HashMap<Object, Object>();
        this.sync(false);
        QKVLogger.log("i", "KVDB Initialized!");
    }

    public KeyValueDatabase(QuickKV quickKV, Context context, String dbAlias, String key) {
        super(quickKV, context);
        this.pKey = key;
        this.pContext = context;
        this.dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        this.dMap = new HashMap<Object, Object>();
        this.sync(false);
        QKVLogger.log("i", "KVDB Initialized!");
    }

    public KeyValueDatabase(QuickKV quickKV, Context context, String dbAlias, String key, boolean enableGZip) {
        super(quickKV, context);
        this.isGZipEnabled = enableGZip;
        this.pKey = key;
        this.pContext = context;
        this.dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ? dbAlias : dbAlias + QKVConfig.KVDB_EXT;
        this.dMap = new HashMap<Object, Object>();
        this.sync(false);
        QKVLogger.log("i", "KVDB Initialized!");
    }

    public void setGZipEnabled(boolean enabled){
        isGZipEnabled = enabled;
    }

    @Override
    public <K extends Object, V extends Object> boolean put(K k, V v) {
        if (k == null || v == null) {
            return false;
        }
        this.dMap.put(k, v);
        return true;
    }

    @Override
    public <K extends Object> Object get(K k) {
        if (k == null) {
            return null;
        }
        if (this.dMap.containsKey(k)) return this.dMap.get(k);
        else return null;
    }

    @Override
    public <K extends Object> boolean containsKey(K k) {
        if (this.dMap.containsKey(k)) return true;
        else return false;
    }

    @Override
    public <V extends Object> boolean containsValue(V v) {
        if (this.dMap.containsValue(v)) return true;
        else return false;
    }

    @Override
    public <K extends Object> boolean remove(K k) {
        if (k == null) {
            return false;
        }
        if (this.dMap.containsKey(k)) {
            this.dMap.remove(k);
            return true;
        } else return false;
    }

    @Override
    public <K extends Object> boolean remove(K[] k) {
        if (k == null || k.length == 0) {
            return false;
        }
        int r = 0;
        for (K key : k) {
            if (this.dMap.containsKey(key)) {
                this.dMap.remove(key);
                r++;
            }
        }
        if (r < k.length) return false;
        else return true;
    }

    @Override
    public void clear() {
        this.dMap.clear();
    }

    @Override
    public int size() {
        return this.dMap.size();
    }

    public List<Object> getKeys() {
        List<Object> list = new ArrayList<Object>();
        if (this.dMap.size() > 0) {
            Iterator iter = dMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                list.add(key);
            }
        }
        return list;
    }

    public List<Object> getValues() {
        List<Object> list = new ArrayList<Object>();
        if (this.dMap.size() > 0) {
            Iterator iter = dMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object value = entry.getValue();
                list.add(value);
            }
        }
        return list;
    }

    public boolean persist() {
        if (this.dMap.size() > 0) {
            try {
                JSONObject treeRoot = new JSONObject();
                treeRoot.put(KVDBProperties.C_PROP, new JSONObject());
                JSONObject propRoot = (JSONObject) treeRoot.get(KVDBProperties.C_PROP);
                propRoot.put(KVDBProperties.P_PROP_STRUCT_VER, QKVConfig.STRUCT_VER_STRING);
                propRoot.put(KVDBProperties.P_PROP_GZIP, isGZipEnabled);
                propRoot.put(KVDBProperties.P_PROP_ENCRYPTION, (this.pKey != null && this.pKey.length() > 0));
                treeRoot.put(KVDBProperties.P_DATA, new JSONObject());
                JSONObject dataRoot = (JSONObject) treeRoot.get(KVDBProperties.P_DATA);
                Iterator iter = this.dMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    if (DataProcessor.Persistable.isValidDataType(key)
                            && DataProcessor.Persistable.isValidDataType(val)) {
                        if (this.pKey != null && this.pKey.length() > 0)
                            dataRoot.put(AES256.encode(this.pKey, DataProcessor.Persistable.addPrefix(key)), AES256.encode(this.pKey, DataProcessor.Persistable.addPrefix(val)));
                        else
                            dataRoot.put(DataProcessor.Persistable.addPrefix(key), DataProcessor.Persistable.addPrefix(val));
                    }
                }
                if (isGZipEnabled) {
                    String compressedData = DataProcessor.Basic.bytesToHex(CompressHelper.compress(dataRoot.toString().getBytes()));
                    treeRoot.remove(KVDBProperties.P_DATA);
                    treeRoot.put(KVDBProperties.P_DATA, compressedData);
                }
                String fName = dbAlias == null ? QKVConfig.KVDB_FILE_NAME : dbAlias;
                File fTarget = new File(pInstance.getStorageManager().getWorkspace(), fName);
                MaglevWriter.NIO.MappedBFR.writeBytesToFile(treeRoot.toString().getBytes(),
                        fTarget.getAbsolutePath());
                return true;
            } catch (Exception e) {
                QKVLogger.ex(e);
                return false;
            }
        } else return true;
    }

    public void persist(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (dMap) {
                    if (persist()) callback.onSuccess();
                    else callback.onFailed();
                }
            }
        }).start();
    }

    public boolean sync() {
        return this.sync(true);
    }

    public boolean sync(boolean merge) {
        try {
            if (!merge) {
                this.dMap.clear();
            }

            File kvdbFile = new File(pInstance.getStorageManager().getWorkspace(), dbAlias == null ? QKVConfig.KVDB_FILE_NAME : dbAlias);
            String rawData = kvdbFile.length() < 256 * 1000 ? MaglevReader.IO.fileToString(kvdbFile.getAbsolutePath()) : MaglevReader.NIO.MappedBFR.fileToString(kvdbFile.getAbsolutePath());
            if (rawData.length() > 0) {
                JSONObject treeRoot = (JSONObject) JSONValue.parse(rawData);
                JSONObject properties = (JSONObject) treeRoot.get(KVDBProperties.C_PROP);
                boolean gzip = (Boolean) properties.get(KVDBProperties.P_PROP_GZIP);
                if (gzip) {
                    String rawDataBody = CompressHelper.decompress(
                            DataProcessor.Basic.hexToBytes((String) treeRoot.get(KVDBProperties.P_DATA))
                    );
                    if (parseKVJS((JSONObject) JSONValue.parse(rawDataBody))) return true;
                    else return false;
                } else {
                    if (parseKVJS((JSONObject) treeRoot.get(KVDBProperties.P_DATA))) return true;
                    else return false;
                }
            }
            return true;

        } catch (Exception e) {
            QKVLogger.ex(e);
            return false;
        }
    }

    public void sync(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (dMap) {
                    if (sync()) callback.onSuccess();
                    else callback.onFailed();
                }
            }
        }).start();
    }

    public void sync(final boolean merge, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (dMap) {
                    if (sync(merge)) callback.onSuccess();
                    else callback.onFailed();
                }
            }
        }).start();
    }

    public boolean enableEncryption(String key) {
        if (key != null && key.length() > 0) {
            this.pKey = key;
            persist();
            return true;
        } else return false;
    }

    public void disableEncryption() {
        this.pKey = null;
        persist();
    }

    private boolean parseKVJS(JSONObject json) {
        try {
            Iterator<String> keys = json.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String val = json.get(key).toString();
                Object k, v;
                if (this.pKey != null && this.pKey.length() > 0) {
                    k = DataProcessor.Persistable.dePrefix(AES256.decode(this.pKey, key));
                    v = DataProcessor.Persistable.dePrefix(AES256.decode(this.pKey, val));
                } else {
                    k = DataProcessor.Persistable.dePrefix(key);
                    v = DataProcessor.Persistable.dePrefix(val);
                }
                this.dMap.put(k, v);
            }
            return true;
        } catch (Exception e) {
            QKVLogger.ex(e);
            return false;
        }
    }

    public interface Callback {
        public void onSuccess();

        public void onFailed();
    }
}
