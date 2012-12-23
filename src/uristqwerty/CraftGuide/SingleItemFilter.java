package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.api.Renderer;
import uristqwerty.CraftGuide.api.Util;

public class SingleItemFilter implements ItemFilter
{
	public ItemStack comparison;
	private NamedTexture overlayAny = Util.instance.getTexture("ItemStack-Any");

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
	public void draw(Renderer renderer, int x, int y)
	{
		renderer.renderItemStack(x, y, comparison);

		if(comparison.getItemDamage() == -1)
		{
			renderer.renderRect(x - 1, y - 1, 18, 18, overlayAny);
		}
	}

	@Override
	public List<String> getTooltip()
	{
		return Util.instance.getItemStackText(comparison);
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
