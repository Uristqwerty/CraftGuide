package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import uristqwerty.CraftGuide.WIP_API.SlotType;

/**
 * Indicates that this slot represents a recipe output,
 * for use in searches. Added before
 * {@link ItemSlot#setSlotType(SlotType)} existed,
 * .setSlotType(...) should be preferred as this class
 * may eventually be removed.
 */
public class OutputSlot extends ItemSlot
{
	public OutputSlot(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		setSlotType(SlotType.OUTPUT_SLOT);
	}

	public OutputSlot(int x, int y, int width, int height, boolean drawQuantity)
	{
		super(x, y, width, height, drawQuantity);
		setSlotType(SlotType.OUTPUT_SLOT);
	}
}
