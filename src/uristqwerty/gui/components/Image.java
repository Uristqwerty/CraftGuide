package uristqwerty.gui.components;

import uristqwerty.gui.rendering.TexturedRect;
import uristqwerty.gui.texture.Texture;

public class Image extends GuiElement
{
	private TexturedRect image;
	
	public Image(int x, int y, int width, int height, Texture texture, int u, int v)
	{
		super(x, y, width, height);
		
		image = new TexturedRect(0, 0, width, height, texture, u, v);
	}
	
	@Override
	public void draw()
	{
		render(image);
		
		super.draw();
	}
}
