package net.minecraft.src.CraftGuide.ui.Rendering;

import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class FloatingText implements IRenderable
{
	private int x, y;
	private String text;
	
	public FloatingText(int x, int y, String text)
	{
		this.x = x - 12;
		this.y = y + 12;
		this.text = text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.drawFloatingText(x + xOffset + 3, y + yOffset + 3, text);
	}
}
