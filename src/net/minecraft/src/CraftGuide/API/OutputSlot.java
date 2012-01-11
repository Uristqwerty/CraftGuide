package net.minecraft.src.CraftGuide.API;

import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;

public class OutputSlot extends ItemSlot
{
	public OutputSlot(int x, int y, int width, int height, int index, boolean drawQuantity)
	{
		super(x, y, width, height, index, drawQuantity);
	}

	public OutputSlot(int x, int y, int width, int height, int index)
	{
		super(x, y, width, height, index);
	}
}
