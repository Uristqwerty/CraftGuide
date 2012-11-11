package uristqwerty.CraftGuide;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ShapelessRecipes;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.gui.texture.DynamicTexture;
import uristqwerty.gui.texture.TextureClip;

public class DefaultRecipeProvider extends CraftGuideAPIObject implements RecipeProvider
{
	private final Slot[] shapelessCraftingSlots = new ItemSlot[]{
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
	};

	private final Slot[] craftingSlotsOwnBackground = new ItemSlot[]{
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
	};

	private final Slot[] smallCraftingSlotsOwnBackground = new ItemSlot[]{
		new ItemSlot(12, 12, 16, 16).drawOwnBackground(),
		new ItemSlot(30, 12, 16, 16).drawOwnBackground(),
		new ItemSlot(12, 30, 16, 16).drawOwnBackground(),
		new ItemSlot(30, 30, 16, 16).drawOwnBackground(),
		new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
	};

	private final Slot[] craftingSlots = new ItemSlot[]{
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
	};

	private final Slot[] smallCraftingSlots = new ItemSlot[]{
		new ItemSlot(12, 12, 16, 16),
		new ItemSlot(30, 12, 16, 16),
		new ItemSlot(12, 30, 16, 16),
		new ItemSlot(30, 30, 16, 16),
		new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
	};

	private final Slot[] furnaceSlots = new ItemSlot[]{
		new ItemSlot(13, 21, 16, 16),
		new ItemSlot(50, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
	};

	private static ItemStack workbench = new ItemStack(Block.workbench);

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		RecipeTemplate craftingTemplate;
		RecipeTemplate smallCraftingTemplate;

		if(CraftGuide.newerBackgroundStyle)
		{
			craftingTemplate = generator.createRecipeTemplate(craftingSlotsOwnBackground, null);
			smallCraftingTemplate = generator.createRecipeTemplate(smallCraftingSlotsOwnBackground, null);
		}
		else
		{
			craftingTemplate = new DefaultRecipeTemplate(
					craftingSlots, workbench,
					new TextureClip(
							DynamicTexture.instance("recipe_backgrounds"),
							1, 1, 79, 58),
					new TextureClip(
							DynamicTexture.instance("recipe_backgrounds"),
							82, 1, 79, 58));

			smallCraftingTemplate = new DefaultRecipeTemplate(
					smallCraftingSlots, workbench,
					new TextureClip(
							DynamicTexture.instance("recipe_backgrounds"),
							1, 61, 79, 58),
					new TextureClip(
							DynamicTexture.instance("recipe_backgrounds"),
							82, 61, 79, 58));
		}

		RecipeTemplate shapelessTemplate = new DefaultRecipeTemplate(
					shapelessCraftingSlots,
					workbench,
					new TextureClip(
							DynamicTexture.instance("recipe_backgrounds"),
							1, 121, 79, 58),
					new TextureClip(
							DynamicTexture.instance("recipe_backgrounds"),
							82, 121, 79, 58));

		RecipeTemplate furnaceTemplate = new DefaultRecipeTemplate(
				furnaceSlots,
				new ItemStack(Block.stoneOvenActive),
				new TextureClip(
						DynamicTexture.instance("recipe_backgrounds"),
						1, 181, 79, 58),
				new TextureClip(
						DynamicTexture.instance("recipe_backgrounds"),
						82, 181, 79, 58));

		addCraftingRecipes(craftingTemplate, smallCraftingTemplate, shapelessTemplate, generator);
		addFurnaceRecipes(furnaceTemplate, generator);
	}

	private void addFurnaceRecipes(RecipeTemplate template, RecipeGenerator generator)
	{
		Map furnaceRecipes = FurnaceRecipes.smelting().getSmeltingList();

		for(Object o: furnaceRecipes.keySet())
		{
			int blockID = (Integer)o;
			ItemStack in = new ItemStack(blockID, 1, -1);
			ItemStack out = (ItemStack)furnaceRecipes.get(o);

			generator.addRecipe(template, new ItemStack[]{in, out});
		}


		try
		{
			Field forgeMetadataSmelting = FurnaceRecipes.class.getDeclaredField("metaSmeltingList");
			forgeMetadataSmelting.setAccessible(true);
			Map<List<Integer>, ItemStack> recipes = (Map<List<Integer>, ItemStack>)forgeMetadataSmelting.get(FurnaceRecipes.smelting());

			for(List<Integer> input: recipes.keySet())
			{
				int blockID = input.get(0);
				int metadata = input.get(1);
				ItemStack in = new ItemStack(blockID, 1, metadata);
				ItemStack out = recipes.get(input);

				generator.addRecipe(template, new ItemStack[]{in, out});
			}
		}
		catch(NoSuchFieldException e){}
		catch(IllegalAccessException e){}
	}

	private void addCraftingRecipes(RecipeTemplate template, RecipeTemplate templateSmall, RecipeTemplate templateShapeless, RecipeGenerator generator)
	{
		List recipes = CraftingManager.getInstance().getRecipeList();

		int errCount = 0;

		for(Object o: recipes)
		{
			try
			{
				IRecipe recipe = (IRecipe)o;

				Object[] items = generator.getCraftingRecipe(recipe, true);

				if(items == null)
				{
					continue;
				}
				else if(items.length == 5)
				{
					generator.addRecipe(templateSmall, items);
				}
				else if(isShapelessRecipe(recipe))
				{
					generator.addRecipe(templateShapeless, items);
				}
				else
				{
					generator.addRecipe(template, items);
				}
			}
			catch(Exception e)
			{
				if(errCount == -1)
				{
				}
				else if(errCount++ >= 5)
				{
					CraftGuideLog.log("CraftGuide DefaultRecipeProvider: Stack trace limit reached, further stack traces from this invocation will not be logged to the console. They will still be logged to (.minecraft)/config/CraftGuide/CraftGuide.log", true);
					errCount = -1;
				}
				else
				{
					e.printStackTrace();
				}

				CraftGuideLog.log(e);
			}
		}
	}

	private boolean isShapelessRecipe(IRecipe recipe)
	{
		return recipe instanceof ShapelessRecipes ||
			(RecipeGeneratorImplementation.forgeExt != null
					&& RecipeGeneratorImplementation.forgeExt.isShapelessRecipe(recipe));
	}
}
