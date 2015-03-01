/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under GNU GPL v3 License.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.7
 */

package sumimakito.android.quickkv;

public class QKVConfig
{
	public static final boolean DEBUG = false; //Change it to true in development mode.
	public static final String PUBLIC_LTAG = "QuickKV"; //Default log tag. There is no need to change it.
	public static final String PKVDB_FILENAME = "database.kv"; //Default Persistable KVDB filename(no encryption).
	public static final String ECPKVDB_FILENAME = "database_ec.kv"; //Default Encrypted Persistable KVDB filename.
}
