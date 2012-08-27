package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.ItemStack;

public class SingleItemFilter extends ItemFilter
{
	private ItemStack comparison;
	
	public SingleItemFilter(ItemStack stack)
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
		else if(stack instanceof List)
		{
			for(ItemStack item: (List<ItemStack>)stack)
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
