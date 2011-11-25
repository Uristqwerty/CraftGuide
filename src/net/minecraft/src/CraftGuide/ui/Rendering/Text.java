package net.minecraft.src.CraftGuide.ui.Rendering;

import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class Text implements IRenderable
{
	protected int x, y;
	protected String text;
	protected int color;
	
	public Text(int x, int y, String text, int color)
	{
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
	}
	public Text(int x, int y, String text)
	{
		this(x, y, text, 0xffffffff);
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.drawText(x + xOffset, y + yOffset, text, color);
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
}
