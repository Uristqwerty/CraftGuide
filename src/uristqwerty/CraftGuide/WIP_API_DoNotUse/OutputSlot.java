package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import uristqwerty.CraftGuide.api.SlotType;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 *
 * Indicates that this slot represents a recipe output,
 * for use in searches. Added before
 * {@link ItemSlot#setSlotType(SlotType)} existed,
 * .setSlotType(...) should be preferred as this class
 * may eventually be removed.
 */
@Deprecated
public class OutputSlot extends ItemSlot
{
	@Deprecated
	public OutputSlot(int x, int y, int width, int height, int index)
	{
		this(x, y, width, height);
	}

	@Deprecated
	public OutputSlot(int x, int y, int width, int height, int index, boolean drawQuantity)
	{
		this(x, y, width, height, drawQuantity);
	}

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
