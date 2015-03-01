/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under GNU GPL v3 License.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.7
 */

package sumimakito.android.quickkv.database;

import android.content.*;
import android.util.*;
import java.util.*;
import sumimakito.android.quickkv.*;
import java.io.*;
import org.json.*;
import sumimakito.android.quickkv.security.*;
import sumimakito.android.quickkv.util.*;

public class PersistableKeyValueDatabase
{
	private static String LTAG;
	private HashMap<Object, Object> dMap;
	private Context pContext;
	private int persistInterval = 0;
	private int opStep = 0;
	private String dbName = null;
	private String pKey = null;
	private boolean isCallbackEnabled;

	public PersistableKeyValueDatabase(Context context)
	{
		this.dbName = QKVConfig.PKVDB_FILENAME;
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

	public void setCallbackEnabled(boolean enabled)
	{
		this.isCallbackEnabled = enabled;
	}
	
	public String dump()
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
				return treeRoot.toString();
			}
			catch (Exception e)
			{
				if (QKVConfig.DEBUG)
				{
					Log.w(QKVConfig.PUBLIC_LTAG, "Failed to dump current key-value database.");
					e.printStackTrace();
				}
				return null;
			}
		}
		else
		{
			try
			{
				return new JSONObject().toString();
			}
			catch (Exception e)
			{
				if (QKVConfig.DEBUG)
				{
					Log.w(QKVConfig.PUBLIC_LTAG, "Failed to dump current key-value database.");
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	public QKVCallback sync()
	{
		if(this.loadPKVDB()) return new QKVCallback(true, QKVCallback.CODE_SUCCESS, "Persistable database synchronized!");
		else return new QKVCallback(false, QKVCallback.CODE_FAILED, "Failed to synchronize!");
	}

	@Override
	public QKVCallback put(Object k, Object v)
	{
		if (isValidDataType(k) && isValidDataType(v))
		{
			this.dMap.put(k, v);
			nextStep();
			return cbkSuccess();
		}
		else
		{
			return cbkFailed("Failed to put key \"" + k + "\" and value \"" + v + "\": Invalid data type!");
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
			if (QKVConfig.DEBUG)
			{
				Log.w(QKVConfig.PUBLIC_LTAG, "Failed to get the value for the key \"" + k + "\": Key doesn't exist!");
			}
			return null;
		}
	}

	private void nextStep()
	{
		if (persistInterval != 0)
		{
			if (persistInterval <= opStep)
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

	private boolean loadPKVDB()
	{
		try
		{
			this.dMap.clear();
			FileInputStream kvdbFis = pContext.openFileInput(dbName == null ?QKVConfig.PKVDB_FILENAME: dbName);
			String rawData = FISReader.read(kvdbFis);
			if (QKVConfig.DEBUG)
			{
				Log.i(LTAG, "Database raw:\n"+rawData);
			}
			if (rawData.length() > 0)
			{
				JSONObject treeRoot = new JSONObject(rawData);
				parseJTree(treeRoot);
				if (QKVConfig.DEBUG)
				{
					Log.i(LTAG, "Database file loaded!");
				}
			}
			else if (QKVConfig.DEBUG)
			{
				Log.i(LTAG, "Database file is empty!");
			}
			return true;
		}
		catch (Exception e)
		{
			if(QKVConfig.DEBUG){
				e.printStackTrace();
			}
			return false;
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
				if (QKVConfig.DEBUG)
				{
					Log.w(QKVConfig.PUBLIC_LTAG, "JParser: K="+k+" V="+v);
				}
			}
		}
		catch (Exception e)
		{
			if (QKVConfig.DEBUG)
			{
				Log.w(QKVConfig.PUBLIC_LTAG, "Failed to parse the database file!");
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
	public QKVCallback remove(Object k)
	{
		if (dMap.containsKey(k))
		{
			this.dMap.remove(k);
			nextStep();
			return cbkSuccess();
		}
		else
		{
			return cbkFailed("Failed to remove the key \"" + k + "\" and its value: Key doesn't exist!");
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

	public QKVCallback persist()
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
				kvdbFos.write(treeRoot.toString().getBytes());
				kvdbFos.close();
				if (QKVConfig.DEBUG)
				{
					Log.i(LTAG, "Key-value database persisted!");
				}
				return cbkSuccess();
			}
			catch (Exception e)
			{
				if (QKVConfig.DEBUG)
				{
					e.printStackTrace();
				}
				return cbkFailed("Failed to persist current key-value database.");
			}
		}
		else
		{
			try
			{
				FileOutputStream kvdbFos = pContext.openFileOutput(dbName == null ?QKVConfig.PKVDB_FILENAME: dbName, Context.MODE_PRIVATE);
				kvdbFos.write("".getBytes());
				kvdbFos.close();
				if (QKVConfig.DEBUG)
				{
					Log.i(LTAG, "Key-value database persisted!");
				}
				return cbkSuccess();
			}
			catch (Exception e)
			{

				if (QKVConfig.DEBUG)
				{
					e.printStackTrace();
				}
				return cbkFailed("Failed to persist current key-value database.");
			}
		}
	}

	private QKVCallback cbkSuccess()
	{
		return this.isCallbackEnabled ?new QKVCallback(): null;
	}

	private QKVCallback cbkFailed(String msg)
	{
		return this.isCallbackEnabled ?new QKVCallback(false, QKVCallback.CODE_FAILED, msg): null;
	}
	
	public boolean hasKey(Object k)
	{
		return this.dMap.containsKey(k);
	}

	public boolean hasValue(Object v)
	{
		return this.dMap.containsValue(v);
	}

	public List<Object> getKeys()
	{
		List<Object> list = new ArrayList<Object>();
		if (this.dMap.size() > 0)
		{
			Iterator iter = dMap.entrySet().iterator(); 
			while (iter.hasNext())
			{ 
				Map.Entry entry = (Map.Entry) iter.next(); 
				Object key = entry.getKey(); 
				list.add(key);
			} 
		}
		return list;
	}

	public List<Object> getValues()
	{
		List<Object> list = new ArrayList<Object>();
		if (this.dMap.size() > 0)
		{
			Iterator iter = dMap.entrySet().iterator(); 
			while (iter.hasNext())
			{ 
				Map.Entry entry = (Map.Entry) iter.next(); 
				Object value = entry.getValue(); 
				list.add(value);
			} 
		}
		return list;
	}
}
