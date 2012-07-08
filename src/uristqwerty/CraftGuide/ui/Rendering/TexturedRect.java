package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;

public class TexturedRect implements IRenderable
{
	private ITexture texture;
	private int x, y, width, height, u, v;

	public TexturedRect(int x, int y, int width, int height, ITexture texture)
	{
		this(x, y, width, height, texture, 0, 0);
	}
	
	public TexturedRect(int x, int y, int width, int height, ITexture texture, int u, int v)
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
	
	public void moveBy(int xChange, int yChange)
	{
		x += xChange;
		y += yChange;
	}
	
	public void resizeBy(int widthChange, int heightChange)
	{
		width += widthChange;
		height += heightChange;
	}
	
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
