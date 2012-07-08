package uristqwerty.CraftGuide;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;
import net.minecraft.src.ItemStack;

public abstract class ItemFilter implements IItemFilter
{
	public boolean areItemsEqual(ItemStack first, ItemStack second, boolean exactMatch)
	{
		if(first == null)
		{
			return (second == null);
		}
		else if(exactMatch)
		{
			return first.isItemEqual(second);
		}
		else
		{
			return areItemsEqual(first, second);
		}
	}
	
	public boolean areItemsEqual(ItemStack first, ItemStack second)
	{
		return first != null
			&& second != null
			&& first.itemID == second.itemID
			&& (
				first.getItemDamage() == -1 ||
				second.getItemDamage() == -1 ||
				first.getItemDamage() == second.getItemDamage()
			);
	}
}
