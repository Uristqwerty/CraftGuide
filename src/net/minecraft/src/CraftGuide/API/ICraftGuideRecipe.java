package net.minecraft.src.CraftGuide.API;

import net.minecraft.item.ItemStack;

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
	/**
	 * When a recipe is rendered, the ItemSlots provided to the template are
	 * used to determine the layout of the recipe's items.
	 */

	public class ItemSlot
	{
		public int x, y, width, height, index;
		public boolean drawQuantity;

		public ItemSlot(int x, int y, int width, int height, int index)
		{
			this(x, y, width, height, index, false);
		}

		public ItemSlot(int x, int y, int width, int height, int index, boolean drawQuantity)
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.index = index;
			this.drawQuantity = drawQuantity;
		}
	}

	public ItemStack getItem(int index);
	public ItemSlot getSlotUnderMouse(int x, int y);

	public boolean containsItem(ItemStack filter);
	public boolean containsItem(ItemStack filter, boolean exactMatch);
	public ItemStack[] getItems();
}
