package net.minecraft.src.CraftGuide.ui;

import net.minecraft.src.CraftGuide.RecipeCache;

public interface IRecipeCacheListener
{
	void onChange(RecipeCache cache);
	void onReset(RecipeCache cache);
}
