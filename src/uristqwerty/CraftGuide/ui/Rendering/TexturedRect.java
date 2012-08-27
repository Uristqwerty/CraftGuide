package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.gui.texture.Texture;

public class TexturedRect implements IRenderable
{
	protected int x, y, width, height, u, v;
	protected Texture texture;

	public TexturedRect(int x, int y, int width, int height, Texture texture)
	{
		this(x, y, width, height, texture, 0, 0);
	}
	
	public TexturedRect(int x, int y, int width, int height, Texture texture, int u, int v)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
		this.u = u;
		this.v = v;
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		//System.out.println("TexturedRect.render( --- , " + xOffset + ", " + yOffset + ")");
		renderer.drawTexturedRect(texture, x + xOffset, y + yOffset, width, height, u, v);
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
