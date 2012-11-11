package uristqwerty.CraftGuide;

import java.lang.reflect.Field;

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
}
