package uristqwerty.CraftGuide.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityFurnace;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.InfoSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;

public class FuelInfo extends CraftGuideAPIObject implements RecipeProvider, FuelInfoSource
{
	private static Map<Long, RecipeTemplate> templateCache = new HashMap<Long, RecipeTemplate>();
	private static List<FuelInfoSource> sources = new ArrayList<FuelInfoSource>();
	private static ItemStack furnace;
	private static ItemStack fire;
	private static String[] format = {
		"\u00a77%1$.3f furnace operations"
	};

	public FuelInfo()
	{
		if(furnace == null)
		{
			furnace = new ItemStack(Block.stoneOvenIdle);
			fire = new ItemStack(Block.fire);
		}

		addSource(this);
	}

	public static void addSource(FuelInfoSource source)
	{
		if(!sources.contains(source))
		{
			sources.add(source);
		}
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();

		for(Item item: Item.itemsList)
		{
			if(item != null)
			{
				if(item.getHasSubtypes())
				{
					list.clear();
					item.getSubItems(item.shiftedIndex, null, list);

					for(ItemStack stack: list)
					{
						generateRecipe(generator, stack);
					}
				}
				else
				{
					ItemStack stack = new ItemStack(item);
					generateRecipe(generator, stack);
				}
			}
		}
	}

	public static void generateRecipe(RecipeGenerator generator, ItemStack stack)
	{
		long sourceMask = getSourceMask(stack);

		if(sourceMask != 0)
		{
			RecipeTemplate template = getTemplate(generator, sourceMask);
			Object[] recipeContents = new Object[2 + countSources(sourceMask)];

			recipeContents[0] = stack;
			recipeContents[1] = fire;

			int n = 2;
			for(int i = 0; i < sources.size(); i++)
			{
				if((sourceMask & (1 << i)) != 0)
				{
					recipeContents[n++] = sources.get(i).getData(stack);
				}
			}

			generator.addRecipe(template, recipeContents);
		}
	}

	private static long getSourceMask(ItemStack stack)
	{
		long mask = 0;

		for(int i = 0; i < sources.size(); i++)
		{
			if(sources.get(i).hasInfo(stack))
			{
				mask |= 1 << i;
			}
		}

		return mask;
	}

	private static RecipeTemplate getTemplate(RecipeGenerator generator, long bitmask)
	{
		if(!templateCache.containsKey(bitmask))
		{
			int size = countSources(bitmask);
			int columns = (size + 2) / 3;
			int xShift = 30 - Math.min(3, (columns - 1)) * 9;
			int yShift = 21 - Math.min(size - 1, 2) * 9;
			Slot[] recipeSlots = new Slot[size + 2];
			FuelInfoSource[] source = new FuelInfoSource[size];
			int n = 0;
			for(int i = 0; i < sources.size(); i++)
			{
				if((bitmask & (1 << i)) != 0)
				{
					source[n++] = sources.get(i);
				}
			}

			recipeSlots[0] = new ItemSlot(xShift, 12, 16, 16, false).drawOwnBackground();
			recipeSlots[1] = new ExtraSlot(xShift, 30, 16, 16, fire);

			for(int i = 0; i < Math.min(size - 1, 2); i++)
			{
				for(int j = 0; j < columns; j++)
				{
					int sourceIndex = i * columns + j;
					recipeSlots[2 + sourceIndex] = source[sourceIndex].getSlot(xShift + 20 + j * 18, yShift + i * 18);
				}
			}

			for(int j = 0; j <= (size - 1) % columns; j++)
			{
				int offset = columns > 1? (columns - (size % columns)) * 9 : 0;
				int i = Math.min(size, 3) - 1;
				int sourceIndex = i * columns + j;
				recipeSlots[2 + sourceIndex] = source[sourceIndex].getSlot(xShift + 20 + j * 18 + offset, yShift + i * 18);
			}

			templateCache.put(bitmask, generator.createRecipeTemplate(recipeSlots, fire));
		}

		return templateCache.get(bitmask);
	}

	private static int countSources(long bitmask)
	{
		int count = 0;

		for(int i = 0; i < sources.size(); i++)
		{
			if((bitmask & (1 << i)) != 0)
			{
				count++;
			}
		}

		return count;
	}

	@Override
	public Slot getSlot(int x, int y)
	{
		return new InfoSlot(x, y, furnace, format);
	}

	@Override
	public boolean hasInfo(ItemStack stack)
	{
		return TileEntityFurnace.getItemBurnTime(stack) > 0;
	}

	@Override
	public Object getData(ItemStack stack)
	{
		return (TileEntityFurnace.getItemBurnTime(stack)) / 200.0;
	}
}
