package net.minecraft.src.CraftGuide;

import java.util.List;
import java.util.Collection;
import java.util.LinkedList;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class RecipeGenerator implements IRecipeGenerator
{
	List<ICraftGuideRecipe> recipes = new LinkedList<ICraftGuideRecipe>();
	
	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
		String backgroundTexture, int backgroundX, int backgroundY,
		String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY)
	{
		return new RecipeTemplate(
			slots,
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

		recipes.add(recipeTemplate.generate(items));
	}

	public Collection<ICraftGuideRecipe> getRecipes()
	{
		return recipes;
	}

}
