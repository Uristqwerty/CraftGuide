package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;

public class ShadedRect implements IRenderable
{
	private int x, y, width, height;
	private int colour, alpha;

	public ShadedRect(int x, int y, int width, int height, int colour)
	{
		this(x, y, width, height, colour, 0xff);
	}
	
	public ShadedRect(int x, int y, int width, int height, int colour, int alpha)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.colour = colour;
		this.alpha = alpha;
	}
	
	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.setColour(colour, alpha);
		renderer.drawRect(x + xOffset, y + yOffset, width, height);
		renderer.setColour(0xffffff, 0xff);
	}
}
