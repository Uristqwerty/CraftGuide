package uristqwerty.gui.texture;

import uristqwerty.gui.rendering.RendererBase;

public interface Texture
{
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v);
}
