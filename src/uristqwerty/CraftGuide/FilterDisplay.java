package uristqwerty.CraftGuide;

import java.util.List;

import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;
import uristqwerty.gui.components.GuiElement;
import uristqwerty.gui.rendering.Renderable;
import uristqwerty.gui.rendering.RendererBase;

public class FilterDisplay extends GuiElement implements Renderable
{
	private ItemFilter filter;
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	private Renderable itemNameOverlay = new Overlay(itemName);
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
		if(containsPoint(x, y) && filter != null)
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

	//@Override
	private void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		if(filter != null)
		{
			filter.draw(renderer, xOffset, yOffset);
		}
	}

	@Override
	public void render(RendererBase renderer, int x, int y)
	{
		render((GuiRenderer)renderer, x, y);
	}
}
