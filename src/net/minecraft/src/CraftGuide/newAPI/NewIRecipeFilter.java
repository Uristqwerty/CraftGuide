package net.minecraft.src.CraftGuide.newAPI;

import net.minecraft.src.ItemStack;

public interface NewIRecipeFilter
{
	boolean removeRecipe(NewICraftGuideRecipe recipe, ItemStack craftingType);
}
