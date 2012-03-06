package net.minecraft.src.CraftGuide.newAPI;

import java.util.List;
import java.util.Map;

import net.minecraft.src.ItemStack;

public interface IRecipeFilter2
{
	Map<ItemStack, List<ICraftGuideRecipe>> removeRecipes(Map<ItemStack, List<ICraftGuideRecipe>> allRecipes);
}
