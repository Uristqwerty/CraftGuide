package uristqwerty.CraftGuide.recipes;

import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.Slot;

public interface FuelInfoSource
{
	public Slot getSlot(int x, int y);
	public boolean hasInfo(ItemStack stack);
	public Object getData(ItemStack stack);
}
