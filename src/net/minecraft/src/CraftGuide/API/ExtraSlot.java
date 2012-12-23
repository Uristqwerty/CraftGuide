package net.minecraft.src.CraftGuide.API;

import net.minecraft.item.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;

public class ExtraSlot extends ItemSlot
{
	public ItemStack displayed;

	public ExtraSlot(int x, int y, int width, int height, int index, ItemStack displayedItem)
	{
		super(x, y, width, height, index);
		displayed = displayedItem;
	}
}
