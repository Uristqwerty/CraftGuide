package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * If crafting recipes are added or removed during the game, this
 * class can be used to trigger a reload, updating the recipe list
 * so that it matches the changes that have occurred.
 */
public class CraftGuideReloadTrigger
{
	public static void reloadRecipes()
	{
		try
		{
			Class c = Class.forName("net.minecraft.src.CraftGuide.ReflectionAPI");
			
			Method m = c.getMethod("reloadRecipes");
			m.invoke(null);
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
