package net.minecraft.src.CraftGuide.newAPI;

import net.minecraft.src.ItemStack;

public class ExtraSlot extends ItemSlot
{
	public ItemStack displayed;
	public boolean showName;
	
	public ExtraSlot(int x, int y, ItemStack displayedItem)
	{
		this(x, y, displayedItem, true);
	}
	
	public ExtraSlot(int x, int y, ItemStack displayedItem, boolean showName)
	{
		super(x, y, true);
		displayed = displayedItem;
		this.showName = showName;
	}
}
