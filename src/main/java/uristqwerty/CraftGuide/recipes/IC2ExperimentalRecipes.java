package uristqwerty.CraftGuide.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IScrapboxManager;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.oredict.OreDictionary;
import uristqwerty.CraftGuide.CraftGuideLog;
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

public class IC2ExperimentalRecipes extends CraftGuideAPIObject implements RecipeProvider
{
	public static interface AdditionalMachines
	{
		public Object[] extraMacerators();
		public Object[] extraExtractors();
		public Object[] extraCompressors();
	}

	public static List<AdditionalMachines> additionalMachines = new ArrayList<AdditionalMachines>();

	public IC2ExperimentalRecipes()
	{
		StackInfo.addSource(new IC2GeneratorFuel());
		StackInfo.addSource(new IC2Power());
		StackInfo.addSource(new IC2ExperimentalAmplifiers());
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			addCraftingRecipes(generator);

			addMachineRecipes(generator, IC2Items.getItem("macerator"), getMacerator(), Recipes.macerator);
			addMachineRecipes(generator, IC2Items.getItem("extractor"), getExtractor(), Recipes.extractor);
			addMachineRecipes(generator, IC2Items.getItem("compressor"), getCompressor(), Recipes.compressor);
			addMachineRecipes(generator, IC2Items.getItem("centrifuge"), Recipes.centrifuge);
			addMachineRecipes(generator, IC2Items.getItem("blockcutter"), Recipes.blockcutter);
			addMachineRecipes(generator, IC2Items.getItem("blastfurance"), Recipes.blastfurance);
			addMachineRecipes(generator, IC2Items.getItem("recycler"), Recipes.recycler);
			addMachineRecipes(generator, IC2Items.getItem("metalformer"), Recipes.metalformerExtruding);
			addMachineRecipes(generator, IC2Items.getItem("metalformer"), Recipes.metalformerCutting);
			addMachineRecipes(generator, IC2Items.getItem("metalformer"), Recipes.metalformerRolling);
			addMachineRecipes(generator, IC2Items.getItem("orewashingplant"), Recipes.oreWashing);

			addScrapboxOutput(generator, IC2Items.getItem("scrapBox"), Recipes.scrapboxDrops);
		}
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(SecurityException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(InvocationTargetException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	private Object getMacerator()
	{
		ArrayList<Object> macerator = new ArrayList<Object>();
		macerator.add(IC2Items.getItem("macerator"));

		for(AdditionalMachines additional: additionalMachines)
		{
			Object machines[] = additional.extraMacerators();

			if(machines != null)
			{
				for(Object machine: machines)
				{
					macerator.add(machine);
				}
			}
		}

		return macerator;
	}

	private Object getExtractor()
	{
		ArrayList<Object> extractor = new ArrayList<Object>();
		extractor.add(IC2Items.getItem("extractor"));

		for(AdditionalMachines additional: additionalMachines)
		{
			Object machines[] = additional.extraExtractors();

			if(machines != null)
			{
				for(Object machine: machines)
				{
					extractor.add(machine);
				}
			}
		}

		return extractor;
	}

	private Object getCompressor()
	{
		ArrayList<Object> compressor = new ArrayList<Object>();
		compressor.add(IC2Items.getItem("compressor"));

		for(AdditionalMachines additional: additionalMachines)
		{
			Object machines[] = additional.extraCompressors();

			if(machines != null)
			{
				for(Object machine: machines)
				{
					compressor.add(machine);
				}
			}
		}

		return compressor;
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack type, IMachineRecipeManager recipeManager)
	{
		addMachineRecipes(generator, type, type, recipeManager);
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack type, Object machine, IMachineRecipeManager recipeManager)
	{
		addMachineRecipes(generator, type, machine, recipeManager, 2, 800);
	}

	private void addMachineRecipes(RecipeGenerator generator, ItemStack type, Object machine, IMachineRecipeManager recipeManager, int eut, int totalEU)
	{
		if(recipeManager == null || recipeManager.getRecipes() == null)
			return;

		int maxOutput = 1;

		for(RecipeOutput output: recipeManager.getRecipes().values())
		{
			maxOutput = Math.max(maxOutput, output.items.size());
		}

		int columns = (maxOutput+1) / 2;

		Slot[] recipeSlots = new Slot[maxOutput + 3];

		recipeSlots[0] = new ItemSlot(columns > 1? 3 : 12, 21, 16, 16, true).drawOwnBackground();
		recipeSlots[1] = new ExtraSlot(columns > 1? 23 : 31, 30, 16, 16, machine).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
		recipeSlots[2] = new EUSlot(columns > 1? 23 : 31, 12).setConstantPacketSize(eut).setConstantEUValue(-totalEU);

		for(int i = 0; i < maxOutput/2; i++)
		{
			recipeSlots[i * 2 + 3] = new ItemSlot((columns > 1? 41 : 50) + i * 18, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			recipeSlots[i * 2 + 4] = new ItemSlot((columns > 1? 41 : 50) + i * 18, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		}

		if((maxOutput & 1) == 1)
		{
			recipeSlots[columns * 2 + 1] = new ItemSlot((columns > 1? 23 : 32) + columns * 18, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		}

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, type);

		for(Entry<IRecipeInput, RecipeOutput> recipe: recipeManager.getRecipes().entrySet())
		{
			ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();

			for(ItemStack s: recipe.getKey().getInputs())
			{
				ItemStack stack = s.copy();
				stack.stackSize = recipe.getKey().getAmount();
				inputs.add(stack);
			}

			Object[] recipeContents = new Object[maxOutput + 3];
			recipeContents[0] = inputs;
			recipeContents[1] = machine;
			recipeContents[2] = null;
			List<ItemStack> output = recipe.getValue().items;

			for(int i = 0; i < Math.min(maxOutput, output.size()); i++)
			{
				recipeContents[i + 3] = output.get(i);
			}

			generator.addRecipe(template, recipeContents);
		}
	}

	private void addScrapboxOutput(RecipeGenerator generator, ItemStack scrapbox, IScrapboxManager scrapboxDrops)
	{
		Slot[] recipeSlots = new Slot[]{
				new ExtraSlot(18, 21, 16, 16, scrapbox).clickable().showName().setSlotType(SlotType.INPUT_SLOT),
				new ChanceSlot(44, 21, 16, 16, true).setFormatString(" (%1$.3f%% chance)").setRatio(100000).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, scrapbox);

		for(Entry<ItemStack, Float> entry: scrapboxDrops.getDrops().entrySet())
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

	private void addCraftingRecipes(RecipeGenerator generator) throws ClassNotFoundException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException
	{
		Class<?> advancedRecipe = Class.forName("ic2.core.AdvRecipe");
		Field outputField = advancedRecipe.getField("output");
		Field inputField = advancedRecipe.getField("input");
		Field widthField = advancedRecipe.getField("inputWidth");
		Method canShow = advancedRecipe.getMethod("canShow");
		Field maskField = null;

		try
		{
			maskField = advancedRecipe.getField("masks");
		}
		catch(NoSuchFieldException e){}

		Class<?> shapelessRecipe = Class.forName("ic2.core.AdvShapelessRecipe");
		Field shapelessInput = shapelessRecipe.getField("input");
		Field shapelessOutput = shapelessRecipe.getField("output");
		Method shapelessCanShow = shapelessRecipe.getMethod("canShow");

		List<?> recipes = CraftingManager.getInstance().getRecipeList();

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
			if(advancedRecipe.isInstance(recipe) && (Boolean)canShow.invoke(recipe))
			{
				ItemStack output = (ItemStack)outputField.get(recipe);
				Object[] input = (Object[])inputField.get(recipe);
				int width = (Integer)widthField.get(recipe);

				if(maskField != null)
				{
					input = expandInput(input, width, ((int[])maskField.get(recipe))[0]);
				}

				if(width < 3 && input.length / width < 3)
				{
					addSmallRecipe(generator, smallTemplate, input, output, width);
				}
				else
				{
					addLargeRecipe(generator, template, input, output, width);
				}
			}
			else if(shapelessRecipe.isInstance(recipe) && (Boolean)shapelessCanShow.invoke(recipe))
			{
				ItemStack output = (ItemStack)shapelessOutput.get(recipe);
				Object[] input = (Object[])shapelessInput.get(recipe);

				addShapelessRecipe(generator, shapelessTemplate, input, output);
			}
		}
	}

	private Object[] expandInput(Object[] input, int width, int mask)
	{
		int height = ((mask & 0x007) != 0? 1 : 0) + ((mask & 0x038) != 0? 1 : 0) + ((mask & 0x1c0) != 0? 1 : 0);

		Object[] expanded = new Object[width * height];
		int i = 0;
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if((mask & (1 << (8 - (y * 3 + x)))) != 0)
				{
					expanded[y * width + x] = input[i++];
				}
			}
		}

		return expanded;
	}

	private void addLargeRecipe(RecipeGenerator generator, RecipeTemplate template,
			Object[] input, ItemStack output, int width)
	{
		Object[] recipeContents = new Object[10];

		int hShift = width == 1? 1 : 0;
		int vShift = width == input.length? 1 : 0;

		for(int y = 0; y < input.length / width; y++)
		{
			for(int x = 0; x < width; x++)
			{
				recipeContents[(y + vShift) * 3 + x + hShift] = resolve(input[y * width + x]);
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
				String fluidName = itemString.substring(7);

				ArrayList<Object> containers = new ArrayList<Object>();
				for(FluidContainerData container: FluidContainerRegistry.getRegisteredFluidContainerData())
				{
					if(container.fluid.getFluid().getName().equals(fluidName))
					{
						containers.add(container.filledContainer);
					}
				}

				return containers;
			}
			else
			{
				return OreDictionary.getOres(itemString);
			}
		}
		else if(item instanceof ItemStack)
		{
			return item;
		}
		else if(item instanceof IRecipeInput)
		{
			return ((IRecipeInput)item).getInputs();
		}
		else if(item instanceof List)
		{
			boolean containsItemStacks = true;

			for(Object o: (List<?>)item)
			{
				if(!(o instanceof ItemStack))
				{
					containsItemStacks = false;
					break;
				}
			}

			if(containsItemStacks)
				return item;

			ArrayList<Object> newlist = new ArrayList<Object>(((List<?>)item).size());

			for(Object o: (List<?>)item)
			{
				Object r = resolve(o);

				if(r instanceof Collection)
				{
					newlist.addAll((Collection<?>)r);
				}
				else
				{
					newlist.add(r);
				}
			}

			return newlist;
		}
		else if(item instanceof Iterable)
		{
			ArrayList<Object> newlist = new ArrayList<Object>();

			for(Object o: (Iterable<?>)item)
			{
				Object r = resolve(o);

				if(r instanceof Collection)
				{
					newlist.addAll((Collection<?>)r);
				}
				else
				{
					newlist.add(r);
				}
			}

			return newlist;
		}
		else if(item != null && item.getClass().isArray())
		{
			ArrayList<Object> newlist = new ArrayList<Object>();

			for(Object o: (Object[])item)
			{
				Object r = resolve(o);

				if(r instanceof Collection)
				{
					newlist.addAll((Collection<?>)r);
				}
				else
				{
					newlist.add(r);
				}
			}

			return newlist;
		}
		else
		{
			return null;
		}
	}
}
