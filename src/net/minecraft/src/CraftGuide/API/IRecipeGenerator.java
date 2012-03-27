package net.minecraft.src.CraftGuide.API;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;

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
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, String backgroundTexture, int backgroundX, int backgroundY, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, String backgroundTexture, int backgroundX, int backgroundY, String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY);

	public void addRecipe(IRecipeTemplate template, ItemStack[] items);
}
