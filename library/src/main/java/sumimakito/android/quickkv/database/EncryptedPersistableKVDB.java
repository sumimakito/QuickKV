package sumimakito.android.quickkv.database;

import android.content.*;
import android.util.*;
import java.io.*;
import sumimakito.android.quickkv.*;
import sumimakito.android.quickkv.security.*;

public class EncryptedPersistableKVDB extends PersistableKeyValueDatabase
{
	private String pKey=null;
	
	public EncryptedPersistableKVDB(Context context, String key){
		super(context, QKVConfig.ECPKVDB_FILENAME);
		this.pKey=key;
	}
	
	public EncryptedPersistableKVDB(Context context, String databaseName, String key){
		super(context, databaseName);
		this.pKey=key;
	}

	@Override
	public String dump()
	{
		return this.pKey!=null?AES256.encode(pKey, super.dump()):super.dump();
	}

	@Override
	public void sync()
	{
		// Do nothing...
	}

	@Override
	public QKVCallback persist()
	{
		String rawData = super.dump();
		try
		{
			FileOutputStream kvdbFos = super.pContext.openFileOutput(super.dbName == null ?QKVConfig.PKVDB_FILENAME: super.dbName, Context.MODE_PRIVATE);
			kvdbFos.write(pKey!=null?AES256.encode(pKey, rawData).getBytes():rawData.getBytes());
			kvdbFos.close();
			if (QKVConfig.DEBUG)
			{
				Log.i(super.LTAG, "Key-value database persisted!");
			}
			return super.cbkSuccess();
		}
		catch (Exception e)
		{
			if (QKVConfig.DEBUG)
			{
				e.printStackTrace();
			}
			return super.cbkFailed("Failed to persist current key-value database.");
		}
	}
	
	public QKVCallback disableEncryption(){
		if(this.pKey==null){
			return super.cbkFailed("Encryption key not found!");
		}
		try
		{
			FileInputStream kvdbFis = super.pContext.openFileInput(super.dbName == null ?QKVConfig.PKVDB_FILENAME: super.dbName);
			byte[] bytes = new byte[1024];  
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			while (kvdbFis.read(bytes) != -1)
			{  
				baos.write(bytes, 0, bytes.length);  
			}  
			kvdbFis.close();  
			baos.close();  
			String rawData = AES256.decode(pKey, new String(baos.toByteArray()));

			FileOutputStream kvdbFos = super.pContext.openFileOutput(super.dbName == null ?QKVConfig.PKVDB_FILENAME: super.dbName, Context.MODE_PRIVATE);
			kvdbFos.write(rawData.getBytes());
			kvdbFos.close();
			if (QKVConfig.DEBUG)
			{
				Log.i(super.LTAG, "KVDB decrypted!");
			}
			return super.cbkSuccess();
		}
		catch (Exception e)
		{
			if (QKVConfig.DEBUG)
			{
				e.printStackTrace();
			}
			return super.cbkFailed("");
		}
	}
}
