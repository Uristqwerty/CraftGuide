package uristqwerty.CraftGuide.recipes;

import ic2.api.item.Items;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.Recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.DefaultRecipeTemplate;
import uristqwerty.CraftGuide.RecipeGeneratorImplementation;
import uristqwerty.CraftGuide.api.ChanceSlot;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.EUSlot;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.api.StackInfo;
import uristqwerty.gui_craftguide.texture.DynamicTexture;
import uristqwerty.gui_craftguide.texture.TextureClip;

public class IC2Recipes extends CraftGuideAPIObject implements RecipeProvider
{
	public IC2Recipes()
	{
		StackInfo.addSource(new IC2GeneratorFuel());
		StackInfo.addSource(new IC2Power());
		StackInfo.addSource(new IC2Amplifiers());
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			addCraftingRecipes(generator);

			addMachineRecipes(generator, Items.getItem("macerator"), Recipes.macerator);
			addMachineRecipes(generator, Items.getItem("extractor"), Recipes.extractor);
			addMachineRecipes(generator, Items.getItem("compressor"), Recipes.compressor);

			addScrapboxOutput(generator, Items.getItem("scrapBox"), Recipes.scrapboxDrops);
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
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack machine, IMachineRecipeManager<ItemStack> recipeManager)
	{
		Slot[] recipeSlots = new Slot[]{
				new ItemSlot(12, 21, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ExtraSlot(31, 30, 16, 16, machine).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
				new EUSlot(31, 12).setConstantPacketSize(2).setConstantEUValue(-800),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, machine);

		for(Entry<ItemStack, ItemStack> recipe: recipeManager.getRecipes().entrySet())
		{
			generator.addRecipe(
					template,
					new Object[]{
						recipe.getKey(),
						recipe.getValue(),
						machine,
						null,
					});
		}
	}

	private void addScrapboxOutput(RecipeGenerator generator, ItemStack scrapbox, IMachineRecipeManager<Float> recipeManager)
	{
		Slot[] recipeSlots = new Slot[]{
				new ExtraSlot(18, 21, 16, 16, scrapbox).clickable().showName().setSlotType(SlotType.INPUT_SLOT),
				new ChanceSlot(44, 21, 16, 16, true).setFormatString(" (%1$.3f%% chance)").setRatio(100000).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, scrapbox);

		for(Entry<ItemStack, Float> entry: recipeManager.getRecipes().entrySet())
		{
			Object[] recipeContents = new Object[]{
					scrapbox,
					new Object[]{
							entry.getKey(),
							(int)(entry.getValue() * 100000),
					},
			};

			generator.addRecipe(template, recipeContents);
		}
	}

	private void addCraftingRecipes(RecipeGenerator generator) throws ClassNotFoundException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		boolean hideSecretRecipes = Class.forName("ic2.core.IC2").getField("enableSecretRecipeHiding").getBoolean(null);

		Class advancedRecipe = Class.forName("ic2.core.AdvRecipe");
		Field outputField = advancedRecipe.getField("output");
		Field inputField = advancedRecipe.getField("input");
		Field widthField = advancedRecipe.getField("inputWidth");
		Field hidden = advancedRecipe.getField("hidden");

		Class shapelessRecipe = Class.forName("ic2.core.AdvShapelessRecipe");
		Field shapelessInput = shapelessRecipe.getField("input");
		Field shapelessOutput = shapelessRecipe.getField("output");
		Field shapelessHidden = shapelessRecipe.getField("hidden");

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

		RecipeTemplate shapelessTemplate = new DefaultRecipeTemplate(
				new Slot[]{
						new ItemSlot( 3,  3, 16, 16),
						new ItemSlot(21,  3, 16, 16),
						new ItemSlot(39,  3, 16, 16),
						new ItemSlot( 3, 21, 16, 16),
						new ItemSlot(21, 21, 16, 16),
						new ItemSlot(39, 21, 16, 16),
						new ItemSlot( 3, 39, 16, 16),
						new ItemSlot(21, 39, 16, 16),
						new ItemSlot(39, 39, 16, 16),
						new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
				},
				RecipeGeneratorImplementation.workbench,
				new TextureClip(
						DynamicTexture.instance("recipe_backgrounds"),
						1, 121, 79, 58),
				new TextureClip(
						DynamicTexture.instance("recipe_backgrounds"),
						82, 121, 79, 58));

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
			if(advancedRecipe.isInstance(recipe) && (!hideSecretRecipes || !hidden.getBoolean(recipe)))
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
			else if(shapelessRecipe.isInstance(recipe) && (!hideSecretRecipes || !shapelessHidden.getBoolean(recipe)))
			{
				ItemStack output = (ItemStack)shapelessOutput.get(recipe);
				Object[] input = (Object[])shapelessInput.get(recipe);

				addShapelessRecipe(generator, shapelessTemplate, input, output);
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

	private void addShapelessRecipe(RecipeGenerator generator, RecipeTemplate template,
			Object[] input, ItemStack output)
	{
		Object[] recipeContents = new Object[10];

		for(int i = 0; i < Math.min(input.length, 9); i++)
		{
			recipeContents[i] = resolve(input[i]);
		}

		recipeContents[9] = output;
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
				int meta = CraftGuide.DAMAGE_WILDCARD;
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
							(meta == CraftGuide.DAMAGE_WILDCARD || container.stillLiquid.itemMeta == meta))
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
}
