package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import uristqwerty.CraftGuide.api.SlotType;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 */

@Deprecated
public interface IItemSlotImplementation
{
	public List<String> getTooltip(ItemSlot itemSlot, Object data);

	public void draw(ItemSlot itemSlot, IRenderer renderer, int x, int y, Object data, boolean isMouseOver);

	public boolean isPointInBounds(ItemSlot itemSlot, int x, int y);

	public IItemFilter getClickedFilter(int x, int y, Object object);

	public boolean matches(ItemSlot itemSlot, IItemFilter search, Object object, SlotType type);
}
