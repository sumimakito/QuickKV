/**
 * QucikKV
 * Copyright 2014-2016 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 */

package com.github.sumimakito.quickkv;

public class QKVConfig
{
	public static final String STRUCT_VER_STRING = "1.0.0_A";
	public static final boolean DEBUG = false; //Change it to true in development mode.
	public static final String PUBLIC_LTAG = "QuickKV"; //Default log tag. There is no need to change it.
	public static final String KVDB_FILE_NAME = "database.qkv"; //Default KVDB filename
	public static final String KVDB_NAME = "database"; //Name (must match above)
	public static final String KVDB_EXT = ".qkv"; //Ext (must match above above)
	public static final String EC_PREFIX = "__QKVEC_"; // :-/ Abandoned
}
