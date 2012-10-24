package uristqwerty.gui.texture;

import uristqwerty.gui.Color;
import uristqwerty.gui.editor.TextureMeta;
import uristqwerty.gui.editor.TextureMeta.TextureParameter;
import uristqwerty.gui.rendering.RendererBase;

@TextureMeta(name = "solidcolor")
public class SolidColorTexture implements Texture
{
	@TextureParameter
	public Color color;

	public SolidColorTexture()
	{
	}

	public SolidColorTexture(int red, int green, int blue, int alpha)
	{
		color = new Color(red, green, blue, alpha);
	}

	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
		renderer.setColor(color.red, color.green, color.blue, color.alpha);
		renderer.drawRect(x, y, width, height);
		renderer.setColor(255, 255, 255, 255);
	}

}
