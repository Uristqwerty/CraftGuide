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
	private Set<CraftType> currentTypes = null;
	private SortedSet<CraftType> allItems = new TreeSet<CraftType>();
	
	RecipeCache()
	{
		reset();
	}

	public void reset()
	{
		Map<ItemStack, List<ICraftGuideRecipe>> rawRecipes = generateRecipes();
	
		ReflectionAPI.applyFilters(rawRecipes);
		filterRawRecipes(rawRecipes);
		craftResults.clear();
		
		for(ItemStack key: rawRecipes.keySet())
		{
			CraftType type = CraftType.getInstance(key);
			
			if(!craftResults.containsKey(type))
			{
				craftResults.put(type, new ArrayList<ICraftGuideRecipe>());
			}
			
			craftResults.get(type).addAll(rawRecipes.get(key));
		}
		
		generateAllItemList();
		
		craftingTypes.addAll(craftResults.keySet());
		setTypes(currentTypes);

		for(IRecipeCacheListener listener: listeners)
		{
			listener.onReset(this);
		}
	}

	private void generateAllItemList()
	{
		allItems.clear();
		
		for(List<ICraftGuideRecipe> type: craftResults.values())
		{
			for(ICraftGuideRecipe recipe: type)
			{
				for(ItemStack item: recipe.getItems())
				{
					if(item != null)
					{
						allItems.add(CraftType.getInstance(item));
					}
				}
			}
		}
		
		removeUselessDuplicates();
	}

	private void removeUselessDuplicates()
	{
		Map<CraftType, Integer> wild = new HashMap<CraftType, Integer>();
		for(CraftType item: allItems)
		{
			if(item.getStack().getItemDamage() == -1)
			{
				wild.put(item, 1);
			}
			else
			{
				for(CraftType type: wild.keySet())
				{
					if(item.getStack().itemID == type.getStack().itemID)
					{
						wild.put(type, wild.get(type) + 1);
					}
				}
			}
		}
		
		for(CraftType type: wild.keySet())
		{
			if(wild.get(type) == 2)
			{
				allItems.remove(type);
			}
		}
	}

	private Map<ItemStack, List<ICraftGuideRecipe>> generateRecipes()
	{
		generator.clearRecipes();
		
		for(Object object: ReflectionAPI.APIObjects)
		{
			if(object instanceof IRecipeProvider)
			{
				((IRecipeProvider)object).generateRecipes(generator);
			}
		}

		return generator.getRecipes();
	}

	private void filterRawRecipes(Map<ItemStack, List<ICraftGuideRecipe>> rawRecipes)
	{
		for(Object object: ReflectionAPI.APIObjects)
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
	}

	public void setTypes(Set<CraftType> types)
	{
		typeResults = new ArrayList<ICraftGuideRecipe>();
		currentTypes = types;
		
		if(types == null)
		{
			for(CraftType type: craftingTypes)
			{
				typeResults.addAll(craftResults.get(type));
			}
		}
		else
		{
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
			listener.onChange(this);
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

	public SortedSet<CraftType> getAllItems()
	{
		return allItems;
	}
}
