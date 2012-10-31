package net.minecraft.src.CraftGuide;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.mod_CraftGuide;


public class ReflectionAPI
{
	public static List<Object> APIObjects = new LinkedList<Object>();

	public static void registerAPIObject(Object object)
	{
		APIObjects.add(object);
	}

	public static void reloadRecipes()
	{
		mod_CraftGuide.side.reloadRecipes();
	}
}
