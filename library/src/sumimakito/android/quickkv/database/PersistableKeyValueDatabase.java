package sumimakito.android.quickkv.database;

import android.content.*;
import android.util.*;
import java.util.*;
import sumimakito.android.quickkv.*;
import java.io.*;
import org.json.*;
import sumimakito.android.quickkv.security.*;

public class PersistableKeyValueDatabase extends ADatabase
{
	private static String LTAG;
	private HashMap<Object, Object> dMap;
	private Context pContext;
	private int persistInterval = 0;
	private int opStep = 0;
	private String dbName = null;
	private String pKey = null;

	public PersistableKeyValueDatabase(Context context)
	{
		this.pContext = context;
		this.LTAG = "_" + this.getClass().getSimpleName();
		this.dMap = new HashMap<Object, Object>();
		loadPKVDB();
		if (QKVConfig.DEBUG)
		{
			Log.i(LTAG, "Instance initialized!");
		}
	}

	public PersistableKeyValueDatabase(Context context, String databaseName)
	{
		this.dbName = databaseName + ".kv";
		this.pContext = context;
		this.LTAG = "_" + this.getClass().getSimpleName();
		this.dMap = new HashMap<Object, Object>();
		loadPKVDB();
		if (QKVConfig.DEBUG)
		{
			Log.i(LTAG, "Instance initialized!");
		}
	}

	public void setEncryptionKey(String keyString)
	{
		this.pKey = keyString;
		loadPKVDB();
	}

	public void setPersistInterval(int interval)
	{
		if (interval >= 0)
		{
			this.persistInterval = interval;
			this.opStep = 0;
		}
		else
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to set persist interval: Interval cannot less than 0!");
		}
	}

	@Override
	public void put(Object k, Object v)
	{
		if (isValidDataType(k) && isValidDataType(v))
		{
			this.dMap.put(k, v);
			nextStep();
		}
		else
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to put the key " + k + "\": Type of the key or value is not allowed!");
			Log.w(QKVConfig.PUBLIC_LTAG, "While using a persistable database, you can only put String,Integer,Long,Double,Float,Boolean,JSONObject,JSONArray as the type of a key or value.");
		}
	}

	@Override
	public Object get(Object k)
	{
		if (dMap.containsKey(k))
		{
			return this.dMap.get(k);
		}
		else
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to get the value for the key \"" + k + "\": Key doesn't exist!");
			return null;
		}
	}

	private void nextStep()
	{
		if (persistInterval != 0)
		{
			if (persistInterval >= opStep)
			{
				this.opStep = 0;
				this.persist();
			}
			else
			{
				this.opStep++;
			}
		}
	}

	private void loadPKVDB()
	{
		try
		{
			FileInputStream kvdbFis = pContext.openFileInput(dbName == null ?QKVConfig.PKVDB_FILENAME: dbName);
			byte[] bytes = new byte[1024];  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            while (kvdbFis.read(bytes) != -1)
			{  
                baos.write(bytes, 0, bytes.length);  
            }  
            kvdbFis.close();  
            baos.close();  
			String rawData = pKey == null ?new String(baos.toByteArray()): AES256.decode(pKey, new String(baos.toByteArray()));
			if (rawData.length() > 0)
			{
				JSONObject treeRoot = new JSONObject();
				parseJTree(treeRoot);
			}
			else if (QKVConfig.DEBUG)
			{
				Log.i(LTAG, "Database file is empty!");
			}
		}
		catch (Exception e)
		{
			if (pKey == null)
			{
				Log.w(LTAG, "This database is might encrypted! You need to use setEncryptionKey(String keyString) to apply the key for decryption.");
			}
			else
			{
				Log.w(LTAG, "Maybe the decryption key is incorrect!");
			}
			Log.w(LTAG, "Failed to load persisted database, maybe it is a new database or database name is incorrect or database is broken!");
			Log.w(LTAG, "QuickKV will automatically ignore this problem by creating a new database and overwrite the old one(if possible).");
		}
	}

	private void parseJTree(JSONObject treeRoot)
	{
		try
		{
			Iterator<String> keys = treeRoot.keys();
			while (keys.hasNext())
			{
				String key = keys.next();
				String val = treeRoot.get(key).toString();
				Object k = dePrefix(key);
				Object v = dePrefix(val);
				this.dMap.put(k, v);
			}
		}
		catch (Exception e)
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to parse the database file!");
			if (QKVConfig.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}

	private Object dePrefix(String k)
	{
		try
		{
			if (k.startsWith("String_"))
			{
				return k.substring("String_".length());
			}
			else if (k.startsWith("Boolean_"))
			{
				return Boolean.parseBoolean(k.substring("Boolean_".length()));
			}
			else if (k.startsWith("Integer_"))
			{
				return Integer.parseInt(k.substring("Integer_".length()));
			}
			else if (k.startsWith("Float_"))
			{
				return Float.parseFloat(k.substring("Float_".length()));
			}
			else if (k.startsWith("Double_"))
			{
				return Double.parseDouble(k.substring("Double_".length()));
			}
			else if (k.startsWith("Long_"))
			{
				return Long.parseLong(k.substring("Long_".length()));
			}
			else if (k.startsWith("JSONArray_"))
			{
				return new JSONArray(k.substring("JSONArray_".length()));
			}
			else if (k.startsWith("JSONObject_"))
			{
				return new JSONObject(k.substring("JSONObject_".length()));
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private boolean isValidDataType(Object obj)
	{
		if (obj instanceof String
			|| obj instanceof Integer
			|| obj instanceof Boolean
			|| obj instanceof Long
			|| obj instanceof Float
			|| obj instanceof Double
			|| obj instanceof JSONObject
			|| obj instanceof JSONArray)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private String addPrefix(Object obj)
	{
		if (obj instanceof String)
		{
			return "String_" + obj.toString();
		}
		else if (obj instanceof Integer)
		{
			return "Integer_" + obj.toString();
		}
		else if (obj instanceof Boolean)
		{
			return "Boolean_" + obj.toString();
		}
		else if (obj instanceof Long)
		{
			return "Long_" + obj.toString();
		}
		else if (obj instanceof Float)
		{
			return "Float_" + obj.toString();
		}
		else if (obj instanceof Double)
		{
			return "Double_" + obj.toString();
		}
		else if (obj instanceof org.json.JSONObject)
		{
			return "JSONObject_" + obj.toString();
		}
		else if (obj instanceof org.json.JSONArray)
		{
			return "JSONArray_" + obj.toString();
		}
		else
		{
			return obj.toString();
		}
	}

	@Override
	public void remove(Object k)
	{
		if (dMap.containsKey(k))
		{
			this.dMap.remove(k);
			nextStep();
		}
		else
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to remove the key \"" + k + "\" and its value: Key doesn't exist!");
		}
	}

	public void clear()
	{
		this.dMap.clear();
		nextStep();
		if (QKVConfig.DEBUG)
		{
			Log.i(LTAG, "Data map cleared!");
		}
	}

	public void persist()
	{
		if (this.dMap.size() > 0)
		{
			try
			{
				JSONObject treeRoot = new JSONObject();
				Iterator iter = this.dMap.entrySet().iterator(); 
				while (iter.hasNext())
				{ 
					Map.Entry entry = (Map.Entry) iter.next(); 
					Object key = entry.getKey(); 
					Object val = entry.getValue(); 

					treeRoot.put(addPrefix(key), addPrefix(val));
				} 
				FileOutputStream kvdbFos = pContext.openFileOutput(dbName == null ?QKVConfig.PKVDB_FILENAME: dbName, Context.MODE_PRIVATE);
				kvdbFos.write(pKey == null ?treeRoot.toString().getBytes(): AES256.encode(pKey, treeRoot.toString()).getBytes());
				kvdbFos.close();
				Log.i(LTAG, "Key-value database persisted!");
			}
			catch (Exception e)
			{
				Log.w(QKVConfig.PUBLIC_LTAG, "Failed to persist current key-value database.");
				if (QKVConfig.DEBUG)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			try
			{
				FileOutputStream kvdbFos = pContext.openFileOutput(dbName == null ?QKVConfig.PKVDB_FILENAME: dbName, Context.MODE_PRIVATE);
				kvdbFos.write("".getBytes());
				kvdbFos.close();
				Log.i(LTAG, "Key-value database persisted!");
			}
			catch (Exception e)
			{
				Log.w(QKVConfig.PUBLIC_LTAG, "Failed to persist current key-value database.");
				if (QKVConfig.DEBUG)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
