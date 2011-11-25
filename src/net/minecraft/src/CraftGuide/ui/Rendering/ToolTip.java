package net.minecraft.src.CraftGuide.ui.Rendering;

import org.lwjgl.input.Mouse;

import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class ToolTip implements IRenderable
{
	private String text;
	
	public ToolTip(String text)
	{
		this.text = text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.drawFloatingText(renderer.guiXFromMouseX(Mouse.getX()) + 12, renderer.guiYFromMouseY(Mouse.getY()) - 13, text);
	}
}
