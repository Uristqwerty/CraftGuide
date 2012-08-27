package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;

public class CentredText extends Text
{
	private int width, height;

	public CentredText(int x, int y, int width, int height, String text)
	{
		this(x, y, width, height, text, 0xff000000);
	}
	
	public CentredText(int x, int y, int width, int height, String text, int color)
	{
		super(x, y, text, color);
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.drawText(x + xOffset + (width + 1 - textWidth()) / 2, y + yOffset + (height + 1 - textHeight()) / 2, text, color);
	}

	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
