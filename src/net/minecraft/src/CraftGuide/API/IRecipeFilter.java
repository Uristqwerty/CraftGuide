package net.minecraft.src.CraftGuide.API;

import java.util.List;

public interface IRecipeFilter
{
	List<ICraftGuideRecipe> removeRecipes(List<ICraftGuideRecipe> allRecipes);
}
