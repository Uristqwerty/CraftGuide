package uristqwerty.CraftGuide;

import java.lang.reflect.Field;

import net.minecraft.src.ItemStack;
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
}
