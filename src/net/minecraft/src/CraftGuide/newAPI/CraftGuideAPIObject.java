package net.minecraft.src.CraftGuide.newAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CraftGuideAPIObject
{
	public CraftGuideAPIObject()
	{
		try
		{
			Class c= Class.forName("net.minecraft.src.CraftGuide.ReflectionAPI");
			
			Method m = c.getMethod("registerAPIObject", Object.class);
			m.invoke(null, this);
		}
		catch(ClassNotFoundException e)
		{
		}
		catch(NoSuchMethodException e)
		{
		}
		catch(SecurityException e)
		{
		}
		catch(InvocationTargetException e)
		{
		}
		catch(IllegalAccessException e)
		{
		}
		catch(IllegalArgumentException e)
		{
		}
	}
}
