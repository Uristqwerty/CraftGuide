package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.api.Renderer;
import uristqwerty.CraftGuide.api.Util;

public class MultipleItemFilter implements ItemFilter
{
	public List<ItemStack> comparison;
	private NamedTexture overlayAny = Util.instance.getTexture("ItemStack-Any");
	private NamedTexture overlayForge = Util.instance.getTexture("ItemStack-OreDict");

	public MultipleItemFilter(List stack)
	{
		comparison = stack;
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
	public void draw(Renderer renderer, int x, int y)
	{
		if(comparison.size() > 0)
		{
			ItemStack stack = comparison.get(0);
			renderer.renderItemStack(x, y, stack);

			if(stack.getItemDamage() == -1)
			{
				renderer.renderRect(x - 1, y - 1, 18, 18, overlayAny);
			}

			renderer.renderRect(x - 1, y - 1, 18, 18, overlayForge);
		}
	}

	@Override
	public List<String> getTooltip()
	{
		if(comparison.size() > 0)
		{
			List<String> text = Util.instance.getItemStackText(comparison.get(0));

			if(comparison.size() > 1)
			{
				text.add("\u00a77Other items:");
				for(int i = 1; i < comparison.size(); i++)
				{
					text.add("\u00a77  " + CommonUtilities.itemName(comparison.get(i)));
				}
			}

			return text;
		}
		else
		{
			return null;
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
