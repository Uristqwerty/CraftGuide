package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.gui.texture.Texture;

public interface ITexture
{
	public void setActive(GuiRenderer renderer);

	public Texture texture(); //Temporary method during conversion
}
