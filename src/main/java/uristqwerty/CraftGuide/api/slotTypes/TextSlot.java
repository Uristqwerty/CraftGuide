package uristqwerty.CraftGuide.api.slotTypes;

import java.util.Collection;
import java.util.List;

import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.Renderer;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class TextSlot implements Slot
{
	private int color = 0xffffffff;
	private int x, y;

	public TextSlot(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(Renderer renderer, int x, int y, Object[] dataArray, int dataIndex, boolean isMouseOver)
	{
		Object data = dataArray[dataIndex];
		if(data instanceof String)
		{
			renderer.renderText(x + this.x, y + this.y, (String)data, color, false);
		}
		else if(data instanceof String[])
		{
			for(String s: (String[])data)
			{
				renderer.renderText(x + this.x, y + this.y, s, color, false);
				y += 8;
			}
		}
		else if(data instanceof Collection)
		{
			for(String s: (Collection<String>)data)
			{
				renderer.renderText(x + this.x, y + this.y, s, color, false);
				y += 8;
			}
		}
	}

	@Override
	public ItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
	{
		return null;
	}

	@Override
	public boolean isPointInBounds(int x, int y, Object[] data, int dataIndex)
	{
		return false;
	}

	@Override
	public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
	{
		return null;
	}

	@Override
	public boolean matches(ItemFilter filter, Object[] data, int dataIndex, SlotType type)
	{
		return false;
	}
}
