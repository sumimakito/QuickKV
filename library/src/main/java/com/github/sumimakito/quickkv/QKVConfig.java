/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.8.2
 */

package com.github.sumimakito.quickkv;

public class QKVConfig
{
	public static final String STRUCT_VER_STRING = "0.9_rev1";
	public static final boolean DEBUG = false; //Change it to true in development mode.
	public static final String PUBLIC_LTAG = "QuickKV"; //Default log tag. There is no need to change it.
	public static final String KVDB_FILE_NAME = "database.qkv"; //Default KVDB filename
	public static final String KVDB_NAME = "database"; //Name (must match above)
	public static final String KVDB_EXT = ".qkv"; //Ext (must match above above)
	public static final String EC_PREFIX = "__QKVEC_"; // :-/ Abandoned
}
