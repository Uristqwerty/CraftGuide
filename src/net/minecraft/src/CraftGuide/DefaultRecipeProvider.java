package net.minecraft.src.CraftGuide;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraft.src.CraftGuide.API.CraftGuideAPIObject;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeProvider;
import net.minecraft.src.CraftGuide.API.IRecipeFilter;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;

public class DefaultRecipeProvider extends CraftGuideAPIObject implements IRecipeProvider
{
	private final boolean obfuscatedNames = true;
	
	private final ItemSlot[] craftingSlots = new ItemSlot[]{
		new ItemSlot( 3,  3, 16, 16, 0),
		new ItemSlot(21,  3, 16, 16, 1),
		new ItemSlot(39,  3, 16, 16, 2),
		new ItemSlot( 3, 21, 16, 16, 3),
		new ItemSlot(21, 21, 16, 16, 4),
		new ItemSlot(39, 21, 16, 16, 5),
		new ItemSlot( 3, 39, 16, 16, 6),
		new ItemSlot(21, 39, 16, 16, 7),
		new ItemSlot(39, 39, 16, 16, 8),
		new ItemSlot(59, 21, 16, 16, 9, true),
	};
	
	private final ItemSlot[] smallCraftingSlots = new ItemSlot[]{
		new ItemSlot(12, 12, 16, 16, 0),
		new ItemSlot(30, 12, 16, 16, 1),
		new ItemSlot(12, 30, 16, 16, 2),
		new ItemSlot(30, 30, 16, 16, 3),
		new ItemSlot(59, 21, 16, 16, 4, true),
	};
	
	private final ItemSlot[] furnaceSlots = new ItemSlot[]{
		new ItemSlot(13, 21, 16, 16, 0),
		new ItemSlot(50, 21, 16, 16, 1, true),
	};

	@Override
	public void generateRecipes(IRecipeGenerator generator)
	{
		IRecipeTemplate craftingTemplate = generator.createRecipeTemplate(
			craftingSlots,
			"/gui/CraftGuideRecipe.png",  1, 1, 82, 1);
		
		IRecipeTemplate shapelessTemplate = generator.createRecipeTemplate(
			craftingSlots,
			"/gui/CraftGuideRecipe.png",  1, 121, 82, 121);
		
		IRecipeTemplate smallCraftingTemplate = generator.createRecipeTemplate(
			smallCraftingSlots,
			"/gui/CraftGuideRecipe.png",  1, 61, 82, 61);
		
		IRecipeTemplate furnaceTemplate = generator.createRecipeTemplate(
			furnaceSlots, new ItemStack(Block.stoneOvenActive),
			"/gui/CraftGuideRecipe.png",  1, 181, 82, 181);
		
		addCraftingRecipes(craftingTemplate, smallCraftingTemplate, shapelessTemplate, generator);
		addFurnaceRecipes(furnaceTemplate, generator);
	}
	
	private void addFurnaceRecipes(IRecipeTemplate template, IRecipeGenerator generator)
	{
		Map furnaceRecipes = FurnaceRecipes.smelting().getSmeltingList();
		
		for(Object o: furnaceRecipes.keySet())
		{
			int blockID = (Integer)o;
			ItemStack in = new ItemStack(blockID, 1, 0);
			ItemStack out = (ItemStack)furnaceRecipes.get(o);
			
			generator.addRecipe(template, new ItemStack[]{in, out});
		}
		
		try
		{
			Field forgeMetadataSmelting = FurnaceRecipes.class.getDeclaredField("metaSmeltingList");
			forgeMetadataSmelting.setAccessible(true);
			Map recipes = (Map)forgeMetadataSmelting.get(FurnaceRecipes.smelting());
			
			for(Object o: recipes.keySet())
			{
				List input = (List)o;
				int blockID = (Integer)input.get(0);
				int metadata = (Integer)input.get(1);
				ItemStack in = new ItemStack(blockID, 1, metadata);
				ItemStack out = (ItemStack)recipes.get(o);
				
				generator.addRecipe(template, new ItemStack[]{in, out});
			}
		}
		catch(NoSuchFieldException e){}
		catch(IllegalAccessException e){}
	}
	
	private void addCraftingRecipes(IRecipeTemplate template, IRecipeTemplate templateSmall, IRecipeTemplate templateShapeless, IRecipeGenerator generator)
	{
		List recipes = CraftingManager.getInstance().getRecipeList();
		
		for(Object o: recipes)
		{
			IRecipe recipe = (IRecipe)o;
			
			if(recipe instanceof ShapedRecipes)
			{
				addShapedRecipe((ShapedRecipes)recipe, template, templateSmall, generator);
			}
			else if(recipe instanceof ShapelessRecipes)
			{
				addShapelessRecipe((ShapelessRecipes)recipe, templateShapeless, generator);
			}
		}
	}
	
	private void addShapelessRecipe(ShapelessRecipes recipe, IRecipeTemplate template, IRecipeGenerator generator)
	{
		try {
			ItemStack crafting[] = new ItemStack[10];
			
			List items = (List)ModLoader.getPrivateValue(ShapelessRecipes.class, recipe, obfuscatedNames? "b": "recipeItems");
			
			for(int i = 0; i < items.size() && i < 9; i++)
			{
				crafting[i] = (ItemStack)items.get(i);
			}
			
			crafting[9] = ((IRecipe)recipe).getRecipeOutput();
			
			generator.addRecipe(template, crafting);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addShapedRecipe(ShapedRecipes recipe, IRecipeTemplate template, IRecipeTemplate templateSmall, IRecipeGenerator generator)
	{
		try {
			int width = (Integer)ModLoader.getPrivateValue(ShapedRecipes.class, recipe, obfuscatedNames? "b": "recipeWidth");
			int height = (Integer)ModLoader.getPrivateValue(ShapedRecipes.class, recipe, obfuscatedNames? "c": "recipeHeight");
			ItemStack items[] = (ItemStack[])ModLoader.getPrivateValue(ShapedRecipes.class, recipe, obfuscatedNames? "d": "recipeItems");

			if(width < 3 && height < 3)
			{
				addSmallShapedRecipe(width, height, items, recipe, templateSmall, generator);
			}
			else
			{
				addLargeShapedRecipe(width, height, items, recipe, template, generator);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addSmallShapedRecipe(int width, int height, ItemStack[] items, ShapedRecipes recipe, IRecipeTemplate template, IRecipeGenerator generator)
	{
		ItemStack crafting[] = new ItemStack[5];
		
		for(int i = 0; i < width && i < 2; i++)
		{
			for(int j = 0; j < height && j < 2; j++)
			{
				crafting[i + j * 2] = items[i + j * width];
			}
		}
		
		crafting[4] = ((IRecipe)recipe).getRecipeOutput();
		
		generator.addRecipe(template, crafting);
	}
	
	private void addLargeShapedRecipe(int width, int height, ItemStack[] items, ShapedRecipes recipe, IRecipeTemplate template, IRecipeGenerator generator)
	{
		ItemStack crafting[] = new ItemStack[10];
		
		for(int i = 0; i < width && i < 3; i++)
		{
			for(int j = 0; j < height && j < 3; j++)
			{
				crafting[i + j * 3] = items[i + j * width];
			}
		}
		
		crafting[9] = ((IRecipe)recipe).getRecipeOutput();

		generator.addRecipe(template, crafting);
	}
}
