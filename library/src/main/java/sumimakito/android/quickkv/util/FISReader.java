package sumimakito.android.quickkv.util;
import java.io.*;

public class FISReader
{
	public static String read(FileInputStream instream){
		try
		{
			StringBuilder sb = new StringBuilder();
			if (instream != null)
			{
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				while ((line = buffreader.readLine()) != null)
				{
					sb.append(line);
				}
				instream.close();
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
