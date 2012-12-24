package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.SlotType;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 */

@Deprecated
public class ExtraSlot extends ItemSlot
{
	public Object displayed;
	public boolean showName = false;
	public boolean canClick = false;

	@Deprecated
	public boolean canFilter = false;

	@Deprecated
	public ExtraSlot(int x, int y, int width, int height, int index, ItemStack displayedItem)
	{
		this(x, y, width, height, displayedItem);
	}

	public ExtraSlot(int x, int y, int width, int height, Object displayedItem)
	{
		super(x, y, width, height);
		displayed = displayedItem;
	}

	@Override
	public void draw(IRenderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver)
	{
		implementation.draw(this, renderer, x, y, displayed, canClick && isMouseOver);
	}

	@Override
	public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
	{
		if(showName)
		{
			return implementation.getTooltip(this, displayed);
		}
		else
		{
			return null;
		}
	}

	@Override
	public IItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
	{
		if(canClick)
		{
			return implementation.getClickedFilter(x, y, displayed);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets whether this slot, if clicked, sets the current
	 * filter to the displayed Object, or doesn't do anything
	 *
	 * @param clickable
	 * @return this ExtraSlot, to permit method chaining
	 */
	public ExtraSlot clickable(boolean clickable)
	{
		canClick = clickable;
		return this;
	}

	/**
	 * Sets whether this slot shows a tooltip on mouseover
	 *
	 * @param showName
	 * @return this ExtraSlot, to permit method chaining
	 */
	public ExtraSlot showName(boolean showName)
	{
		this.showName = showName;
		return this;
	}

	/**
	 * Sets whether this slot is considered when searching.
	 * <br><br>
	 * {@link ItemSlot#setSlotType(SlotType)} is preferred, as
	 * it is more flexible and specific
	 *
	 * @param filterable
	 * @return this ExtraSlot, to permit method chaining
	 */
	@Deprecated
	public ExtraSlot filterable(boolean filterable)
	{
		if(filterable)
		{
			setSlotType(SlotType.MACHINE_SLOT);
		}
		else
		{
			setSlotType(SlotType.DISPLAY_SLOT);
		}
		canFilter = filterable;
		return this;
	}

	/**
	 * A shortened version of {@link #clickable(boolean)}
	 * that passes true
	 *
	 * @return this ExtraSlot, to permit method chaining
	 */
	public ExtraSlot clickable()
	{
		return clickable(true);
	}

	/**
	 * A shortened version of {@link #showName(boolean)}
	 * that passes true
	 *
	 * @return this ExtraSlot, to permit method chaining
	 */
	public ExtraSlot showName()
	{
		return showName(true);
	}

	/**
	 * A shortened version of {@link #filterable(boolean)}
	 * that passes true
	 * <br><br>
	 * {@link ItemSlot#setSlotType(SlotType)} is preferred, as
	 * it is more flexible and specific
	 *
	 * @return this ExtraSlot, to permit method chaining
	 */
	@Deprecated
	public ExtraSlot filterable()
	{
		return filterable(true);
	}
}
