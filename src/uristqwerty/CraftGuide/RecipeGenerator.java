package uristqwerty.CraftGuide;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeGenerator;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeTemplate;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.minecraft.Image;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeGenerator implements IRecipeGenerator
{
	private Map<ItemStack, List<ICraftGuideRecipe>> recipes = new HashMap<ItemStack, List<ICraftGuideRecipe>>();
	public List<ItemStack> disabledTypes = new LinkedList<ItemStack>();
	private static ItemStack workbench = new ItemStack(Block.workbench);

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
			ItemStack craftingType, String backgroundTexture, int backgroundX,
			int backgroundY, int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate(slots, craftingType,
				backgroundTexture, backgroundX, backgroundY,
				backgroundTexture, backgroundSelectedX, backgroundSelectedY);
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType,
			String backgroundTexture, int backgroundX, int backgroundY,
			String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY)
	{
		if(craftingType == null)
		{
			craftingType = workbench;
		}
		
		return new RecipeTemplate(
				slots,
				craftingType,
				new TexturedRect(0, 0, 79, 58, 
					Image.getImage(backgroundTexture),
					backgroundX, backgroundY),
				new TexturedRect(0, 0, 79, 58, 
					Image.getImage(backgroundSelectedTexture),
					backgroundSelectedX, backgroundSelectedY));
	}

	@Override
	public void addRecipe(IRecipeTemplate template, Object[] items)
	{
		RecipeTemplate recipeTemplate = (RecipeTemplate)template;

		List<ICraftGuideRecipe> recipeList = recipes.get(recipeTemplate.craftingType);
		
		if(recipeList == null)
		{
			recipeList = new LinkedList<ICraftGuideRecipe>();
			recipes.put(recipeTemplate.craftingType, recipeList);
		}
		
		recipeList.add(recipeTemplate.generate(items));
	}

	public Map<ItemStack, List<ICraftGuideRecipe>> getRecipes()
	{
		return recipes;
	}
	
	public void clearRecipes()
	{
		recipes.clear();
	}

	@Override
	public void setDefaultTypeVisibility(ItemStack type, boolean visible)
	{
		if(visible)
		{
			disabledTypes.remove(type);
		}
		else if(!disabledTypes.contains(type))
		{
			disabledTypes.add(type);
		}
	}

	@Override
	public Object[] getCraftingRecipe(IRecipe recipe)
	{
		return getCraftingRecipe(recipe, false);
	}

	@Override
	public Object[] getCraftingRecipe(IRecipe recipe, boolean allowSmallGrid)
	{
		try
		{
			if(recipe instanceof ShapedRecipes)
			{
				int width = (Integer)ModLoader.getPrivateValue(ShapedRecipes.class, (ShapedRecipes)recipe, "b");
				int height = (Integer)ModLoader.getPrivateValue(ShapedRecipes.class, (ShapedRecipes)recipe, "c");
				Object[] items = (Object[])ModLoader.getPrivateValue(ShapedRecipes.class, (ShapedRecipes)recipe, "d");
				
				if(allowSmallGrid && width < 3 && height < 3)
				{
					return getSmallShapedRecipe(width, height, items, ((ShapedRecipes)recipe).getRecipeOutput());
				}
				else
				{
					return getCraftingShapedRecipe(width, height, items, ((ShapedRecipes)recipe).getRecipeOutput());
				}
			}
			else if(recipe instanceof ShapelessRecipes)
			{
				List items = (List)ModLoader.getPrivateValue(ShapelessRecipes.class, (ShapelessRecipes)recipe, "b");
				return getCraftingShapelessRecipe(items, ((ShapelessRecipes)recipe).getRecipeOutput());
			}
			else if(recipe instanceof ShapedOreRecipe)
			{
				int width = (Integer)ModLoader.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "width");
				int height = (Integer)ModLoader.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "height");
				Object[] items = (Object[])ModLoader.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "input");

				if(allowSmallGrid && width < 3 && height < 3)
				{
					return getSmallShapedRecipe(width, height, items, ((ShapedOreRecipe)recipe).getRecipeOutput());
				}
				else
				{
					return getCraftingShapedRecipe(width, height, items, ((ShapedOreRecipe)recipe).getRecipeOutput());
				}
			}
			else if(recipe instanceof ShapelessOreRecipe)
			{
				List items = (List)ModLoader.getPrivateValue(ShapelessOreRecipe.class, (ShapelessOreRecipe)recipe, "input");
				return getCraftingShapelessRecipe(items, ((ShapelessOreRecipe)recipe).getRecipeOutput());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			CraftGuideLog.log("Exception while trying to parse an ItemStack[10] from an IRecipe:");
			CraftGuideLog.log(e);
		}
		
		return null;
	}

	private Object[] getSmallShapedRecipe(int width, int height, Object[] items, ItemStack recipeOutput)
	{
		Object[] output = new Object[5];
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				output[y * 2 + x] = items[y * width + x];
			}
		}
		
		output[4] = recipeOutput;
		return output;
	}

	private Object[] getCraftingShapelessRecipe(List items, ItemStack recipeOutput)
	{
		Object[] output = new Object[10];
		
		for(int i = 0; i < items.size(); i++)
		{
			output[i] = items.get(i);
		}
		
		output[9] = recipeOutput;
		return output;
	}

	private Object[] getCraftingShapedRecipe(int width, int height, Object[] items, ItemStack recipeOutput)
	{
		Object[] output = new Object[10];
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				output[y * 3 + x] = items[y * width + x];
			}
		}
		
		output[9] = recipeOutput;
		return output;
	}
}
