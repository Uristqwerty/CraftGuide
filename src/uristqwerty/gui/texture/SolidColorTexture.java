package uristqwerty.gui.texture;

import uristqwerty.gui.rendering.RendererBase;

public class SolidColorTexture implements Texture
{
	private int red, green, blue, alpha;
	
	public SolidColorTexture(int red, int green, int blue, int alpha)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
		renderer.setColor(red, green, blue, alpha);
		renderer.drawRect(x, y, width, height);
		renderer.setColor(255, 255, 255, 255);
	}

}
