package net.minecraft.src.CraftGuide.API;

import net.minecraft.item.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 */

@Deprecated
public class ExtraSlot extends ItemSlot
{
	public ItemStack displayed;

	public ExtraSlot(int x, int y, int width, int height, int index, ItemStack displayedItem)
	{
		super(x, y, width, height, index);
		displayed = displayedItem;
	}
}
