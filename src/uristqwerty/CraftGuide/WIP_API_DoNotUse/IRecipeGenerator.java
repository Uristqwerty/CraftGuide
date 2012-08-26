package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.src.ItemStack;

/**
 * This interface contains methods that can be used to provide CraftGuide with
 * crafting recipes.
 * <br><br>
 * To do so, use one of the methods returning an {@link IRecipeTemplate}
 * to create a template from a list of {@link ItemSlot}s and optionally an
 * ItemStack representing the recipe type (generally the block/item used to
 * craft the recipe, if not provided, defaults to the workbench).
 * <br><br>
 * With the template, call addRecipe for each recipe, passing an ItemStack[]
 * corresponding to the ItemSlot[] provided when creating the template.
 */

public interface IRecipeGenerator
{
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY);

	public void addRecipe(IRecipeTemplate template, Object[] crafting);
	public void setDefaultTypeVisibility(ItemStack type, boolean visible);
	
	/**
	 * Takes an IRecipe, and returns an array representing a
	 * 3x3 crafting grid, plus a single output, for that recipe's
	 * contents. Each element is either null, an ItemStack, or an array
	 * containing zero or more ItemStacks (for Forge ore dictionary
	 * recipes).
	 * <br><br>
	 * May return null if given an IRecipe implementation that it
	 * cannot interpret.
	 * @param recipe the IRecipe to be read
	 * @return an Object[10], where the first nine elements form
	 * the 3x3 input grid, and the last element is the recipe output.
	 */
	public Object[] getCraftingRecipe(IRecipe recipe);
}
