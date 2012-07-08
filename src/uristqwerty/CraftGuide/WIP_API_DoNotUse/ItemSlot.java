package uristqwerty.CraftGuide.WIP_API_DoNotUse;

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