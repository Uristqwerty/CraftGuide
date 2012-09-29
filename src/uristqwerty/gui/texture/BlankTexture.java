package uristqwerty.gui.texture;

import uristqwerty.gui.editor.TextureMeta;
import uristqwerty.gui.rendering.RendererBase;

@TextureMeta(name = "blank")
public class BlankTexture implements Texture
{
	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
	}
}
