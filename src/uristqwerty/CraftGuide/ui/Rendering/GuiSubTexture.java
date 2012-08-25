package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.gui.texture.SubTexture;
import uristqwerty.gui.texture.Texture;

public class GuiSubTexture implements ITexture
{
	private GuiTexture texture;
	private int x, y, width, height;
	
	private SubTexture converted;
	
	public GuiSubTexture(GuiTexture texture, int x, int y, int width, int height)
	{
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		converted = new SubTexture(texture.texture(), x, y, width, height);
	}

	@Override
	public void setActive(GuiRenderer renderer)
	{
		texture.setActive(renderer);
		renderer.setSubTexture(x, y, width, height);
	}

	@Override
	public Texture texture()
	{
		return converted;
	}
}
