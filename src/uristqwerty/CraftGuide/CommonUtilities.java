package uristqwerty.CraftGuide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.StackInfo;
import uristqwerty.CraftGuide.api.StackInfoSource;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.client.CraftGuideClient;

public class CommonUtilities
{
	public static <T> Object getPrivateValue(Class<T> objectClass, T object, String obfuscatedName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field = objectClass.getDeclaredField(obfuscatedName);
		field.setAccessible(true);
		return field.get(object);
	}

	public static <T> Object getPrivateValue(Class<T> objectClass, T object, String obfuscatedName, String deobfuscatedName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field;

		try
		{
			field = objectClass.getDeclaredField(obfuscatedName);
		}
		catch(NoSuchFieldException e)
		{
			field = objectClass.getDeclaredField(deobfuscatedName);
		}

		field.setAccessible(true);
		return field.get(object);
	}

	public static String itemName(ItemStack item)
	{
		String idText = "";

		if(((CraftGuideClient)CraftGuide.side).getMinecraftInstance().gameSettings.advancedItemTooltips)
		{
			if(item.getHasSubtypes())
			{
				idText = String.format(" (#%04d/%d)", item.itemID, item.getItemDamage());
			}
			else
			{
				idText = String.format(" (#%04d)", item.itemID);
			}
		}

		return item.getDisplayName() + idText;
	}

	public static List<String> itemNames(ItemStack item)
	{
		ArrayList<String> list = new ArrayList<String>();

		if(item.getItemDamage() == -1 && item.getHasSubtypes())
		{
			ArrayList<ItemStack> subItems = new ArrayList();
			item.getItem().getSubItems(item.itemID, null, subItems);

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
		if(item.getItemDamage() == -1 && item.getHasSubtypes())
		{
			ArrayList temp = new ArrayList();
			item.getItem().getSubItems(item.itemID, null, temp);

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

	public static List<String> getExtendedItemStackText(Object item)
	{
		List<String> text = getItemStackText(item);

		if(item instanceof ItemStack || (item instanceof List && ((List)item).get(0) instanceof ItemStack))
		{
			for(StackInfoSource infoSource: StackInfo.sources)
			{
				String info = infoSource.getInfo(item instanceof ItemStack? (ItemStack)item : (ItemStack)((List)item).get(0));

				if(info != null)
				{
					text.add(info);
				}
			}
		}

		if(item instanceof List && ((List)item).size() > 1)
		{
			int count = CommonUtilities.countItemNames(item);
			text.add("\u00a77" + (count - 1) + " other type" + (count > 2? "s" : "") + " of item accepted");
		}

		return text;
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
}
