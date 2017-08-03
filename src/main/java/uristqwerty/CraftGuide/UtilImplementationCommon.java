package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.client.ui.GuiRenderer;
import uristqwerty.CraftGuide.filters.EmptyOreDictionaryFilter;
import uristqwerty.CraftGuide.filters.MultipleItemFilter;
import uristqwerty.CraftGuide.filters.NoItemFilter;
import uristqwerty.CraftGuide.filters.SingleItemFilter;
import uristqwerty.CraftGuide.filters.StringItemFilter;

public abstract class UtilImplementationCommon extends Util
{
	private static final List<ItemStack> specialEmptyItemList = new ArrayList<>(0);

	public float partialTicks;

	@Override
	public ItemFilter getCommonFilter(Object stack)
	{
		if(stack == null)
		{
			return null;
		}
		else if(stack instanceof ItemStack)
		{
			return new SingleItemFilter((ItemStack)stack);
		}
		else if(stack instanceof List)
		{
			if(((List<ItemStack>)stack).size() > 0)
			{
				return new MultipleItemFilter((List<ItemStack>)stack);
			}
			else if(stack == specialEmptyItemList)
			{
				return new NoItemFilter();
			}
			else
			{
				String oreDictionaryName = ForgeExtensions.getOreDictionaryName((List<ItemStack>)stack);
				if(oreDictionaryName != null)
				{
					return new EmptyOreDictionaryFilter((List<ItemStack>)stack, oreDictionaryName);
				}
			}
		}
		else if(stack instanceof String)
		{
			return new StringItemFilter((String)stack);
		}

		return null;
	}

	@Override
	public List<String> getItemStackText(ItemStack stack)
	{
		try
		{
			List<String> list = ((GuiRenderer)GuiRenderer.instance).getItemNameandInformation(stack);

			if(CommonUtilities.getItemDamage(stack) == CraftGuide.DAMAGE_WILDCARD && (list.size() < 1 || (list.size() == 1 && (list.get(0) == null || list.get(0).isEmpty()))))
			{
				list = ((GuiRenderer)GuiRenderer.instance).getItemNameandInformation(GuiRenderer.fixedItemStack(stack));
			}

			List<String> text = new ArrayList<>(list.size());
			boolean first = true;

			for(Object o: list)
			{
				if(o instanceof String)
				{
					if(first)
					{
						EnumRarity rarity = null;

						try
						{
							rarity = stack.getRarity();
						}
						catch(NullPointerException e)
						{
						}

						if(rarity == null)
						{
							rarity = EnumRarity.COMMON;
						}

						text.add(rarity.rarityColor + (String)o);

						if(CraftGuide.alwaysShowID)
						{
							text.add(EnumChatFormatting.DARK_GRAY +
									"ID: " + Item.itemRegistry.getNameForObject(stack.getItem()) +
									"; data: " + CommonUtilities.getItemDamage(stack) +
									(stack.hasTagCompound()?
											"; NBT: " +Integer.toHexString(stack.getTagCompound().hashCode()) : ""
									));
						}

						first = false;
					}
					else
					{
						text.add(EnumChatFormatting.DARK_GRAY + (String)o);
					}
				}
			}

			return text;
		}
		catch(Exception e)
		{
			CraftGuideLog.log(e);

			List<String> text = new ArrayList<>(1);
			text.add(EnumChatFormatting.YELLOW + "Item " + Item.itemRegistry.getNameForObject(stack.getItem()) + " data " + Integer.toString(CommonUtilities.getItemDamage(stack)));
			return text;
		}
	}

	@Override
	public void reloadRecipes()
	{
		CraftGuide.side.reloadRecipes();
	}

	@Override
	public float getPartialTicks()
	{
		return partialTicks;
	}

	@Override
	public List<ItemStack> addItemLists(List<ItemStack> a, List<ItemStack> b)
	{
		ArrayList<ItemStack> result = new ArrayList<>();
		addUniqueItemsToList(result, a);
		addUniqueItemsToList(result, b);
		return result;
	}

	@Override
	public List<ItemStack> subtractItemLists(List<ItemStack> a, List<ItemStack> b)
	{
		ArrayList<ItemStack> result = new ArrayList<>();
		addUniqueItemsToList(result, a);
		removeItemsFromList(result, b);

		if(result.isEmpty())
			return specialEmptyItemList;

		return result;
	}

	private void addUniqueItemsToList(ArrayList<ItemStack> result, List<ItemStack> a)
	{
		outer:
		for(ItemStack item: a)
		{
			for(ItemStack t: result)
			{
				if(CommonUtilities.checkItemStackMatch(item, t))
					continue outer;
			}

			result.add(item);
		}
	}

	private void removeItemsFromList(ArrayList<ItemStack> result, List<ItemStack> a)
	{
		Iterator<ItemStack> i = result.iterator();

		while(i.hasNext())
		{
			ItemStack stack = i.next();

			for(ItemStack t: a)
			{
				if(CommonUtilities.checkItemStackMatch(stack, t))
				{
					i.remove();
					break;
				}
			}
		}
	}
}
