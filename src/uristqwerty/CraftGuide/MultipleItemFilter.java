package uristqwerty.CraftGuide;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;

public class MultipleItemFilter extends ItemFilter
{
	private ArrayList<ItemStack> comparison;
	
	public void setItems(ArrayList stack)
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
		else if(stack instanceof ArrayList)
		{
			for(ItemStack item: (ArrayList<ItemStack>)stack)
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
