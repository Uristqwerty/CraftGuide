package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.src.ItemStack;

/**
 * Contains a number of methods that allow instances of {@link IRecipeFilter} and
 * {@link IRecipeFilter2} to determine what items a recipe contains.
 * <br><br>
 * Also contains a few bad decisions which can't be fixed without risking problems
 * with existing mods (ItemSlot should really have its own file, getSlotUnderMouse
 * really shouldn't be a part of this interface at all).
 */

public interface ICraftGuideRecipe
{
	public Object getItem(int index);
	public ItemStack getItemStack(int index);
	public ItemSlot getSlotUnderMouse(int x, int y);
	
	public boolean containsItem(ItemStack filter);
	public boolean containsItem(ItemStack filter, boolean exactMatch);
	public Object[] getItems();
	public boolean containsItem(IItemFilter filter);
}
