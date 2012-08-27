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
	/**
	 * Creates an {@link IRecipeTemplate} for the provided ISlot[],
	 * associated with the provided crafting type. Create a default
	 * background based on the size of the template.
	 * @param slots
	 * @param craftingType
	 * @return
	 */
	public IRecipeTemplate createRecipeTemplate(ISlot[] slots, ItemStack craftingType);
	
	public IRecipeTemplate createRecipeTemplate(ISlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ISlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY);

	@Deprecated
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, int backgroundSelectedX, int backgroundSelectedY);
	
	@Deprecated
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY);

	public void addRecipe(IRecipeTemplate template, Object[] crafting);
	public void addRecipe(ICraftGuideRecipe recipe, ItemStack craftingType);
	
	/**
	 * Sets whether a certain type of recipe is initially visible.
	 * <br><br>
	 * Useful for hiding recipe types that have a lot of recipes, but
	 * few people need, so they would normally just add excessive
	 * clutter to the recipe list.
	 * @param type an ItemStack associated with the recipe type
	 * @param visible whether it is initially visible or not
	 */
	public void setDefaultTypeVisibility(ItemStack type, boolean visible);
	
	/**
	 * Takes an IRecipe, and returns an array representing a
	 * 3x3 crafting grid, plus a single output, for that recipe's
	 * contents. Each element is either null, an ItemStack, or a
	 * List containing zero or more ItemStacks (for Forge ore
	 * dictionary recipes).
	 * <br><br>
	 * May return null if given an IRecipe implementation that it
	 * cannot interpret.
	 * @see #getCraftingRecipe(IRecipe, boolean)
	 * @param recipe the IRecipe to be read
	 * @return an Object[10], where the first nine elements form
	 * the 3x3 input grid, and the last element is the recipe output.
	 */
	public Object[] getCraftingRecipe(net.minecraft.src.IRecipe recipe);
	
	/**
	 * Takes an IRecipe, and returns an array representing a
	 * 3x3 or 2x2 crafting grid, plus a single output.
	 * <br><br>
	 * See {@link #getCraftingRecipe(IRecipe)} for details.
	 * @see #getCraftingRecipe(IRecipe)
	 * @param recipe
	 * @param allowSmallGrid	if true, may return an Object[5] if
	 * 		the recipe fits in a 2x2 grid.
	 * @return an Object[10] or an Object[5]
	 */
	public Object[] getCraftingRecipe(net.minecraft.src.IRecipe recipe, boolean allowSmallGrid);
}
