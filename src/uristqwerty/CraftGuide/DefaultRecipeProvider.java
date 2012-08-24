package uristqwerty.CraftGuide;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.CraftGuideAPIObject;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeGenerator;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeProvider;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeTemplate;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
		
		int errCount = 0;
		
		for(Object o: recipes)
		{
			try
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
				else if(recipe instanceof ShapedOreRecipe)
				{
					addShapedOreRecipe((ShapedOreRecipe)recipe, template, templateSmall, generator);
				}
				else if(recipe instanceof ShapelessOreRecipe)
				{
					addShapelessOreRecipe((ShapelessOreRecipe)recipe, templateShapeless, generator);
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
	
	private void addShapelessOreRecipe(ShapelessOreRecipe recipe, IRecipeTemplate template, IRecipeGenerator generator)
	{
		try {
			Object crafting[] = new Object[10];
			
			List items = (List)ModLoader.getPrivateValue(ShapelessOreRecipe.class, recipe, "input");
			
			for(int i = 0; i < items.size() && i < 9; i++)
			{
				crafting[i] = (ItemStack)items.get(i);
			}
			
			crafting[9] = recipe.getRecipeOutput();
			
			generator.addRecipe(template, crafting);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void addShapedOreRecipe(ShapedOreRecipe recipe, IRecipeTemplate template, IRecipeTemplate templateSmall, IRecipeGenerator generator)
	{
		try {
			int width = (Integer)ModLoader.getPrivateValue(ShapedOreRecipe.class, recipe, "width");
			int height = (Integer)ModLoader.getPrivateValue(ShapedOreRecipe.class, recipe, "height");
			Object items[] = ModLoader.getPrivateValue(ShapedOreRecipe.class, recipe, "input");

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

	private void addShapelessRecipe(ShapelessRecipes recipe, IRecipeTemplate template, IRecipeGenerator generator)
	{
		try {
			Object crafting[] = new ItemStack[10];
			
			List items = (List)ModLoader.getPrivateValue(ShapelessRecipes.class, recipe, obfuscatedNames? "b": "recipeItems");
			
			for(int i = 0; i < items.size() && i < 9; i++)
			{
				crafting[i] = items.get(i);
			}
			
			crafting[9] = recipe.getRecipeOutput();
			
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
	
	private void addSmallShapedRecipe(int width, int height, Object[] items, IRecipe recipe, IRecipeTemplate template, IRecipeGenerator generator)
	{
		Object crafting[] = new Object[5];
		
		for(int i = 0; i < width && i < 2; i++)
		{
			for(int j = 0; j < height && j < 2; j++)
			{
				crafting[i + j * 2] = items[i + j * width];
			}
		}
		
		crafting[4] = recipe.getRecipeOutput();
		
		generator.addRecipe(template, crafting);
	}
	
	private void addLargeShapedRecipe(int width, int height, Object[] items, IRecipe recipe, IRecipeTemplate template, IRecipeGenerator generator)
	{
		Object crafting[] = new Object[10];
		
		for(int i = 0; i < width && i < 3; i++)
		{
			for(int j = 0; j < height && j < 3; j++)
			{
				crafting[i + j * 3] = items[i + j * width];
			}
		}
		
		crafting[9] = recipe.getRecipeOutput();

		generator.addRecipe(template, crafting);
	}
}
