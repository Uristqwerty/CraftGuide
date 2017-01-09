package uristqwerty.gui_craftguide.rendering;

import uristqwerty.gui_craftguide.texture.Texture;

public class TexturedRect implements Renderable
{
	public int x, y, width, height, u, v;
	public Texture texture;

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
	public void render(RendererBase renderer, int x, int y)
	{
		renderer.drawTexturedRect(texture, x + this.x, y + this.y, width, height, u, v);
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

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || obj.getClass() != this.getClass())
			return false;

		TexturedRect other = (TexturedRect)obj;
		return this.texture.equals(other.texture) &&
				this.x == other.x && this.y == other.y &&
				this.width == other.width && this.height == other.height &&
				this.u == other.u && this.v == other.v;
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
