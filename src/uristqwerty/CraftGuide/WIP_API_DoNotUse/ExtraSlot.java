package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.src.ItemStack;

public class ExtraSlot extends ItemSlot
{
	public ExtraSlot(int i, int i2, int i3, int i4, int i5, ItemStack is)
	{
		super(i, i2, i3, i4, i5);
	}
	
	public ExtraSlot showName()
	{
		return this;
	}
	
	public ExtraSlot clickable()
	{
		return this;
	}
}
