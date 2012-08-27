package uristqwerty.gui.texture;

import uristqwerty.gui.Renderer;

/**
 * Represents a subsection of a larger texture, shifted so that
 * the sections's top left corner is at (0, 0). When drawn, only
 * draws the portion, if any, of the drawn area that overlaps with
 * the subsection.
 */
public class TextureClip implements Texture
{
	private final Texture source;
	private final int u, v, width, height;

	public TextureClip(Texture source, int u, int v, int width, int height)
	{
		this.source = source;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
	}

	@Override
	public void renderRect(Renderer renderer, int x, int y, int width, int height, int u, int v)
	{
		if(u < 0)
		{
			width += u;
			u = 0;
		}
		
		if(u + width > this.width)
		{
			width = this.width - u;
		}
		
		if(v < 0)
		{
			height += v;
			v = 0;
		}
		
		if(v + height > this.height)
		{
			height = this.height - v;
		}
		
		if(width > 0 && height > 0)
		{
			source.renderRect(renderer, x, y, width, height, u + this.u, v + this.v);
		}
	}
}
