package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import uristqwerty.CraftGuide.api.SlotType;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 *
 * When a recipe is rendered, the ItemSlots provided to the template are
 * used to determine the layout of the recipe's items.
 */

@Deprecated
public class ItemSlot implements ISlot
{
	public int x, y, width, height;
	public boolean drawQuantity, drawBackground;
	public SlotType slotType = SlotType.INPUT_SLOT;

	public static IItemSlotImplementation implementation;

	@Deprecated
	public ItemSlot(int x, int y, int width, int height, int index)
	{
		this(x, y, width, height);
	}

	@Deprecated
	public ItemSlot(int x, int y, int width, int height, int index, boolean drawQuantity)
	{
		this(x, y, width, height, drawQuantity);
	}

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
	public void draw(IRenderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver)
	{
		implementation.draw(this, renderer, x, y, data[dataIndex], isMouseOver);
	}

	@Override
	public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
	{
		return implementation.getTooltip(this, data[dataIndex]);
	}

	@Override
	public boolean matches(IItemFilter search, Object[] data, int dataIndex, SlotType type)
	{
		return implementation.matches(this, search, data[dataIndex], type);
	}

	@Override
	public boolean isPointInBounds(int x, int y, Object[] data, int dataIndex)
	{
		return implementation.isPointInBounds(this, x, y);
	}

	@Override
	public IItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
	{
		return implementation.getClickedFilter(x, y, data[dataIndex]);
	}

	public ItemSlot drawOwnBackground(boolean draw)
	{
		drawBackground = draw;
		return this;
	}

	public ItemSlot drawOwnBackground()
	{
		return drawOwnBackground(true);
	}

	/**
	 * Sets the {@link SlotType} associated with this ItemSlot.
	 * The SlotType is used for searches, both from in-game, and
	 * from the use of the API
	 * @param type
	 * @return this ItemSlot, to permit method chaining
	 */
	public ItemSlot setSlotType(SlotType type)
	{
		slotType = type;
		return this;
	}
}