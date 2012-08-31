package uristqwerty.CraftGuide;

import java.util.List;

import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.ui.GuiElement;
import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;

public class FilterDisplay extends GuiElement implements IRenderable
{
	private ItemFilter filter;
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	private IRenderable itemNameOverlay = new Overlay(itemName);
	private List<String> text;

	public FilterDisplay(int x, int y)
	{
		super(x, y, 16, 16);
	}

	public void setFilter(ItemFilter filter)
	{
		this.filter = filter;
	}
	
	@Override
	public void mouseMoved(int x, int y)
	{
		if(isMouseOver(x, y) && filter != null)
		{
			text = filter.getTooltip();
		}
		else
		{
			text = null;
		}
		
		super.mouseMoved(x, y);
	}
	
	@Override
	public void draw()
	{
		super.draw();
		render(this);

		if(text != null)
		{
			itemName.setText(text);
			render(itemNameOverlay);
		}
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		if(filter != null)
		{
			filter.draw(renderer, xOffset, yOffset);
		}
	}
}
