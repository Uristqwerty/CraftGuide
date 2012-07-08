package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.src.ItemStack;

public class ExtraSlot extends ItemSlot
{
	public ItemStack displayed;
	public boolean showName = false;
	public boolean canClick = false;
	public boolean canFilter = false;
	
	public ExtraSlot(int x, int y, int width, int height, int index, ItemStack displayedItem)
	{
		super(x, y, width, height, index);
		displayed = displayedItem;
	}

	public ExtraSlot clickable(boolean clickable)
	{
		canClick = clickable;
		return this;
	}

	public ExtraSlot showName(boolean showName)
	{
		this.showName = showName;
		return this;
	}

	public ExtraSlot filterable(boolean filterable)
	{
		this.canFilter = filterable;
		return this;
	}

	public ExtraSlot clickable()
	{
		return clickable(true);
	}

	public ExtraSlot showName()
	{
		return showName(true);
	}

	public ExtraSlot filterable()
	{
		return filterable(true);
	}
}
