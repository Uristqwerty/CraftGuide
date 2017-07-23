package uristqwerty.gui_craftguide.theme.reader;

import java.lang.reflect.Field;

import uristqwerty.gui_craftguide.editor.TextureMeta.Unit;
import uristqwerty.gui_craftguide.editor.TextureMeta.WithUnits;
import uristqwerty.gui_craftguide.theme.Theme;

public class IntegerElement extends PrimitiveValueTemplate
{
	Long value = 0L;
	private final WithUnits units;

	public IntegerElement(WithUnits units)
	{
		this.units = units;
	}

	@Override
	public void characters(Theme theme, char[] chars, int start, int length)
	{
		String str = String.valueOf(chars, start, length);
		double multiplier = 1;
		if(units != null)
		{
			String lower = str.toLowerCase();
			int longestMatch = 0;
			for(Unit u: units.value())
			{
				for(String unitName: u.names())
				{
					if(lower.endsWith(unitName.toLowerCase()) && unitName.length() > longestMatch)
					{
						multiplier = u.multiplier();
						longestMatch = unitName.length();
					}
				}
			}
			if(longestMatch > 0)
			{
				str = str.substring(0, str.length() - longestMatch).trim();
			}
		}
		try
		{
			value = (long)(Long.parseLong(str) * multiplier);
		}
		catch(NumberFormatException e)
		{
		}
	}

	@Override
	public Class<?> valueType()
	{
		return Long.class;
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public boolean setField(Field field, Object object) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> type = field.getType();
		if(type == Long.class || type == long.class)
		{
			field.set(object, value);
		}
		else if(type == Integer.class || type == int.class)
		{
			field.set(object, value.intValue());
		}
		else if(type == Short.class || type == short.class)
		{
			field.set(object, value.shortValue());
		}
		else if(type == Byte.class || type == byte.class)
		{
			field.set(object, value.byteValue());
		}
		else
		{
			return false;
		}

		return true;
	}
}
