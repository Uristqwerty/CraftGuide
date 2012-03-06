package net.minecraft.src.CraftGuide.newAPI;

public class ItemSlot
{
	public int x, y, width = 16, height = 16, index = -2;
	public boolean drawQuantity;

	public ItemSlot(int x, int y)
	{
		this(x, y, false);
	}
	
	public ItemSlot(int x, int y, boolean drawQuantity)
	{
		this.x = x;
		this.y = y;
		this.drawQuantity = drawQuantity;
	}
}