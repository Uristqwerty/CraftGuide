package net.minecraft.src.CraftGuide.ui.Rendering;

import net.minecraft.src.CraftGuide.ui.CraftingDisplay;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class CraftingDisplayRect implements IRenderable
{
	private int x, y, width, height;
	private CraftingDisplay displayElement;
	
	public CraftingDisplayRect(int x, int y, int width, int height, CraftingDisplay displayElement)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.displayElement = displayElement;
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.setClippingRegion(x + xOffset, y + yOffset, width, height);
		displayElement.renderRecipes(renderer, x + xOffset, y + yOffset);
		renderer.clearClippingRegion();
	}
}
