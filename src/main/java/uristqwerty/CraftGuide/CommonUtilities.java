package uristqwerty.CraftGuide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.StackInfo;
import uristqwerty.CraftGuide.api.StackInfoSource;
import uristqwerty.CraftGuide.api.Util;

public class CommonUtilities
{
	public static Field getPrivateField(Class<?> fromClass, String... names) throws NoSuchFieldException
	{
		Field field = null;

		for(String name: names)
		{
			try
			{
				field = fromClass.getDeclaredField(name);
				field.setAccessible(true);
				return field;
			}
			catch(NoSuchFieldException e)
			{
			}
		}

		if(names.length == 1)
		{
			throw new NoSuchFieldException("Could not find a field named " + names[0]);
		}
		else
		{
			String nameStr = "[" + names[0];

			for(int i = 1; i < names.length; i++)
			{
				nameStr += ", " + names[i];
			}

			throw new NoSuchFieldException("Could not find a field with any of the following names: " + nameStr + "]");
		}
	}

	public static <T> Object getPrivateValue(Class<T> objectClass, T object, String... names) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field = getPrivateField(objectClass, names);
		return field.get(object);
	}

	public static String itemName(ItemStack item)
	{
		String idText = "";

		if(Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
		{
			if(item.getHasSubtypes())
			{
				idText = String.format(" (%s #%04d/%d)", Item.REGISTRY.getNameForObject(item.getItem()), Item.getIdFromItem(item.getItem()), getItemDamage(item));
			}
			else
			{
				idText = String.format(" (%s #%04d)", Item.REGISTRY.getNameForObject(item.getItem()), Item.getIdFromItem(item.getItem()));
			}
		}
		return item.getDisplayName() + idText;
	}

	public static List<String> itemNames(ItemStack item)
	{
		ArrayList<String> list = new ArrayList<>();

		if(getItemDamage(item) == CraftGuide.DAMAGE_WILDCARD && item.getHasSubtypes())
		{
			ArrayList<ItemStack> subItems = new ArrayList<>();
			item.getItem().getSubItems(item.getItem(), null, subItems);

			for(ItemStack stack: subItems)
			{
				list.add(itemName(stack));
			}
		}
		else
		{
			list.add(itemName(item));
		}

		return list;
	}

	public static int countItemNames(ItemStack item)
	{
		if(getItemDamage(item) == CraftGuide.DAMAGE_WILDCARD && item.getHasSubtypes())
		{
			ArrayList<ItemStack> temp = new ArrayList<>();
			item.getItem().getSubItems(item.getItem(), null, temp);

			return temp.size();
		}
		else
		{
			return 1;
		}
	}

	public static int countItemNames(Object item)
	{
		if(item instanceof ItemStack)
		{
			return countItemNames((ItemStack)item);
		}
		else if(item instanceof List)
		{
			int count = 0;
			for(Object o: (List<?>)item)
			{
				count += countItemNames(o);
			}

			return count;
		}
		else
		{
			return 0;
		}
	}

	private static Map<Pair<Item, Integer>, List<String>> textCache = new HashMap<>();

	static void clearTooltipCache()
	{
		textCache.clear();
	}

	private static List<String> cachedExtendedItemStackText(ItemStack stack)
	{
		try
		{
			Pair<Item, Integer> key = new Pair<>(stack.getItem(), stack.getItemDamage());

			List<String> tooltip = textCache.get(key);

			if(tooltip == null)
			{
				tooltip = new ArrayList<>(genExtendedItemStackText(stack));
				textCache.put(key, tooltip);
			}

			return tooltip;
		}
		catch (Throwable e)
		{
			CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.CommonUtilities.cachedExtendedItemStackText item " + (stack != null? stack.getClass() : "null"));
			throw new RuntimeException(e);
		}
	}

	public static List<String> getExtendedItemStackText(Object item)
	{
		try
		{
			List<String> text = getPossiblyCachedExtendedItemText(item);

			if(item instanceof List && ((List<?>)item).size() > 1)
			{
				int count = CommonUtilities.countItemNames(item);
				text.add("\u00a77" + (count - 1) + " other type" + (count > 2? "s" : "") + " of item accepted");
			}

			return text;
		}
		catch (Throwable e)
		{
			CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.CommonUtilities.getExtendedItemStackText item " + (item != null? item.getClass() : "null"));
			throw new RuntimeException(e);
		}
	}

	private static List<String> nullItemText, nullItemStackText;
	static
	{
		nullItemText = new ArrayList<>();
		nullItemText.add("Error: ItemStack has null item!");
		nullItemText.add("This is a bug in one of the mods adding recipes.");

		nullItemStackText = new ArrayList<>();
		nullItemStackText.add("null ItemStack (might be a bug)");
	}

	private static List<String> getPossiblyCachedExtendedItemText(Object item)
	{
		if(item instanceof ItemStack || (item instanceof List && ((List<?>)item).size() > 0 && ((List<?>)item).get(0) instanceof ItemStack))
		{
			ItemStack stack = item instanceof ItemStack? (ItemStack)item : (ItemStack)((List<?>)item).get(0);

			if(stack == null)
				return nullItemStackText;
			if(stack.getItem() == null)
				return nullItemText;

			List<String> tooltip;
			if(stack.hasTagCompound())
				tooltip = genExtendedItemStackText(stack);
			else
				tooltip = new ArrayList<>(cachedExtendedItemStackText(stack));

			if(item instanceof List)
			{
				String name = ForgeExtensions.getOreDictionaryName((List<?>)item);
				if(name != null)
				{
					tooltip.add("Ore dictionary name: '" + name + "'");
				}
			}
			return tooltip;
		}
		else
		{
			return getItemStackText(item);
		}
	}

	private static List<String> genExtendedItemStackText(ItemStack stack)
	{
		List<String> text = getItemStackText(stack);
		appendStackInfo(text, stack);
		return text;
	}

	private static void appendStackInfo(List<String> text, ItemStack stack)
	{
		try
		{
			Iterator<StackInfoSource> iterator = StackInfo.sources.iterator();
			while(iterator.hasNext())
			{
				StackInfoSource infoSource = iterator.next();
				try
				{
					String info = infoSource.getInfo(stack);

					if(info != null && !info.isEmpty())
					{
						if(info.indexOf('\n') == -1)
						{
							text.add(info);
						}
						else
						{
							text.addAll(Arrays.asList(info.split("\n")));
						}
					}
				}
				catch(LinkageError e)
				{
					CraftGuideLog.log(e);
					iterator.remove();
				}
				catch(Exception e)
				{
					CraftGuideLog.log(e);
				}
			}
		}
		catch (Throwable e)
		{
			CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.CommonUtilities.appendStackInfo stack " + (stack != null? stack.getClass() : "null"));
			throw new RuntimeException(e);
		}
	}

	public static boolean searchExtendedItemStackText(Object item, String text)
	{
		try
		{
			List<String> lines = getExtendedItemStackText(item);

			if(lines != null)
			{
				for(String line: lines)
				{
					if(line != null && line.toLowerCase().contains(text))
					{
						return true;
					}
				}
			}

			return false;
		}
		catch (Throwable e)
		{
			CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.CommonUtilities.searchExtendedItemStackText item " + (item != null? item.getClass() : "null"));
			throw new RuntimeException(e);
		}
	}

	private static List<String> getItemStackText(Object item)
	{
		try
		{
			if(item instanceof List)
			{
				List<?> list = ((List<?>)item);

				if(list.size() > 0)
				{
					return getItemStackText(list.get(0));
				}
				else
				{
					return null;
				}
			}
			else if(item instanceof ItemStack)
			{
				return Util.instance.getItemStackText((ItemStack)item);
			}
			else
			{
				return null;
			}
		}
		catch (Throwable e)
		{
			CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.CommonUtilities.getItemStackText item " + (item != null? item.getClass() : "null"));
			throw new RuntimeException(e);
		}
	}

	static Field itemDamageField = null;

	static
	{
		try
		{
			itemDamageField = getPrivateField(ItemStack.class, "itemDamage", "field_77991_e", "e");
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	public static int getItemDamage(ItemStack stack)
	{
		if(stack.getItem() != null)
		{
			return stack.getItemDamage();
		}
		else
		{
			if(itemDamageField != null)
			{
				try
				{
					return itemDamageField.getInt(stack);
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					CraftGuideLog.log(e, "", true);
				}
			}

			return 0;
		}
	}

	public static boolean checkItemStackMatch(ItemStack first, ItemStack second)
	{
		if(first == null || second == null)
		{
			return first == second;
		}

		return first.getItem() == second.getItem()
			&& (
				getItemDamage(first) == CraftGuide.DAMAGE_WILDCARD ||
				getItemDamage(second) == CraftGuide.DAMAGE_WILDCARD ||
				getItemDamage(first) == getItemDamage(second)
			)
			&& (
				!first.hasTagCompound() ||
				!second.hasTagCompound() ||
				ItemStack.areItemStackTagsEqual(first, second)
			);
	}
}
