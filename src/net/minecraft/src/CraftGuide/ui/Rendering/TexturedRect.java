package net.minecraft.src.CraftGuide.ui.Rendering;

import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;

public class TexturedRect implements IRenderable
{
	private GuiTexture texture;
	private int x, y, width, height, u, v;
	
	public TexturedRect(int x, int y, int width, int height, GuiTexture texture, int u, int v)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
		this.u = u;
		this.v = v;
	}

	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.setTexture(texture);
		renderer.setTextureCoords(u, v);
		renderer.drawTexturedRect(x + xOffset, y + yOffset, width, height);
	}
}
