package uristqwerty.CraftGuide.ui;

import uristqwerty.CraftGuide.RecipeCache;

public interface IRecipeCacheListener
{
	void onChange(RecipeCache cache);
	void onReset(RecipeCache cache);
}
