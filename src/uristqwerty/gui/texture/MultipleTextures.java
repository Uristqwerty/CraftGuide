package uristqwerty.gui.texture;

import uristqwerty.gui.editor.TextureMeta;
import uristqwerty.gui.editor.TextureMeta.TextureParameter;
import uristqwerty.gui.rendering.RendererBase;

@TextureMeta(name = "multipletextures")
public class MultipleTextures implements Texture
{
	@TextureParameter
	public Texture[] textures;

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
