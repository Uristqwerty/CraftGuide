package uristqwerty.CraftGuide.client.ui;

import org.lwjgl.input.Keyboard;

import uristqwerty.gui_craftguide.components.GuiElement;
import uristqwerty.gui_craftguide.texture.DynamicTexture;
import uristqwerty.gui_craftguide.texture.Texture;

public class FilterCombineModeDisplay extends GuiElement
{
	private Texture add = DynamicTexture.instance("filter-add");
	private Texture remove = DynamicTexture.instance("filter-remove");

	public FilterCombineModeDisplay(int x, int y)
	{
		super(x, y, 8, 18);
	}

	@Override
	public void drawBackground()
	{
		super.drawBackground();

		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		if(shift)
		{
			render(add, 0, 0, this.width(), this.height());
		}
		else if(ctrl)
		{
			render(remove, 0, 0, this.width(), this.height());
		}
	}
}
