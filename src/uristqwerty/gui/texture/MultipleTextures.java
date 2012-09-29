package uristqwerty.gui.texture;

import uristqwerty.gui.rendering.RendererBase;

public class MultipleTextures implements Texture
{
	private final Texture[] textures;
	
	public MultipleTextures(Texture[] textures)
	{
		this.textures = textures;
	}

	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
		for(Texture texture: textures)
		{
			texture.renderRect(renderer, x, y, width, height, u, v);
		}
	}
}
