package uristqwerty.CraftGuide;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;

public class SingleItemFilter extends ItemFilter
{
	private ItemStack comparison;
	
	public void setItem(ItemStack stack)
	{
		comparison = stack;
	}

	@Override
	public boolean matches(Object stack)
	{
		if(stack instanceof ItemStack)
		{
			return areItemsEqual((ItemStack)stack, comparison);
		}
		else if(stack instanceof ArrayList)
		{
			for(ItemStack item: (ArrayList<ItemStack>)stack)
			{
				if(areItemsEqual(item, comparison))
				{
					return true;
				}
			}
			
			return false;
		}
		else
		{
			return false;
		}
	}

	@Override
	public ItemStack getDisplayStack()
	{
		return comparison;
	}

	@Override
	public Object getItem()
	{
		return comparison;
	}
}
