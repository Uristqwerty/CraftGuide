package uristqwerty.CraftGuide.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IMachineRecipeManager.RecipeIoContainer;
import ic2.api.recipe.IScrapboxManager;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.AdvRecipe;
import ic2.core.recipe.AdvShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import uristqwerty.CraftGuide.api.slotTypes.ChanceSlot;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.slotTypes.EUSlot;
import uristqwerty.CraftGuide.api.slotTypes.ExtraSlot;
import uristqwerty.CraftGuide.api.slotTypes.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.slotTypes.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.api.StackInfo;

public class IC2ExperimentalRecipes extends CraftGuideAPIObject implements RecipeProvider
{
	public static interface AdditionalMachines
	{
		public Object[] extraMacerators();
		public Object[] extraExtractors();
		public Object[] extraCompressors();
	}

	public static List<AdditionalMachines> additionalMachines = new ArrayList<>();

	public IC2ExperimentalRecipes()
	{
		StackInfo.addSource(new IC2GeneratorFuel());
		StackInfo.addSource(new IC2Power());
		StackInfo.addSource(new IC2ExperimentalAmplifiers());
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		addCraftingRecipes(generator);

		addMachineRecipes(generator, IC2Items.getItem("te", "macerator"), getMacerator(), Recipes.macerator);
		addMachineRecipes(generator, IC2Items.getItem("te", "extractor"), getExtractor(), Recipes.extractor);
		addMachineRecipes(generator, IC2Items.getItem("te", "compressor"), getCompressor(), Recipes.compressor);
		addMachineRecipes(generator, IC2Items.getItem("te", "centrifuge"), Recipes.centrifuge);
		addMachineRecipes(generator, IC2Items.getItem("te", "block_cutter"), Recipes.blockcutter);
		addMachineRecipes(generator, IC2Items.getItem("te", "blast_furnace"), Recipes.blastfurnace);
		// addMachineRecipes(generator, IC2Items.getItem("recycler"), Recipes.recycler);
		addMachineRecipes(generator, IC2Items.getItem("te", "metal_former"), Recipes.metalformerExtruding);
		addMachineRecipes(generator, IC2Items.getItem("te", "metal_former"), Recipes.metalformerCutting);
		addMachineRecipes(generator, IC2Items.getItem("te", "metal_former"), Recipes.metalformerRolling);
		addMachineRecipes(generator, IC2Items.getItem("te", "ore_washing_plant"), Recipes.oreWashing);

		addScrapboxOutput(generator, IC2Items.getItem("crafting", "scrap_box"), Recipes.scrapboxDrops);
	}

	private Object getMacerator()
	{
		ArrayList<Object> macerator = new ArrayList<>();
		macerator.add(IC2Items.getItem("te", "macerator"));

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
		ArrayList<Object> extractor = new ArrayList<>();
		extractor.add(IC2Items.getItem("te", "extractor"));

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
		ArrayList<Object> compressor = new ArrayList<>();
		compressor.add(IC2Items.getItem("te", "compressor"));

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

		for(RecipeIoContainer recipe: recipeManager.getRecipes())
		{
			maxOutput = Math.max(maxOutput, recipe.output.items.size());
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

		for(RecipeIoContainer recipe: recipeManager.getRecipes())
		{
			ArrayList<ItemStack> inputs = new ArrayList<>();

			for(ItemStack s: recipe.input.getInputs())
			{
				ItemStack stack = s.copy();
				stack.stackSize = recipe.input.getAmount();
				inputs.add(stack);
			}

			Object[] recipeContents = new Object[maxOutput + 3];
			recipeContents[0] = inputs;
			recipeContents[1] = machine;
			recipeContents[2] = null;
			List<ItemStack> output = recipe.output.items;

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

	private void addCraftingRecipes(RecipeGenerator generator)
	{
		ItemStack workbench = new ItemStack(Blocks.CRAFTING_TABLE);
		ConstructedRecipeTemplate smallShaped = generator.buildTemplate(workbench)
				.shapedItemGrid(2, 2).nextColumn(1)
				.outputItem()
				.finishTemplate();

		ConstructedRecipeTemplate shaped = generator.buildTemplate(workbench)
				.shapedItemGrid(3, 3).nextColumn(1)
				.outputItem()
				.finishTemplate();

		ConstructedRecipeTemplate shapeless = generator.buildTemplate(workbench)
				.shapelessItemGrid(3, 3).nextColumn(1)
				.outputItem()
				.finishTemplate();

		for(IRecipe r: CraftingManager.getInstance().getRecipeList())
		{
			if(r instanceof AdvShapelessRecipe)
			{
				AdvShapelessRecipe recipe = (AdvShapelessRecipe)r;
				if(recipe.canShow())
				{
					shapeless.buildRecipe()
						.shapelessItemGrid(AdvRecipe.expandArray(recipe.input))
						.item(recipe.getRecipeOutput())
						.addRecipe(generator);
				}
			}
			else if(r instanceof AdvRecipe)
			{
				AdvRecipe recipe = (AdvRecipe)r;
				if(recipe.canShow())
				{
					int width = recipe.inputWidth;
					int height = recipe.inputHeight;
					Object[] input = expandInput(recipe.input, width, height, recipe.masks[0]);
					ConstructedRecipeTemplate template = width < 3 && height < 3? smallShaped : shaped;

					template.buildRecipe()
						.shapedItemGrid(width, height, AdvRecipe.expandArray(input))
						.item(recipe.getRecipeOutput())
						.addRecipe(generator);
				}
			}
		}
	}

	private Object[] expandInput(Object[] input, int width, int height, int mask)
	{
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
}
