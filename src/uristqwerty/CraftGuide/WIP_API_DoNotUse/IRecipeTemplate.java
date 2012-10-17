package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.src.ItemStack;

/**
 */
public interface IRecipeTemplate
{
	/**
	 * Sets the width and height that will be used for recipes
	 * produced by this template
	 * @param width
	 * @param height
	 * @return this template, to allow method chaining
	 */
	public IRecipeTemplate setSize(int width, int height);
	
	/**
	 * Gets an ItemStack that represents the type of the recipe.
	 * Generally, this should be the block or item that players
	 * would most closely associate with it, such as the machine
	 * that the recipe is crafted in, or an item that opens the
	 * crafting GUI used by the recipe.
	 * 
	 * @return the ItemStack, or null to default to a workbench
	 */
	public ItemStack getCraftingType();
	
	public ICraftGuideRecipe generate(Object[] items);
}
