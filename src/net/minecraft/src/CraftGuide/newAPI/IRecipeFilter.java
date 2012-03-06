package net.minecraft.src.CraftGuide.newAPI;

import java.util.List;

public interface IRecipeFilter
{
	List<ICraftGuideRecipe> removeRecipes(List<ICraftGuideRecipe> allRecipes);
}
