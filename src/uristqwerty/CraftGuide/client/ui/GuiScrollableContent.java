package uristqwerty.CraftGuide.client.ui;

import uristqwerty.CraftGuide.client.ui.GuiScrollBar.ScrollBarAlignmentCallback;
import uristqwerty.gui_craftguide.components.GuiElement;

public abstract class GuiScrollableContent extends GuiElement implements ScrollBarAlignmentCallback
{
	protected GuiScrollBar scrollBar;

	public GuiScrollableContent(int x, int y, int width, int height, GuiScrollBar scrollBar)
	{
		super(x, y, width, height);

		this.scrollBar = scrollBar;
	}

	@Override
	public float alignScrollBar(GuiScrollBar guiScrollBar, float oldValue, float newValue)
	{
		return newValue;
	}
}
