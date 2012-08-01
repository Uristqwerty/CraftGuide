package uristqwerty.CraftGuide;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CraftGuideLog
{
	private static PrintWriter output;
	
	public static void init(File file)
	{
		try
		{
			output = new PrintWriter(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void log(String text)
	{
		output.println(text);
		output.flush();
	}

	public static void log(Throwable e)
	{
		e.printStackTrace(output);
		output.flush();
	}
}
