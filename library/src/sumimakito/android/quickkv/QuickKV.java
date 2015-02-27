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
	
	public QuickKV(Context context){
		this.pContext = context;
		this.kvDB = new KeyValueDatabase();
		this.pKVDB = new PersistableKeyValueDatabase(pContext);
		this.pKVDBMap = new HashMap<String, PersistableKeyValueDatabase>();
		this.kvdbMap = new HashMap<String, KeyValueDatabase>();
	}
	
	public PersistableKeyValueDatabase getDefaultPersistableKVDB(){
		return this.pKVDB;
	}
	
	public KeyValueDatabase getDefaultKVDB(){
		return this.kvDB;
	}
	
	public PersistableKeyValueDatabase getPersistableKVDB(String dbName){
		if(!this.pKVDBMap.containsKey(dbName)){
			PersistableKeyValueDatabase pkvd = new PersistableKeyValueDatabase(pContext, dbName);
			this.pKVDBMap.put(dbName, pkvd);
		}
		return this.pKVDBMap.get(dbName);
	}
	
	public KeyValueDatabase getKVDB(String dbAlias){
		if(!this.kvdbMap.containsKey(dbAlias)){
			KeyValueDatabase kvd = new KeyValueDatabase();
			this.kvdbMap.put(dbAlias, kvd);
		}
		return this.kvDB;
	}
	
	public boolean isPersistableKVDBOpened(String dbName){
		if(this.pKVDBMap.containsKey(dbName)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isKVDBOpened(String dbAlias){
		if(this.kvdbMap.containsKey(dbAlias)){
			return true;
		}else{
			return false;
		}
	}
	
	public void releaseKVDB(String dbAlias){
		if(this.kvdbMap.containsKey(dbAlias)){
			kvdbMap.remove(dbAlias);
		}else{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to release kvdb: No opened database matches the given alias \""+dbAlias+"\"!");
		}
	}
	
	public void releasePersistableKVDB(String dbName){
		if(this.pKVDBMap.containsKey(dbName)){
			pKVDBMap.remove(dbName);
		}else{
			Log.w(QKVConfig.PUBLIC_LTAG, "Failed to release persistable kvdb: No opened database matches the given name \""+dbName+"\"!");
		}
	}
	
	public void releaseAllKVDB(){
		this.kvdbMap.clear();
	}
	
	public void releaseAllPersistableKVDB(){
		this.pKVDBMap.clear();
	}
}
