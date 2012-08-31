package uristqwerty.CraftGuide;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
	
	private final Slot[] craftingSlots = new ItemSlot[]{
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
	
	private final Slot[] smallCraftingSlots = new ItemSlot[]{
		new ItemSlot(12, 12, 16, 16).drawOwnBackground(),
		new ItemSlot(30, 12, 16, 16).drawOwnBackground(),
		new ItemSlot(12, 30, 16, 16).drawOwnBackground(),
		new ItemSlot(30, 30, 16, 16).drawOwnBackground(),
		new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
	};
	
	private final Slot[] furnaceSlots = new ItemSlot[]{
		new ItemSlot(13, 21, 16, 16),
		new ItemSlot(50, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT),
	};

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		RecipeTemplate craftingTemplate = generator.createRecipeTemplate(craftingSlots, null);
		RecipeTemplate smallCraftingTemplate = generator.createRecipeTemplate(smallCraftingSlots, null);
		
		RecipeTemplate shapelessTemplate = generator.createRecipeTemplate(
			shapelessCraftingSlots, null,
			"/gui/CraftGuideRecipe.png",  1, 121, 82, 121);
		
		RecipeTemplate furnaceTemplate = generator.createRecipeTemplate(
			furnaceSlots, new ItemStack(Block.stoneOvenActive),
			"/gui/CraftGuideRecipe.png",  1, 181, 82, 181);
		
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
				
				if(items.length == 5)
				{
					generator.addRecipe(templateSmall, items);
				}
				else if(recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe)
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
					System.out.println("CraftGuide DefaultRecipeProvider: Stack trace limit reached, supressing further stack traces for this source");
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
}
