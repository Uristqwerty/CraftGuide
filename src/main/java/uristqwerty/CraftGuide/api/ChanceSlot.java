package uristqwerty.CraftGuide.api;

import java.util.List;

/**
 * An ItemSlot that appends probability information to the name of
 *  the item it contains. To use it, instead of putting an ItemStack
 *  or List in the data array, put an Object[] containing the item
 *  at index 0, and an Integer representing the probability at index
 *  1.
 *
 * Also worth noting, this class does not require any additional support
 *  within the rest of CraftGuide. If you copy it into a different package,
 *  and use that copy, it will function just fine with older versions of
 *  CraftGuide, as well as newer ones. It also shows how the API is set up
 *  to make that sort of extension possible.
 */
public class ChanceSlot extends ItemSlot
{
	private int ratio = 100;
	private String formatString = " (%1$.0f%% chance)";

	public ChanceSlot(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}

	public ChanceSlot(int x, int y, int width, int height, boolean drawQuantity)
	{
		super(x, y, width, height, drawQuantity);
	}

	@Override
	public void draw(Renderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver)
	{
		implementation.draw(this, renderer, x, y, stack(data, dataIndex), isMouseOver);
	}

	@Override
	public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
	{
		if(data[dataIndex] == null || stack(data, dataIndex) == null)
		{
			return implementation.getTooltip(this, null);
		}

		List<String> tooltip = implementation.getTooltip(this, stack(data, dataIndex));
		double val = unboxToDouble(((Object[])data[dataIndex])[1]) * 100 / ratio;

		tooltip.set(0, tooltip.get(0) + String.format(formatString, val));
		return tooltip;
	}

	private static double unboxToDouble(Object obj)
	{
		if(obj instanceof Double)
			return (Double)obj;

		else if(obj instanceof Float)
			return (Float)obj;

		else if(obj instanceof Long)
			return (Long)obj;

		else if(obj instanceof Integer)
			return (Integer)obj;

		else if(obj instanceof Short)
			return (Short)obj;

		else if(obj instanceof Character)
			return (Character)obj;

		else if(obj instanceof Byte)
			return (Byte)obj;

		return (Double)obj; // As instanceof Double was already checked, this will throw with a meaningful error message.
	}

	@Override
	public boolean matches(ItemFilter search, Object[] data, int dataIndex, SlotType type)
	{
		return implementation.matches(this, search, stack(data, dataIndex), type);
	}

	@Override
	public ItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
	{
		return implementation.getClickedFilter(x, y, stack(data, dataIndex));
	}

	private Object stack(Object[] data, int dataIndex)
	{
		return data[dataIndex] != null? ((Object[])data[dataIndex])[0] : null;
	}

	public ChanceSlot setRatio(int ratio)
	{
		this.ratio = ratio;
		return this;
	}

	public ChanceSlot setFormatString(String format)
	{
		formatString = format;
		return this;
	}
}
