package net.minecraft.src.CraftGuide.API;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;

public interface ICraftGuideRecipe
{
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
	
	public ItemStack getItem(int index);
	public ItemSlot getSlotUnderMouse(int x, int y);
	
	public boolean containsItem(ItemStack filter);
	public boolean containsItem(ItemStack filter, boolean exactMatch);
	public ItemStack[] getItems();
}
