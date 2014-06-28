package uristqwerty.CraftGuide;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
	public static Field getPrivateField(Class fromClass, String... names) throws NoSuchFieldException
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
				idText = String.format(" (%04d/%d)", Item.itemRegistry.getNameForObject(item.getItem()), getItemDamage(item));
			}
			else
			{
				idText = String.format(" (%04d)", Item.itemRegistry.getNameForObject(item.getItem()));
			}
		}

		return item.getDisplayName() + idText;
	}

	public static List<String> itemNames(ItemStack item)
	{
		ArrayList<String> list = new ArrayList<String>();

		if(getItemDamage(item) == CraftGuide.DAMAGE_WILDCARD && item.getHasSubtypes())
		{
			ArrayList<ItemStack> subItems = new ArrayList();
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
			ArrayList temp = new ArrayList();
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
			for(Object o: (List)item)
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

	private static class Pair<T1, T2>
	{
		T1 first;
		T2 second;

		public Pair(T1 a, T2 b)
		{
			first = a;
			second = b;
		}

		@Override
		public int hashCode()
		{
			// Null hashes arbitrarily chosen by keyboard mashing.
			int firsthash = first == null? 5960343 : first.hashCode();
			int secondhash = second == null? 1186323 : second.hashCode();
			return firsthash ^ Integer.rotateLeft(secondhash, 13);
		}

		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof Pair))
				return false;

			Pair other = (Pair)obj;

			return (first == null? other.first == null : first.equals(other.first)) &&
					(second == null? other.second == null : second.equals(other.second));
		}
	}

	private static Map<Pair<Item, Integer>, List<String>> textCache = new HashMap<Pair<Item, Integer>, List<String>>();

	static void clearTooltipCache()
	{
		textCache.clear();
	}

	private static List<String> cachedExtendedItemStackText(ItemStack stack)
	{
		Pair<Item, Integer> key = new Pair(stack.getItem(), stack.getItemDamage());

		List<String> tooltip = textCache.get(key);

		if(tooltip == null)
		{
			tooltip = new ArrayList(genExtendedItemStackText(stack));
			textCache.put(key, tooltip);
		}

		return tooltip;
	}

	public static List<String> getExtendedItemStackText(Object item)
	{
		List<String> text = getPossiblyCachedExtendedItemText(item);

		if(item instanceof List && ((List)item).size() > 1)
		{
			int count = CommonUtilities.countItemNames(item);
			text.add("\u00a77" + (count - 1) + " other type" + (count > 2? "s" : "") + " of item accepted");
		}

		return text;
	}

	private static List<String> getPossiblyCachedExtendedItemText(Object item)
	{
		if(item instanceof ItemStack || (item instanceof List && ((List)item).get(0) instanceof ItemStack))
		{
			ItemStack stack = item instanceof ItemStack? (ItemStack)item : (ItemStack)((List)item).get(0);

			if(stack.hasTagCompound())
				return genExtendedItemStackText(stack);
			else
				return new ArrayList(cachedExtendedItemStackText(stack));
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
		Iterator<StackInfoSource> iterator = StackInfo.sources.iterator();
		while(iterator.hasNext())
		{
			StackInfoSource infoSource = iterator.next();
			try
			{
				String info = infoSource.getInfo(stack);

				if(info != null)
				{
					text.add(info);
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

	public static boolean searchExtendedItemStackText(Object item, String text)
	{
		for(String line: getExtendedItemStackText(item))
		{
			if(line != null && line.toLowerCase().contains(text))
			{
				return true;
			}
		}

		return false;
	}

	private static List<String> getItemStackText(Object item)
	{
		if(item instanceof List)
		{
			return getItemStackText(((List)item).get(0));
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

	static Field itemDamageField = null;

	static
	{
		try
		{
			itemDamageField = getPrivateField(ItemStack.class, "itemDamage", "field_77991_e", "e");
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
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
				catch(IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch(IllegalAccessException e)
				{
					e.printStackTrace();
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
			);
	}
}
