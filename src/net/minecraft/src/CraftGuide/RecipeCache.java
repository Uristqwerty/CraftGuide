package net.minecraft.src.CraftGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
import net.minecraft.src.CraftGuide.ui.IRecipeCacheListener;

public class RecipeCache
{
	private SortedSet<CraftType> craftingTypes = new TreeSet<CraftType>();
	private Map<CraftType, List<ICraftGuideRecipe>> craftResults = new HashMap<CraftType, List<ICraftGuideRecipe>>();
	private List<ICraftGuideRecipe> typeResults;
	private List<ICraftGuideRecipe> filteredResults;
	private RecipeGenerator generator = new RecipeGenerator();
	private ItemStack filterItem = null;
	private List<IRecipeCacheListener> listeners = new LinkedList<IRecipeCacheListener>();
	
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
		
		Map<ItemStack, List<ICraftGuideRecipe>> rawRecipes = new HashMap<ItemStack, List<ICraftGuideRecipe>>();
		
		rawRecipes.putAll(generator.getRecipes());
		
		ReflectionAPI.applyFilters(rawRecipes);
		
		for(Object object: objects)
		{
			if(object instanceof IRecipeFilter2)
			{
				rawRecipes = ((IRecipeFilter2)object).removeRecipes(rawRecipes);
			}
			else if(object instanceof IRecipeFilter)
			{
				for(ItemStack type: rawRecipes.keySet())
				{
					rawRecipes.put(type, ((IRecipeFilter)object).removeRecipes(rawRecipes.get(type)));
				}
			}
		}
		
		for(ItemStack key: rawRecipes.keySet())
		{
			CraftType type = CraftType.getInstance(key);
			craftResults.put(type, rawRecipes.get(key));
		}
		
		craftingTypes.addAll(craftResults.keySet());
		
		setTypes(null);
	}

	public void setTypes(Set<CraftType> types)
	{
		typeResults = new ArrayList<ICraftGuideRecipe>();
		
		if(types == null)
		{
			for(CraftType type: craftingTypes)
			{
				typeResults.addAll(craftResults.get(type));
			}
		}
		else
		{
			/*Set<CraftType> craftTypes = new HashSet<CraftType>();
			for(ItemStack stack: types)
			{
				craftTypes.add(CraftType.getInstance(stack));
			}*/
			
			for(CraftType type: craftingTypes)
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
		
		for(IRecipeCacheListener listener: listeners)
		{
			listener.onChange();
		}
	}
	
	public ItemStack getFilterItem()
	{
		return filterItem;
	}

	public Set<CraftType> getCraftTypes()
	{
		return craftingTypes;
	}

	public void addListener(IRecipeCacheListener listener)
	{
		listeners.add(listener);
	}
}
