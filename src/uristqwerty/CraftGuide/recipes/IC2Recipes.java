package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.ItemStack;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.EUSlot;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class IC2Recipes extends CraftGuideAPIObject implements RecipeProvider
{
	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			Class itemClass = Class.forName("ic2.common.Ic2Items");

			addCraftingRecipes(generator);

			addMachineRecipes(generator,
					(ItemStack)itemClass.getField("macerator").get(null),
					(List)Class.forName("ic2.common.TileEntityMacerator").getField("recipes").get(null));

			addMachineRecipes(generator,
					(ItemStack)itemClass.getField("compressor").get(null),
					(List)Class.forName("ic2.common.TileEntityCompressor").getField("recipes").get(null));

			addMachineRecipes(generator,
					(ItemStack)itemClass.getField("extractor").get(null),
					(List)Class.forName("ic2.common.TileEntityExtractor").getField("recipes").get(null));
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	private void addCraftingRecipes(RecipeGenerator generator) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
	{
		Class advancedRecipe = Class.forName("ic2.common.AdvRecipe");
		Method canShow = advancedRecipe.getMethod("canShow", advancedRecipe);
		Field outputField = advancedRecipe.getField("output");
		Field inputField = advancedRecipe.getField("input");
		Field widthField = advancedRecipe.getField("inputWidth");

		List recipes = CraftingManager.getInstance().getRecipeList();

		RecipeTemplate template = generator.createRecipeTemplate(
				new Slot[]{
						new ItemSlot( 3,  3, 16, 16).drawOwnBackground(),
						new ItemSlot(21,  3, 16, 16).drawOwnBackground(),
						new ItemSlot(39,  3, 16, 16).drawOwnBackground(),
						new ItemSlot( 3, 21, 16, 16).drawOwnBackground(),
						new ItemSlot(21, 21, 16, 16).drawOwnBackground(),
						new ItemSlot(39, 21, 16, 16).drawOwnBackground(),
						new ItemSlot( 3, 39, 16, 16).drawOwnBackground(),
						new ItemSlot(21, 39, 16, 16).drawOwnBackground(),
						new ItemSlot(39, 39, 16, 16).drawOwnBackground(),
						new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				}, null);

		RecipeTemplate smallTemplate = generator.createRecipeTemplate(
				new Slot[]{
						new ItemSlot(12, 12, 16, 16).drawOwnBackground(),
						new ItemSlot(30, 12, 16, 16).drawOwnBackground(),
						new ItemSlot(12, 30, 16, 16).drawOwnBackground(),
						new ItemSlot(30, 30, 16, 16).drawOwnBackground(),
						new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				}, null);

		for(Object recipe: recipes)
		{
			if(advancedRecipe.isInstance(recipe) && (Boolean)canShow.invoke(null, recipe))
			{
				ItemStack output = (ItemStack)outputField.get(recipe);
				Object[] input = (Object[])inputField.get(recipe);
				int width = (Integer)widthField.get(recipe);

				if(width < 3 && input.length / width < 3)
				{
					addSmallRecipe(generator, smallTemplate, input, output, width);
				}
				else
				{
					addLargeRecipe(generator, template, input, output, width);
				}
			}
		}
	}

	private void addLargeRecipe(RecipeGenerator generator, RecipeTemplate template,
			Object[] input, ItemStack output, int width)
	{
		Object[] recipeContents = new Object[10];

		for(int y = 0; y < input.length / width; y++)
		{
			for(int x = 0; x < width; x++)
			{
				recipeContents[y * 3 + x + (width == 1? 1 : 0) + (width == input.length? 3 : 0)] = resolve(input[y * width + x]);
			}
		}

		recipeContents[9] = output;

		generator.addRecipe(template, recipeContents);
	}

	private void addSmallRecipe(RecipeGenerator generator, RecipeTemplate template,
			Object[] input, ItemStack output, int width)
	{
		Object[] recipeContents = new Object[5];

		for(int y = 0; y < input.length / width; y++)
		{
			for(int x = 0; x < width; x++)
			{
				recipeContents[y * 2 + x] = resolve(input[y * width + x]);
			}
		}

		recipeContents[4] = output;

		generator.addRecipe(template, recipeContents);
	}

	private Object resolve(Object item)
	{
		if(item instanceof String)
		{
			String itemString = (String)item;

			if(itemString.startsWith("liquid$"))
			{
				int colon = itemString.indexOf(':');
				int meta =-1;
				int id;

				if(colon == -1)
				{
					id = Integer.parseInt(itemString.substring(7));
				}
				else
				{
					id = Integer.parseInt(itemString.substring(7, colon - 1));
					meta = Integer.parseInt(itemString.substring(colon + 1));
				}

				ArrayList containers = new ArrayList();
				for(LiquidContainerData container: LiquidContainerRegistry.getRegisteredLiquidContainerData())
				{
					if(container.stillLiquid.itemID == id &&
							(meta == -1 || container.stillLiquid.itemMeta == meta))
					{
						containers.add(container.filled);
					}
				}

				return containers;
			}
			else
			{
				return OreDictionary.getOres(itemString);
			}
		}
		else if(item instanceof ItemStack || item instanceof List)
		{
			return item;
		}
		else
		{
			return null;
		}
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack machine, List recipes)
	{
		Slot[] recipeSlots = new Slot[]{
				new ItemSlot(12, 21, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ExtraSlot(31, 30, 16, 16, machine).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
				new EUSlot(31, 12).setConstantPacketSize(2).setConstantEUValue(-800),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, machine);

		for(Object recipe: recipes)
		{
			generator.addRecipe(
					template,
					new Object[]{
						((Entry)recipe).getKey(),
						((Entry)recipe).getValue(),
						machine,
						null,
					});
		}
	}
}
