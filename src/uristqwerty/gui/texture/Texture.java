package uristqwerty.gui.texture;

import uristqwerty.gui.Renderer;

public interface Texture
{
	public void renderRect(Renderer renderer, int x, int y, int width, int height, int u, int v);
}
