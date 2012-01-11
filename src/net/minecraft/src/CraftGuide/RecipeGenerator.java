package net.minecraft.src.CraftGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class RecipeGenerator implements IRecipeGenerator
{
	private Map<ItemStack, List<ICraftGuideRecipe>> recipes = new HashMap<ItemStack, List<ICraftGuideRecipe>>();
	private static ItemStack workbench = new ItemStack(Block.workbench);

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
			String backgroundTexture, int backgroundX, int backgroundY,
			int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate(slots,
				backgroundTexture, backgroundX, backgroundY,
				backgroundTexture, backgroundSelectedX, backgroundSelectedY);
	}

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
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
		String backgroundTexture, int backgroundX, int backgroundY,
		String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate(slots, workbench,
				backgroundTexture, backgroundX, backgroundY,
				backgroundSelectedTexture, backgroundSelectedX, backgroundSelectedY);
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType,
			String backgroundTexture, int backgroundX, int backgroundY,
			String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY)
	{
		return new RecipeTemplate(
				slots,
				craftingType,
				new TexturedRect(0, 0, 79, 58, 
					GuiTexture.getInstance(backgroundTexture),
					backgroundX, backgroundY),
				new TexturedRect(0, 0, 79, 58, 
					GuiTexture.getInstance(backgroundSelectedTexture),
					backgroundSelectedX, backgroundSelectedY));
	}

	@Override
	public void addRecipe(IRecipeTemplate template, ItemStack[] items)
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
}
