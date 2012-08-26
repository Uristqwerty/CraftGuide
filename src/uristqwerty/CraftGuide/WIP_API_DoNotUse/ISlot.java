package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import uristqwerty.CraftGuide.WIP_API.SlotType;

public interface ISlot
{
	public void init(IRenderer renderer);
	public void draw(IRenderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver);
	public List<String> getTooltip(Object[] data, int dataIndex);

	/**
	 * Used by {@link IRecipe}.contains(...), to test if a specific slot matches
	 * the searched Object(s).
	 * 
	 * @param search	the Object being compared the the contents of this slot
	 * @param data		the recipe's raw data array
	 * @param dataIndex	this slot's index
	 * @param type		the type of slot that is being searched for
	 */
	public boolean contains(Object search, Object[] data, int dataIndex, SlotType type);
}
