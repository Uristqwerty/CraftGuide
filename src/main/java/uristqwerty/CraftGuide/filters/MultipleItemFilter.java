package uristqwerty.CraftGuide.filters;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.api.CombinableItemFilter;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.LiquidFilter;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.api.PseudoFluidFilter;
import uristqwerty.CraftGuide.api.Renderer;
import uristqwerty.CraftGuide.api.Util;

public class MultipleItemFilter implements CombinableItemFilter
{
	public final List<ItemStack> comparison;
	private static final NamedTexture overlayAny = Util.instance.getTexture("ItemStack-Any");
	private static final NamedTexture overlayForge = Util.instance.getTexture("ItemStack-OreDict");
	private static final NamedTexture overlayForgeSingle = Util.instance.getTexture("ItemStack-OreDict-Single");
	private List<String> tooltip = null;

	public MultipleItemFilter(List<ItemStack> stack)
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
			for(Object item: (List<?>)stack)
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
			if(CommonUtilities.checkItemStackMatch(stack, compare))
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

			if(CommonUtilities.getItemDamage(stack) == CraftGuide.DAMAGE_WILDCARD)
			{
				renderer.renderRect(x - 1, y - 1, 18, 18, overlayAny);
			}

			if(comparison.size() == 1)
			{
				renderer.renderRect(x - 1, y - 1, 18, 18, overlayForgeSingle);
			}
			else
			{
				renderer.renderRect(x - 1, y - 1, 18, 18, overlayForge);
			}
		}
	}

	@Override
	public List<String> getTooltip()
	{
		if(tooltip == null)
		{
			if(comparison.size() > 0)
			{
				ItemStack primaryItem = comparison.get(0);

				List<String> text;

				if(CommonUtilities.getItemDamage(primaryItem) == CraftGuide.DAMAGE_WILDCARD)
				{
					if(primaryItem.getHasSubtypes())
					{
						ArrayList<ItemStack> list = new ArrayList<>();
						primaryItem.getItem().getSubItems(primaryItem.getItem(), null, list);
						text = Util.instance.getItemStackText(list.get(0));
					}
					else
					{
						ItemStack alteredStack = primaryItem.copy();
						alteredStack.setItemDamage(0);
						text = Util.instance.getItemStackText(alteredStack);
					}
				}
				else
				{
					text = Util.instance.getItemStackText(primaryItem);
				}

				if(comparison.size() > 1)
				{
					text.add("\u00a77Other items:");

					if(CommonUtilities.getItemDamage(primaryItem) == CraftGuide.DAMAGE_WILDCARD && primaryItem.getHasSubtypes())
					{
						ArrayList<ItemStack> list = new ArrayList<>();
						primaryItem.getItem().getSubItems(primaryItem.getItem(), null, list);

						for(int i = 1; i < list.size(); i++)
						{
							text.add("\u00a77  " + CommonUtilities.itemName(list.get(i)));
						}
					}

					for(int i = 1; i < comparison.size(); i++)
					{
						for(String name: CommonUtilities.itemNames(comparison.get(i)))
						{
							text.add("\u00a77  " + name);
						}
					}
				}

				tooltip = text;
			}
			else
			{
				return null;
			}
		}

		return tooltip;
	}

	@Override
	public ItemFilter addItemFilter(ItemFilter other)
	{
		if(other instanceof CombinableItemFilter)
		{
			List<ItemStack> otherItems = ((CombinableItemFilter)other).getRepresentativeItems();

			if(otherItems != null)
			{
				return Util.instance.getCommonFilter(Util.instance.addItemLists(getRepresentativeItems(), otherItems));
			}
		}
		else if(other instanceof StringItemFilter || other instanceof LiquidFilter || other instanceof PseudoFluidFilter)
		{
			return new MultiFilter(this).addItemFilter(other);
		}

		return null;
	}

	@Override
	public ItemFilter subtractItemFilter(ItemFilter other)
	{
		if(other instanceof CombinableItemFilter)
		{
			List<ItemStack> otherItems = ((CombinableItemFilter)other).getRepresentativeItems();

			if(otherItems != null)
			{
				return Util.instance.getCommonFilter(Util.instance.subtractItemLists(getRepresentativeItems(), otherItems));
			}
		}

		return null;
	}

	@Override
	public List<ItemStack> getRepresentativeItems()
	{
		ArrayList<ItemStack> list = new ArrayList<>(comparison.size());

		for(ItemStack stack: comparison)
		{
			if(stack.stackSize != 1)
			{
				stack = stack.copy();
				stack.stackSize = 1;
			}

			list.add(stack);
		}

		return list;
	}
}
