package net.minecraft.src.CraftGuide.API;

import java.util.List;

import net.minecraft.src.CraftGuide.Recipe;

public interface IRecipeFilter
{
	List<ICraftGuideRecipe> removeRecipes(List<ICraftGuideRecipe> allRecipes);
}
