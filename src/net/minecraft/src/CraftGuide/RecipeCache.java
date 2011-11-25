package net.minecraft.src.CraftGuide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeProvider;
import net.minecraft.src.CraftGuide.API.IRecipeFilter;

public class RecipeCache
{
	private List<ICraftGuideRecipe> craftResults = new ArrayList<ICraftGuideRecipe>();
	private List<ICraftGuideRecipe> filteredResults;
	private RecipeGenerator generator = new RecipeGenerator();
	private ItemStack filterItem;
	
	RecipeCache()
	{
		Package[] packages = Package.getPackages();
		
		List<Object> objects = new LinkedList<Object>();
		
		for(Package pack: packages)
		{
			try{
				objects.add(Class.forName(pack.getName() + ".CraftGuideAPIHook").getConstructor().newInstance());
			} catch(Exception e) {}
		}
		
		for(Object object: objects)
		{
			if(object instanceof IRecipeProvider)
			{
				((IRecipeProvider)object).generateRecipes(generator);
			}
		}
		
		craftResults.addAll(generator.getRecipes());
		
		ReflectionAPI.applyFilters(craftResults);
		
		for(Object object: objects)
		{
			if(object instanceof IRecipeFilter)
			{
				craftResults = ((IRecipeFilter)object).removeRecipes(craftResults);
			}
		}
		
		filter(null);
	}

	public List<ICraftGuideRecipe> getRecipes()
	{
		return filteredResults;
	}
	
	public void filter(ItemStack filter)
	{
		filterItem = filter;
		
		if(filter == null)
		{
			filteredResults = craftResults;
		}
		else
		{
			filteredResults = new ArrayList<ICraftGuideRecipe>();
			
			for(ICraftGuideRecipe recipe: craftResults)
			{
				if(recipe.containsItem(filter))
				{
					filteredResults.add(recipe);
				}
			}
		}
	}
	
	public ItemStack getFilterItem()
	{
		return filterItem;
	}
}
