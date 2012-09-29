package uristqwerty.gui.texture;

import uristqwerty.gui.rendering.RendererBase;

public class BorderedTexture implements Texture
{
	private final Texture[] textures = new Texture[9];
	private final int borderWidth, borderHeight;

	public BorderedTexture(Texture[] textures, int borderWidth)
	{
		this(textures, borderWidth, borderWidth);
	}
	
	public BorderedTexture(Texture[] textures, int borderWidth, int borderHeight)
	{
		for(int i = 0; i < Math.min(textures.length, 9); i++)
		{
			this.textures[i] = textures[i];
		}
		
		this.borderWidth = borderWidth;
		this.borderHeight = borderHeight;
	}

	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
		textures[0].renderRect(renderer, x, y, borderWidth, borderHeight, 0, 0);
		textures[1].renderRect(renderer, x + borderWidth, y, width - borderWidth * 2, borderHeight, 0, 0);
		textures[2].renderRect(renderer, x + width - borderWidth, y, borderWidth, borderHeight, 0, 0);
		textures[3].renderRect(renderer, x, y + borderHeight, borderWidth, height - borderHeight * 2, 0, 0);
		textures[4].renderRect(renderer, x + borderWidth, y + borderHeight, width - borderWidth * 2, height - borderHeight * 2, 0, 0);
		textures[5].renderRect(renderer, x + width - borderWidth, y + borderHeight, borderWidth, height - borderHeight * 2, 0, 0);
		textures[6].renderRect(renderer, x, y + height - borderHeight, borderWidth, borderHeight, 0, 0);
		textures[7].renderRect(renderer, x + borderWidth, y + height - borderHeight, width - borderWidth * 2, borderHeight, 0, 0);
		textures[8].renderRect(renderer, x + width - borderWidth, y + height - borderHeight, borderWidth, borderHeight, 0, 0);
	}
}
