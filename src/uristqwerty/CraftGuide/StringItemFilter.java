package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;

public class StringItemFilter implements IItemFilter
{
	private String comparison;
	
	public StringItemFilter(String string)
	{
		comparison = string;
	}
	
	@Override
	public boolean matches(Object item)
	{
		if(item instanceof ItemStack)
		{
			List list = ((ItemStack)item).getItemNameandInformation();
			
			for(Object o: list)
			{
				if(o instanceof String)
				{
					if(((String)o).indexOf(comparison) != -1)
					{
						return true;
					}
				}
			}
			
			return false;
		}
		else if(item instanceof String)
		{
			return ((String)item).indexOf(comparison) != -1;
		}
		else
		{
			return false;
		}
	}

	@Override
	public ItemStack getDisplayStack()
	{
		return null;
	}

	@Override
	public Object getItem()
	{
		return comparison;
	}
}
