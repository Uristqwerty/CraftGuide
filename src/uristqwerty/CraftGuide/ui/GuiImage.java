package uristqwerty.CraftGuide.ui;

import uristqwerty.CraftGuide.ui.GuiElement;
import uristqwerty.CraftGuide.ui.Rendering.ITexture;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;

public class GuiImage extends GuiElement
{
	private TexturedRect image;
	
	public GuiImage(int x, int y, int width, int height, ITexture texture, int u, int v)
	{
		super(x, y, width, height);
		
		image = new TexturedRect(0, 0, width, height, texture.texture(), u, v);
	}
	
	@Override
	public void draw()
	{
		render(image);
		
		super.draw();
	}
}
