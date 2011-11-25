package net.minecraft.src.CraftGuide.ui;

import net.minecraft.src.CraftGuide.ui.GuiElement;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class GuiImage extends GuiElement
{
	private TexturedRect image;
	
	public GuiImage(int x, int y, int width, int height, GuiTexture texture, int u, int v)
	{
		super(x, y, width, height);
		
		image = new TexturedRect(x, y, width, height, texture, u, v);
	}
	
	@Override
	public void draw()
	{
		render(image);
		
		super.draw();
	}
}
