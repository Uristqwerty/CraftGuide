package net.minecraft.src.CraftGuide.API;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 *
 * When CraftGuide (re)loads its recipe list, every Object that implements this
 * interface, and has been registered with CraftGuide, will have its generateRecipes
 * method called.
 */

@Deprecated
public interface IRecipeProvider
{
	/**
	 * Called by CraftGuide when it is (re)populating its recipe list, as a request
	 * for the implementing Object to provide recipes through the IRecipeGenerator
	 * instance passed to it.
	 * @param generator
	 * @see IRecipeGenerator
	 */
	void generateRecipes(IRecipeGenerator generator);
}
