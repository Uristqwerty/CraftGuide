package uristqwerty.CraftGuide.client.ui;

import org.lwjgl.input.Keyboard;

import uristqwerty.CraftGuide.client.FilterDisplay;
import uristqwerty.gui_craftguide.components.GuiElement;
import uristqwerty.gui_craftguide.texture.DynamicTexture;
import uristqwerty.gui_craftguide.texture.Texture;

public class FilterCombineModeDisplay extends GuiElement
{
	private Texture add = DynamicTexture.instance("filter-add");
	private Texture remove = DynamicTexture.instance("filter-remove");
	private FilterDisplay filterDisplay;

	public FilterCombineModeDisplay(int x, int y, FilterDisplay filter)
	{
		super(x, y, 8, 18);
		this.filterDisplay = filter;
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
		else if(ctrl && filterDisplay.filter != null)
		{
			render(remove, 0, 0, this.width(), this.height());
		}
	}
}
