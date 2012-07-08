package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.GuiScrollableGrid;

public class GridRect implements IRenderable
{
	private int x, y, width, height;
	private GuiScrollableGrid gridElement;
	
	public GridRect(int x, int y, int width, int height, GuiScrollableGrid displayElement)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gridElement = displayElement;
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.setClippingRegion(x + xOffset, y + yOffset, width, height);
		gridElement.renderGridRows(renderer, x + xOffset, y + yOffset);
		renderer.clearClippingRegion();
	}

	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
