package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeFilter;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeFilter2;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeProvider;
import uristqwerty.CraftGuide.ui.IRecipeCacheListener;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.OldAPITranslator;

public class RecipeCache
{
	private SortedSet<CraftType> craftingTypes = new TreeSet<CraftType>();
	private Map<CraftType, List<ICraftGuideRecipe>> craftResults = new HashMap<CraftType, List<ICraftGuideRecipe>>();
	private List<ICraftGuideRecipe> typeResults;
	private List<ICraftGuideRecipe> filteredResults;
	private RecipeGenerator generator = new RecipeGenerator();
	private SingleItemFilter singleFilterItem = new SingleItemFilter();
	private MultipleItemFilter multiFilterItem = new MultipleItemFilter();
	private IItemFilter filterItem = null;
	private List<IRecipeCacheListener> listeners = new LinkedList<IRecipeCacheListener>();
	private Set<CraftType> currentTypes = null;
	private SortedSet<CraftType> allItems = new TreeSet<CraftType>();
	private boolean firstReset = true;
	
	RecipeCache()
	{
		reset();
	}

	public void reset()
	{
		CraftGuideLog.log("(re)loading recipe list...");
		Map<ItemStack, List<ICraftGuideRecipe>> rawRecipes = generateRecipes();
	
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
		
		generator.clearRecipes();
		
		generateAllItemList();
		
		craftingTypes.addAll(craftResults.keySet());
		
		if(firstReset)
		{
			currentTypes = new HashSet<CraftType>();
			currentTypes.addAll(craftingTypes);
			
			for(ItemStack stack: generator.disabledTypes)
			{
				currentTypes.remove(CraftType.getInstance(stack));
			}
			
			firstReset = false;
		}
		
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
				for(Object item: recipe.getItems())
				{
					if(item != null)
					{
						if(item instanceof ItemStack)
						{
							allItems.add(CraftType.getInstance((ItemStack)item));
						}
						else if(item instanceof ArrayList)
						{
							for(ItemStack stack: (ArrayList<ItemStack>)item)
							{
								CraftType craftType = CraftType.getInstance((ItemStack)stack);
								
								if(craftType != null)
								{
									allItems.add(craftType);
								}
							}
							
							CraftType craftType = CraftType.getInstance((ArrayList)item);
							
							if(craftType != null)
							{
								allItems.add(craftType);
							}
						}
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
			if(!(item.getStack() instanceof ItemStack))
			{
				continue;
			}
			
			if(((ItemStack)item.getStack()).getItemDamage() == -1)
			{
				wild.put(item, 1);
			}
			else
			{
				for(CraftType type: wild.keySet())
				{
					if(((ItemStack)item.getStack()).itemID == ((ItemStack)type.getStack()).itemID)
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
		CraftGuideLog.log("  Getting recipes...");
		for(Object object: ReflectionAPI.APIObjects)
		{
			if(object instanceof IRecipeProvider)
			{
				CraftGuideLog.log("    Generating recipes from " + object.getClass().getName());
				try
				{
					((IRecipeProvider)object).generateRecipes(generator);
				}
				catch(Exception e)
				{
					CraftGuideLog.log(e);
				}
			}
		}
		
		OldAPITranslator.generateRecipes(generator);

		return generator.getRecipes();
	}

	private void filterRawRecipes(Map<ItemStack, List<ICraftGuideRecipe>> rawRecipes)
	{
		CraftGuideLog.log("  Filtering recipes...");
		for(Object object: ReflectionAPI.APIObjects)
		{
			if(object instanceof IRecipeFilter2)
			{
				CraftGuideLog.log("    Filtering recipes from " + object.getClass().getName());
				rawRecipes = ((IRecipeFilter2)object).removeRecipes(rawRecipes);
			}
			else if(object instanceof IRecipeFilter)
			{
				CraftGuideLog.log("    Filtering recipes from " + object.getClass().getName());
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
	
	public void filter(IItemFilter filter)
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
	
	public IItemFilter getFilterItem()
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

	public void filter(ItemStack stack)
	{
		if(stack == null)
		{
			filter((IItemFilter)null);
		}
		else
		{
			singleFilterItem.setItem(stack);
			filter(singleFilterItem);
		}
	}
	
	public void filter(ArrayList stack)
	{
		if(stack == null)
		{
			filter((IItemFilter)null);
		}
		else
		{
			multiFilterItem.setItems(stack);
			filter(multiFilterItem);
		}
	}

	public void filter(Object stack)
	{
		if(stack == null)
		{
			filter((IItemFilter)null);
		}
		else if(stack instanceof ItemStack)
		{
			filter((ItemStack)stack);
		}
		else if(stack instanceof ArrayList)
		{
			filter((ArrayList)stack);
		}
	}

	public Set<CraftType> getFilterTypes()
	{
		return currentTypes;
	}
}
