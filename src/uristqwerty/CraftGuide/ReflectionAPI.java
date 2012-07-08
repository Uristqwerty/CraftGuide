package uristqwerty.CraftGuide;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;

import net.minecraft.src.ItemStack;

public class ReflectionAPI
{
	public static List<Object> APIObjects = new LinkedList<Object>();
	private static boolean enabled = true;
	
	public static void registerAPIObject(Object object)
	{
		APIObjects.add(object);
	}
	
	public static void reloadRecipes()
	{
		GuiCraftGuide.getInstance().reloadRecipes();
	}
}
