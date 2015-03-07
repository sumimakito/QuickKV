/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 * @author sumimakito <sumimakito@hotmail.com>
 * @version 0.8
 */

package sumimakito.android.quickkv;

public class QKVConfig
{
	public static final boolean DEBUG = false; //Change it to true in development mode.
	public static final String PUBLIC_LTAG = "QuickKV"; //Default log tag. There is no need to change it.
	public static final String KVDB_FILE_NAME = "database.qkv"; //Default Persistable KVDB filename(no encryption).
	public static final String KVDB_NAME = "database";
	public static final String KVDB_EXT = ".qkv";
	public static final String EC_PREFIX = "__QKVEC_"; // :-/
}