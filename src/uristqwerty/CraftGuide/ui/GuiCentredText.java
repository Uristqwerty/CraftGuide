package uristqwerty.CraftGuide.ui;

import uristqwerty.CraftGuide.ui.Rendering.CentredText;
import uristqwerty.gui.components.GuiElement;

public class GuiCentredText extends GuiElement
{
	private CentredText text;
	
	public GuiCentredText(int x, int y, int width, int height, String text)
	{
		super(x, y, width, height);
		
		this.text = new CentredText(0, 0, width, height, text);
	}

	@Override
	public void onResize(int oldWidth, int oldHeight)
	{
		text.setSize(bounds.width(), bounds.height());
		
		super.onResize(oldWidth, oldHeight);
	}

	@Override
	public void draw()
	{
		render(text);
		
		super.draw();
	}
}
