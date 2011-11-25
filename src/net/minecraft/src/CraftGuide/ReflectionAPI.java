package net.minecraft.src.CraftGuide;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;

public class ReflectionAPI
{
	private static List<Object> filters = new LinkedList<Object>();
	
	public static void addFilter(Object callback)
	{
		System.out.println("addFilter: Success!");
		
		try {
			callback.getClass().getMethod("allowRecipe", ItemStack[].class);
			filters.add(callback);
			System.out.println("addFilter: callback has proper method, too!");
		}
		catch(SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void applyFilters(List<ICraftGuideRecipe> craftResults)
	{
		for(Object callback: filters)
		{
			try {
				Method allowRecipe = callback.getClass().getMethod("allowRecipe", ItemStack[].class);
				List<ICraftGuideRecipe> removedRecipes = new LinkedList<ICraftGuideRecipe>();

				for(ICraftGuideRecipe recipe: craftResults)
				{
					if(((Boolean)allowRecipe.invoke(callback, (Object)recipe.getItems())) == false)
					{
						removedRecipes.add(recipe);
					}
				}
				
				craftResults.removeAll(removedRecipes);
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
}
