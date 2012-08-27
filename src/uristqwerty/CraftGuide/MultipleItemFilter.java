package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.ItemStack;

public class MultipleItemFilter extends ItemFilter
{
	private List<ItemStack> comparison;
	
	public MultipleItemFilter(List stack)
	{
		comparison = stack;
	}

	@Override
	public ItemStack getDisplayStack()
	{
		if(comparison == null || comparison.size() < 1)
		{
			return null;
		}
		else
		{
			return comparison.get(0);
		}
	}

	@Override
	public boolean matches(Object stack)
	{
		if(comparison == null)
		{
			return stack == null;
		}
		
		if(stack instanceof ItemStack)
		{
			return matches((ItemStack)stack);
		}
		else if(stack instanceof List)
		{
			for(ItemStack item: (List<ItemStack>)stack)
			{
				if(matches(item))
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
	
	private boolean matches(ItemStack stack)
	{
		for(ItemStack compare: comparison)
		{
			if(areItemsEqual(stack, compare))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Object getItem()
	{
		return comparison;
	}
}
