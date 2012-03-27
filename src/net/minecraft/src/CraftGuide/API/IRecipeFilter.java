package net.minecraft.src.CraftGuide.API;

import java.util.List;

/**
 * When CraftGuide (re)loads its recipe list, every Object that implements this
 * interface, and has been registered with CraftGuide, will be passed a List
 * of recipes for each recipe type. It should return a List of recipes, omitting
 * any recipe that it wants removed.
 * 
 * @see IRecipeFilter2
 */

public interface IRecipeFilter
{
	/**
	 * Each time CraftGuide's recipe list is reloaded, this method is called once for
	 * each recipe type that has been provided by an instance of IRecipeProvider.
	 * 
	 * @param allRecipes
	 * @return a subset of the passed List, omitting any recipe that the implementing
	 * Object wishes removed.
	 */
	List<ICraftGuideRecipe> removeRecipes(List<ICraftGuideRecipe> allRecipes);
}
