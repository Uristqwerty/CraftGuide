package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import uristqwerty.CraftGuide.WIP_API.SlotType;

public interface IItemSlotImplementation
{
	public List<String> getTooltip(ItemSlot itemSlot, Object data);
	
	public void draw(ItemSlot itemSlot, IRenderer renderer, int x, int y, Object data, boolean isMouseOver);

	public boolean contains(ItemSlot itemSlot, Object search, Object data, SlotType type);
}
