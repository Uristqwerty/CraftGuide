package net.minecraft.src.CraftGuide.newAPI;

import net.minecraft.src.ItemStack;

public class NewExtraSlot extends NewItemSlot
{
	public ItemStack displayed;
	public boolean showName;
	
	public NewExtraSlot(int x, int y, ItemStack displayedItem)
	{
		this(x, y, displayedItem, true);
	}
	
	public NewExtraSlot(int x, int y, ItemStack displayedItem, boolean showName)
	{
		super(x, y, true);
		displayed = displayedItem;
		this.showName = showName;
	}
}
