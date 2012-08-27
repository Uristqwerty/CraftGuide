package uristqwerty.CraftGuide.ui;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.NamedTexture;
import uristqwerty.gui.Renderer;
import uristqwerty.gui.texture.Texture;

public class NamedTextureObject implements NamedTexture, Texture
{
	private final Texture actualTexture;
	
	public NamedTextureObject(Texture texture)
	{
		actualTexture = texture;
	}
	
	@Override
	public void renderRect(Renderer renderer, int x, int y, int width, int height, int u, int v)
	{
		actualTexture.renderRect(renderer, x, y, width, height, u, v);
	}
}
