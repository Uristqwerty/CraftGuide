package net.minecraft.src.CraftGuide.ui;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.ui.Rendering.RenderItemStack;

public class GuiItemStack extends GuiElement
{
	private RenderItemStack renderable;
	
	public GuiItemStack(int x, int y, boolean drawQuantity)
	{
		super(x, y, 16, 16);
		
		renderable = new RenderItemStack(null, drawQuantity);
	}
	
	public void setItem(ItemStack item)
	{
		renderable.setItem(item);
	}

	@Override
	public void draw()
	{
		render(renderable);
		
		super.draw();
	}
}
