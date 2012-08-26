package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import uristqwerty.CraftGuide.WIP_API.SlotType;

/**
 * When a recipe is rendered, the ItemSlots provided to the template are
 * used to determine the layout of the recipe's items.
 */
public class ItemSlot implements ISlot
{
	public int x, y, width, height;
	public boolean drawQuantity;
	public SlotType slotType = SlotType.INPUT_SLOT;
	
	public static IItemSlotImplementation implementation;

	public ItemSlot(int x, int y, int width, int height)
	{
		this(x, y, width, height, false);
	}
	
	public ItemSlot(int x, int y, int width, int height, boolean drawQuantity)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.drawQuantity = drawQuantity;
	}

	@Override
	public void init(IRenderer renderer)
	{
	}

	@Override
	public void draw(IRenderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver)
	{
		implementation.draw(this, renderer, x, y, data[dataIndex], isMouseOver);
	}

	@Override
	public List<String> getTooltip(Object[] data, int dataIndex)
	{
		return implementation.getTooltip(this, data[dataIndex]);
	}

	@Override
	public boolean contains(Object search, Object[] data, int dataIndex, SlotType type)
	{
		return implementation.contains(this, search, data[dataIndex], type);
	}
	
	public ItemSlot setSlotType(SlotType type)
	{
		slotType = type;
		return this;
	}
}