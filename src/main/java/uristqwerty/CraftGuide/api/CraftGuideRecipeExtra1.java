package uristqwerty.CraftGuide.api;

import net.minecraft.item.ItemStack;


/**
 * Contains two forgotten methods from CraftGuideRecipe. Allows
 *  searching for items, limiting the search to a specific
 *  SlotType.
 */
public interface CraftGuideRecipeExtra1 //extends CraftGuideRecipe //TODO: Uncomment at MC version break.
{
	public boolean containsItem(ItemStack filter, SlotType type);
	public boolean containsItem(ItemFilter filter, SlotType type);
}
