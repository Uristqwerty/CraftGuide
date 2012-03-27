package net.minecraft.src.CraftGuide;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;

public class ReflectionAPI
{
	private static List<Object> filters = new LinkedList<Object>();
	public static List<Object> APIObjects = new LinkedList<Object>();
	
	public static void addFilter(Object callback)
	{
		System.out.println("addFilter: Success!");
		
		try {
			callback.getClass().getMethod("allowRecipe", ItemStack.class, ItemStack[].class);
			filters.add(callback);
			return;
		}
		catch(SecurityException e) {
			e.printStackTrace();
		}
		catch(NoSuchMethodException e) {
		}
		
		try {
			callback.getClass().getMethod("allowRecipe", ItemStack[].class);
			filters.add(callback);
		}
		catch(SecurityException e) {
			e.printStackTrace();
		}
		catch(NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public static void applyFilters(Map<ItemStack, List<ICraftGuideRecipe>> craftResults)
	{
		for(Object callback: filters)
		{
			try {
				Method allowRecipe = callback.getClass().getMethod("allowRecipe", ItemStack.class, ItemStack[].class);
				List<ICraftGuideRecipe> removedRecipes = new LinkedList<ICraftGuideRecipe>();

				for(ItemStack type: craftResults.keySet())
				{
					for(ICraftGuideRecipe recipe: craftResults.get(type))
					{
						if(((Boolean)allowRecipe.invoke(callback, type, (Object)recipe.getItems())) == false)
						{
							removedRecipes.add(recipe);
						}
					}
					
					craftResults.get(type).removeAll(removedRecipes);
				}
			}
			catch(SecurityException e) {
				e.printStackTrace();
			}
			catch(NoSuchMethodException e) {
			}
			catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch(IllegalAccessException e) {
				e.printStackTrace();
			}
			catch(InvocationTargetException e) {
				e.printStackTrace();
			}
			
			try {
				Method allowRecipe = callback.getClass().getMethod("allowRecipe", ItemStack[].class);
				List<ICraftGuideRecipe> removedRecipes = new LinkedList<ICraftGuideRecipe>();

				for(ItemStack type: craftResults.keySet())
				{
					for(ICraftGuideRecipe recipe: craftResults.get(type))
					{
						if(((Boolean)allowRecipe.invoke(callback, (Object)recipe.getItems())) == false)
						{
							removedRecipes.add(recipe);
						}
					}
					
					craftResults.get(type).removeAll(removedRecipes);
				}
			}
			catch(SecurityException e) {
				e.printStackTrace();
			}
			catch(NoSuchMethodException e) {
				e.printStackTrace();
			}
			catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch(IllegalAccessException e) {
				e.printStackTrace();
			}
			catch(InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void registerAPIObject(Object object)
	{
		APIObjects.add(object);
	}
	
	public static void reloadRecipes()
	{
		GuiCraftGuide.getInstance().reloadRecipes();
	}
}
