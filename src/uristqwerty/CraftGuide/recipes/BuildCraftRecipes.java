package uristqwerty.CraftGuide.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.LiquidSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.api.recipes.IAssemblyRecipeManager.IAssemblyRecipe;
import buildcraft.api.recipes.IRefineryRecipeManager.IRefineryRecipe;

public class BuildCraftRecipes extends CraftGuideAPIObject implements RecipeProvider
{
	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			Class silicon = Class.forName("buildcraft.BuildCraftSilicon");
			addAssemblyRecipes(generator,
					new ItemStack((Block)silicon.getField("assemblyTableBlock").get(null)),
					new ItemStack((Block)silicon.getField("laserBlock").get(null)));

			Class factory = Class.forName("buildcraft.BuildCraftFactory");
			addRefineryRecipes(generator,
					new ItemStack((Block)factory.getField("refineryBlock").get(null)));
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

	private void addAssemblyRecipes(RecipeGenerator generator, ItemStack table, ItemStack laser)
	{
		int maxInput = 1;

		for(IAssemblyRecipe recipe: BuildcraftRecipes.assemblyTable.getRecipes())
		{
			maxInput = Math.max(maxInput, recipe.getInputs().length);
		}

		int rows = (maxInput + 2) / 3;
		Slot[] recipeSlots = new Slot[rows * 3 + 3];

		int offset = rows == 1? 18 : rows == 2? 9 : 0;

		for(int i = 0; i < rows; i++)
		{
			recipeSlots[i * 3 + 0] = new ItemSlot( 3, 3 + i * 18 + offset, 16, 16).drawOwnBackground();
			recipeSlots[i * 3 + 1] = new ItemSlot(21, 3 + i * 18 + offset, 16, 16).drawOwnBackground();
			recipeSlots[i * 3 + 2] = new ItemSlot(39, 3 + i * 18 + offset, 16, 16).drawOwnBackground();
		}

		offset = rows <= 3? 3 : 3 + (rows - 3) * 9;

		recipeSlots[rows * 3 + 0] = new ItemSlot(59, offset + 18, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		recipeSlots[rows * 3 + 1] = new ExtraSlot(59, offset +  0, 16, 16, table).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
		recipeSlots[rows * 3 + 2] = new ExtraSlot(59, offset + 36, 16, 16, laser).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, table);
		template.setSize(79, rows > 3? 58 + (rows - 3) * 18 : 58);

		for(IAssemblyRecipe recipe: BuildcraftRecipes.assemblyTable.getRecipes())
		{
			Object[] recipeContents = new Object[rows * 3 + 3];
			Object[] input = recipe.getInputs();
			for(int i = 0; i < Math.min(rows * 3, input.length); i++)
			{
				recipeContents[i] = input[i];
			}

			recipeContents[rows * 3 + 0] = recipe.getOutput();
			recipeContents[rows * 3 + 1] = table;
			recipeContents[rows * 3 + 2] = laser;

			generator.addRecipe(template, recipeContents);
		}
	}

	private void addRefineryRecipes(RecipeGenerator generator, ItemStack refinery)
	{
		Slot[] recipeSlotsOneInput = new Slot[] {
				new LiquidSlot(12, 21),
				new LiquidSlot(50, 21).setSlotType(SlotType.OUTPUT_SLOT),
				new ExtraSlot(31, 21, 16, 16, refinery).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
		};

		Slot[] recipeSlotsTwoInputs = new Slot[] {
				new LiquidSlot(12, 12),
				new LiquidSlot(12, 30),
				new LiquidSlot(50, 21).setSlotType(SlotType.OUTPUT_SLOT),
				new ExtraSlot(31, 21, 16, 16, refinery).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
		};

		RecipeTemplate templateOneInput = generator.createRecipeTemplate(recipeSlotsOneInput, refinery);
		RecipeTemplate templateTwoInputs = generator.createRecipeTemplate(recipeSlotsTwoInputs, refinery);

		for(IRefineryRecipe recipe: BuildcraftRecipes.refinery.getRecipes())
		{
			boolean twoInputs = recipe.getIngredient2() != null;
			Object[] recipeContents = new Object[twoInputs? 4 : 3];
			recipeContents[0] = recipe.getIngredient1();

			if(twoInputs)
			{
				recipeContents[1] = recipe.getIngredient2();
			}

			recipeContents[twoInputs? 2 : 1] = recipe.getResult();
			recipeContents[twoInputs? 3 : 2] = refinery;

			generator.addRecipe(twoInputs? templateTwoInputs : templateOneInput, recipeContents);
		}
	}
}
