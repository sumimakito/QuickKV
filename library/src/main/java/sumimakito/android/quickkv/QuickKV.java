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
import java.io.*;
import sumimakito.android.quickkv.util.*;
import sumimakito.android.quickkv.security.*;

public class QuickKV
{
	private Context pContext;
	private PersistableKeyValueDatabase pKVDB;
	private KeyValueDatabase kvDB;
	private Map<String, PersistableKeyValueDatabase> pKVDBMap;
	private HashMap<String, KeyValueDatabase> kvdbMap;

	public QuickKV(Context context)
	{
		this.pContext = context;
		this.kvDB = new KeyValueDatabase();
		this.pKVDB = new PersistableKeyValueDatabase(pContext);
		this.pKVDBMap = new HashMap<String, PersistableKeyValueDatabase>();
		this.kvdbMap = new HashMap<String, KeyValueDatabase>();
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
		if ((dbName + ".kv").equals(QKVConfig.PKVDB_FILENAME))
		{
			return this.pKVDB;
		}
		else
		{
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

	public QKVCallback encryptPersistableKVDB(String dbName, String key)
	{
		if ((dbName + ".kv").equals(QKVConfig.PKVDB_FILENAME))
		{
			return new QKVCallback(false, QKVCallback.CODE_FAILED, "Default database cannot be encrypted!");
		}
		else if (key == null || key.length() == 0)
		{
			return new QKVCallback(false, QKVCallback.CODE_FAILED, "Key is invalid!");
		}
		else
		{
			try
			{
				FileInputStream fis = pContext.openFileInput(dbName + ".kv");
				String fcontent = FISReader.read(fis);
				if (fcontent.startsWith(QKVConfig.EC_PREFIX))
				{
					return new QKVCallback(false, QKVCallback.CODE_FAILED, "Database already encrypted!");
				}
				else
				{
					String rawStr = QKVConfig.EC_PREFIX + AES256.encode(key, fcontent);
					FileOutputStream fos = pContext.openFileOutput(dbName + ".kv", Context.MODE_PRIVATE);
					fos.write(rawStr.getBytes());
					fos.close();
					return new QKVCallback();
				}
			}
			catch (Exception e)
			{
				if (QKVConfig.DEBUG)
				{
					e.printStackTrace();
				}
				return new QKVCallback(false, QKVCallback.CODE_FAILED, "Failed to encrypt.");
			}
		}
	}

	public QKVCallback decryptPersistableKVDB(String dbName, String key)
	{
		if ((dbName + ".kv").equals(QKVConfig.PKVDB_FILENAME))
		{
			return new QKVCallback(false, QKVCallback.CODE_FAILED, "Default database cannot be decrypted!");
		}
		else if (key == null || key.length() == 0)
		{
			return new QKVCallback(false, QKVCallback.CODE_FAILED, "Key is invalid!");
		}
		else
		{
			try
			{
				FileInputStream fis = pContext.openFileInput(dbName + ".kv");
				String fcontent = FISReader.read(fis);
				if (fcontent.startsWith(QKVConfig.EC_PREFIX))
				{
					String rawStr = AES256.decode(key, fcontent.substring(QKVConfig.EC_PREFIX.length()));
					FileOutputStream fos = pContext.openFileOutput(dbName + ".kv", Context.MODE_PRIVATE);
					fos.write(rawStr.getBytes());
					fos.close();
					return new QKVCallback();
				}
				else
				{
					return new QKVCallback(false, QKVCallback.CODE_FAILED, "No a valid encrypted database!");
				}
			}
			catch (Exception e)
			{
				if (QKVConfig.DEBUG)
				{
					e.printStackTrace();
				}
				return new QKVCallback(false, QKVCallback.CODE_FAILED, "Failed to decrypt.");
			}
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
		else if (QKVConfig.DEBUG)
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
		else if (QKVConfig.DEBUG)
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to release persistable kvdb: No opened database matches the given name \"" + dbName + "\"!");
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
}
