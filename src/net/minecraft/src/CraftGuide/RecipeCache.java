package net.minecraft.src.CraftGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.API.IRecipeFilter2;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeProvider;
import net.minecraft.src.CraftGuide.API.IRecipeFilter;

public class RecipeCache
{
	private Map<ItemStack, List<ICraftGuideRecipe>> craftResults = new HashMap<ItemStack, List<ICraftGuideRecipe>>();
	private List<ICraftGuideRecipe> typeResults;
	private List<ICraftGuideRecipe> filteredResults;
	private RecipeGenerator generator = new RecipeGenerator();
	private ItemStack filterItem = null;
	
	RecipeCache()
	{
		Package[] packages = Package.getPackages();
		
		List<Object> objects = ReflectionAPI.APIObjects;
		
		for(Object object: objects)
		{
			if(object instanceof IRecipeProvider)
			{
				((IRecipeProvider)object).generateRecipes(generator);
			}
		}
		
		craftResults.putAll(generator.getRecipes());
		
		ReflectionAPI.applyFilters(craftResults);
		
		for(Object object: objects)
		{
			if(object instanceof IRecipeFilter2)
			{
				for(ItemStack type: craftResults.keySet())
				{
					craftResults.put(type, ((IRecipeFilter)object).removeRecipes(craftResults.get(type)));
				}
			}
			else if(object instanceof IRecipeFilter)
			{
				for(ItemStack type: craftResults.keySet())
				{
					craftResults.put(type, ((IRecipeFilter)object).removeRecipes(craftResults.get(type)));
				}
			}
		}
		
		setTypes(null);
	}

	private void setTypes(Set<ItemStack> types)
	{
		typeResults = new ArrayList<ICraftGuideRecipe>();
		
		if(types == null)
		{
			for(ItemStack type: craftResults.keySet())
			{
				typeResults.addAll(craftResults.get(type));
			}
		}
		else
		{
			for(ItemStack type: craftResults.keySet())
			{
				if(types.contains(type))
				{
					typeResults.addAll(craftResults.get(type));
				}
			}
		}
		
		filter(filterItem);
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
			filteredResults = typeResults;
		}
		else
		{
			filteredResults = new ArrayList<ICraftGuideRecipe>();
			
			for(ICraftGuideRecipe recipe: typeResults)
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
