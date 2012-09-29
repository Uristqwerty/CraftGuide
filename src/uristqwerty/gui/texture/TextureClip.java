package uristqwerty.gui.texture;

import uristqwerty.gui.Rect;
import uristqwerty.gui.editor.TextureMeta;
import uristqwerty.gui.editor.TextureMeta.TextureParameter;
import uristqwerty.gui.rendering.RendererBase;

/**
 * Represents a subsection of a larger texture, shifted so that
 * the sections's top left corner is at (0, 0). When drawn, only
 * draws the portion, if any, of the drawn area that overlaps with
 * the subsection.
 */
@TextureMeta(name = "clip")
public class TextureClip implements Texture
{
	@TextureParameter
	public Texture source;
	
	@TextureParameter
	public Rect rect;

	public TextureClip(Texture source, int u, int v, int width, int height)
	{
		this.source = source;
		rect = new Rect(u, v, width, height);
	}

	public TextureClip(Texture source, Rect rect)
	{
		this.source = source;
		this.rect = rect;
	}

	public TextureClip()
	{
	}

	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
		if(u < 0)
		{
			width += u;
			u = 0;
		}
		
		if(u + width > rect.width)
		{
			width = rect.width - u;
		}
		
		if(v < 0)
		{
			height += v;
			v = 0;
		}
		
		if(v + height > rect.height)
		{
			height = rect.height - v;
		}
		
		if(width > 0 && height > 0)
		{
			source.renderRect(renderer, x, y, width, height, u + rect.x, v + rect.y);
		}
	}
}
