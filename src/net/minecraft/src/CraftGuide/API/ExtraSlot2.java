package net.minecraft.src.CraftGuide.API;

import net.minecraft.src.ItemStack;

public class ExtraSlot2 extends ExtraSlot
{
	public boolean showName = false, canClick = false, canFilter = false;
	
	public ExtraSlot2(int x, int y, int width, int height, int index, ItemStack displayedItem)
	{
		super(x, y, width, height, index, displayedItem);
	}
	
	public ExtraSlot2 clickable(boolean clickable)
	{
		canClick = clickable;
		return this;
	}
	
	public ExtraSlot2 showName(boolean showName)
	{
		this.showName = showName;
		return this;
	}
	
	public ExtraSlot2 filterable(boolean filterable)
	{
		this.canFilter = filterable;
		return this;
	}
	
	public ExtraSlot2 clickable()
	{
		return clickable(true);
	}
	
	public ExtraSlot2 showName()
	{
		return showName(true);
	}
	
	public ExtraSlot2 filterable()
	{
		return filterable(true);
	}
}
