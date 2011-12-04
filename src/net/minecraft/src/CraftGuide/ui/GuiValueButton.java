package net.minecraft.src.CraftGuide.ui;

import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.ITexture;

public class GuiValueButton extends GuiButton
{
	private int value;

	public GuiValueButton(int x, int y, int width, int height, ITexture texture,
			int u, int v, int value)
	{
		super(x, y, width, height, texture, u, v);
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}
