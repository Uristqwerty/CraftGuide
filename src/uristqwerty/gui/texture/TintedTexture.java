package uristqwerty.gui.texture;

import uristqwerty.gui.Color;
import uristqwerty.gui.editor.TextureMeta;
import uristqwerty.gui.editor.TextureMeta.TextureParameter;
import uristqwerty.gui.rendering.RendererBase;

@TextureMeta(name = "coloredtexture")
public class TintedTexture implements Texture
{
	@TextureParameter
	public Texture source;

	@TextureParameter
	public Color color;

	private double prev[] = new double[4];

	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
		renderer.getColorModifierv(prev);
		renderer.setColorModifier(
				prev[0] * (color.red / 255.0),
				prev[1] * (color.green / 255.0),
				prev[2] * (color.blue / 255.0),
				prev[3] * (color.alpha / 255.0));

		source.renderRect(renderer, x, y, width, height, u, v);
		renderer.setColorModifierv(prev);
	}
}
