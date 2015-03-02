/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under GNU GPL v3 License.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.7
 */

package sumimakito.android.quickkv.database;

import java.util.*;
import sumimakito.android.quickkv.*;
import android.util.*;

public class KeyValueDatabase extends ADatabase
{
	private static String LTAG;
	private HashMap<Object, Object> dMap;

	public KeyValueDatabase()
	{
		this.LTAG = "_" + this.getClass().getSimpleName();
		this.dMap = new HashMap<Object, Object>();
		if (QKVConfig.DEBUG)
		{
			Log.i(LTAG, "Data map created!");
		}
	}

	@Override
	public void put(Object k, Object v)
	{
		this.dMap.put(k, v);
		if (QKVConfig.DEBUG)
		{
			Log.i(LTAG, "put(): K=" + k + " V=" + v);
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

	@Override
	public void remove(Object k)
	{
		if (dMap.containsKey(k))
		{
			this.dMap.remove(k);
		}
		else if (QKVConfig.DEBUG)
		{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to remove the key \"" + k + "\" and its value: Key doesn't exist!");
		}
	}

	public void clear()
	{
		this.dMap.clear();
		if (QKVConfig.DEBUG)
		{
			Log.i(LTAG, "Database cleared!");
		}
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
