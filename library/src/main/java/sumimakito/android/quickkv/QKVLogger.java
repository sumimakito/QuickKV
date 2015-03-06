package sumimakito.android.quickkv;

import android.util.*;

public class QKVLogger
{
	public static void log(String level, String msg)
	{
		if (QKVConfig.DEBUG)
		{
			if (level.equals("i"))
			{
				Log.i(QKVConfig.PUBLIC_LTAG, msg);
			}
			else if (level.equals("w"))
			{
				Log.w(QKVConfig.PUBLIC_LTAG, msg);
			}
		}
	}
	public static void ex(Exception e)
	{
		if (QKVConfig.DEBUG)
		{
			e.printStackTrace();
		}
	}
}
