/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under GNU GPL v3 License.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.7
 */

package sumimakito.android.quickkv;

import android.util.*;

public class QKVCallback
{
	public static final int CODE_SUCCESS = 0;
	public static final int CODE_FAILED = -1;

	private boolean success;
	private int code;
	private String msg;

	public QKVCallback()
	{
		this.success = true;
		this.code = CODE_SUCCESS;
		this.msg = "";
	}	
	public QKVCallback(boolean success, int code, String msg)
	{
		this.success = success;
		this.code = code;
		this.msg = msg;
		if (QKVConfig.DEBUG)
		{
			if(success) Log.i(QKVConfig.PUBLIC_LTAG, this.msg);
			else Log.w(QKVConfig.PUBLIC_LTAG, this.msg);
		}
	}	
	public boolean success()
	{
		return this.success;
	}
	public boolean notSuccess()
	{
		return !this.success;
	}
	public int code()
	{
		return this.code;
	}
	public String msg()
	{
		return this.msg;
	}
}
