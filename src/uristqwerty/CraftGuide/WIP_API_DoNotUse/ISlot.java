package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import uristqwerty.CraftGuide.WIP_API.SlotType;

public interface ISlot
{
	public void draw(IRenderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver);
	
	/**
	 * When this slot is clicked on in a recipe, this method is called in
	 * order to get the filter Object that is used to search the recipe list
	 * for related recipes. If null is returned, then the filter will not
	 * change.
	 * <br><br>
	 * For common types, {@link Util#getCommonFilter(Object)} can provide
	 * a filter if given a known type of Object.
	 * @param x clicked coordinate, relative to the recipe's top left corner
	 * @param y clicked coordinate, relative to the recipe's top left corner
	 * @param data the Object[] representing the recipe
	 * @param dataIndex this slot's index in data
	 * @return null, or an IItemFilter
	 */
	public IItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex);
	
	/**
	 * This method is used by CraftGuide to determine whether a specific point
	 * is considered within the bounds of this particular slot. It is typically
	 * used to determine what slot, if any, the user's cursor is currently over.
	 * 
	 * @param x coordinate, relative to the recipe's top left corner
	 * @param y coordinate, relative to the recipe's top left corner
	 * @param data the Object[] representing the recipe
	 * @param dataIndex this slot's index in data
	 * @return true if (x, y) is within this slot's bounds, otherwise false
	 */
	public boolean isPointInBounds(int x, int y, Object[] data, int dataIndex);
	
	/**
	 * When the user moves their cursor over a recipe containing this slot, and
	 * the cursor's position is within the bounds of this slot (relative to the
	 * containing recipe), this method is called to determine whether to display
	 * a tooltip, and, if so, what text should be displayed.
	 * 
	 * @param x cursor coordinate, relative to the recipe's top left corner
	 * @param y cursor coordinate, relative to the recipe's top left corner
	 * @param data the Object[] representing the recipe
	 * @param dataIndex this slot's index in data
	 * @return a List of Strings, representing one or more lines of text, or
	 * null if the tooltip should not be displayed
	 */
	public List<String> getTooltip(int x, int y, Object[] data, int dataIndex);

	/**
	 * Used to test if a specific slot matches the searched {@link IItemFilter}
	 * 
	 * @param filter	the Object being compared the the contents of this slot
	 * @param data		the recipe's raw data array
	 * @param dataIndex	this slot's index
	 * @param type		the type of slot that is being searched for
	 */
	public boolean matches(IItemFilter filter, Object[] data, int dataIndex, SlotType type);
}
