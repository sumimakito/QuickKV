/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under GNU GPL v3 License.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.7
 */

package sumimakito.android.quickkv;

import android.content.*;
import java.util.*;
import sumimakito.android.quickkv.database.*;
import android.util.*;

public class QuickKV
{
	private Context pContext;
	private PersistableKeyValueDatabase pKVDB;
	private KeyValueDatabase kvDB;
	private Map<String, PersistableKeyValueDatabase> pKVDBMap;
	private HashMap<String, KeyValueDatabase> kvdbMap;
	private HashMap<String, EncryptedPersistableKVDB> ecPKVDBMap;
	private EncryptedPersistableKVDB ecPKVDB;

	public QuickKV(Context context)
	{
		this.pContext = context;
		this.kvDB = new KeyValueDatabase();
		this.pKVDB = new PersistableKeyValueDatabase(pContext);
		this.pKVDBMap = new HashMap<String, PersistableKeyValueDatabase>();
		this.kvdbMap = new HashMap<String, KeyValueDatabase>();
		this.ecPKVDBMap = new HashMap<String, EncryptedPersistableKVDB>();
	}

	public PersistableKeyValueDatabase getDefaultPersistableKVDB()
	{
		return this.pKVDB;
	}

	public KeyValueDatabase getDefaultKVDB()
	{
		return this.kvDB;
	}

	public PersistableKeyValueDatabase getPersistableKVDB(String dbName)
	{
		if((dbName + ".kv").equals(QKVConfig.ECPKVDB_FILENAME)){
			return null;
		}
		if ((dbName + ".kv").equals(QKVConfig.PKVDB_FILENAME))
		{
			return this.pKVDB;
		}
		else
		{
			if(isEncrpytedPersistableKVDBOpened(dbName)){
				return null;
			}
			if (!this.pKVDBMap.containsKey(dbName))
			{
				PersistableKeyValueDatabase pkvd = new PersistableKeyValueDatabase(pContext, dbName);
				this.pKVDBMap.put(dbName, pkvd);
			}
			return this.pKVDBMap.get(dbName);
		}
	}

	public KeyValueDatabase getKVDB(String dbAlias)
	{
		if (!this.kvdbMap.containsKey(dbAlias))
		{
			KeyValueDatabase kvd = new KeyValueDatabase();
			this.kvdbMap.put(dbAlias, kvd);
		}
		return this.kvDB;
	}

	public EncryptedPersistableKVDB getDefaultEncryptedPersistableKVDB(String key)
	{
		if (this.ecPKVDB == null)
		{
			this.ecPKVDB = new EncryptedPersistableKVDB(pContext, key);
		}
		return this.ecPKVDB;
	}

	public EncryptedPersistableKVDB getEncryptedKVDB(String dbName, String key)
	{
		if ((dbName + ".kv").equals(QKVConfig.PKVDB_FILENAME)||(dbName + ".kv").equals(QKVConfig.ECPKVDB_FILENAME))
		{
			if (this.ecPKVDB == null)
			{
				this.ecPKVDB = new EncryptedPersistableKVDB(pContext, key);
			}
			return this.ecPKVDB;
		}
		else if ((dbName + ".kv").equals(QKVConfig.PKVDB_FILENAME))
		{
			return null;
		}
		else
		{
			if (isPersistableKVDBOpened(dbName))
			{
				return null;
			}
			if (!this.ecPKVDBMap.containsKey(dbName))
			{
				EncryptedPersistableKVDB epkvd = new EncryptedPersistableKVDB(pContext, dbName, key);
				this.ecPKVDBMap.put(dbName, epkvd);
			}
			return this.ecPKVDBMap.get(dbName);
		}
	}

	public boolean isEncrpytedPersistableKVDBOpened(String dbName)
	{
		if (this.ecPKVDBMap.containsKey(dbName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isPersistableKVDBOpened(String dbName)
	{
		if (this.pKVDBMap.containsKey(dbName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isKVDBOpened(String dbAlias)
	{
		if (this.kvdbMap.containsKey(dbAlias))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void releaseKVDB(String dbAlias)
	{
		if (this.kvdbMap.containsKey(dbAlias))
		{
			kvdbMap.remove(dbAlias);
		}
		else if(QKVConfig.DEBUG)
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to release kvdb: No opened database matches the given alias \"" + dbAlias + "\"!");
		}
	}

	public void releasePersistableKVDB(String dbName)
	{
		if (this.pKVDBMap.containsKey(dbName))
		{
			pKVDBMap.remove(dbName);
		}
		else if(QKVConfig.DEBUG)
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to release persistable kvdb: No opened database matches the given name \"" + dbName + "\"!");
		}
	}

	public void releaseEncryptedPersistableKVDB(String dbName)
	{
		if (this.ecPKVDBMap.containsKey(dbName))
		{
			ecPKVDBMap.remove(dbName);
		}
		else if(QKVConfig.DEBUG)
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to release encrypted persistable kvdb: No opened database matches the given name \"" + dbName + "\"!");
		}
	}

	public void releaseAllKVDB()
	{
		this.kvdbMap.clear();
	}

	public void releaseAllPersistableKVDB()
	{
		this.pKVDBMap.clear();
	}

	public void releaseAllEncryptedPersistableKVDB()
	{
		this.ecPKVDBMap.clear();
	}
}
