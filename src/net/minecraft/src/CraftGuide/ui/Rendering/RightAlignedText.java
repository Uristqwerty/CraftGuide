package net.minecraft.src.CraftGuide.ui.Rendering;

import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class RightAlignedText extends Text
{
	public RightAlignedText(int x, int y, String text)
	{
		super(x, y, text);
	}
	
	public RightAlignedText(int x, int y, String text, int color)
	{
		super(x, y, text, color);
	}
	
	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.drawRightAlignedText(x + xOffset, y + yOffset, text, color);
	}
}
