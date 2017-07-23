package uristqwerty.gui_craftguide.theme.reader;

import java.lang.reflect.Field;

import uristqwerty.gui_craftguide.editor.TextureMeta.Unit;
import uristqwerty.gui_craftguide.editor.TextureMeta.WithUnits;
import uristqwerty.gui_craftguide.theme.Theme;

public class DoubleElement extends PrimitiveValueTemplate
{
	Double value = 0.0;
	private final WithUnits units;

	public DoubleElement(WithUnits units)
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
			value = Double.parseDouble(str) * multiplier;
		}
		catch(NumberFormatException e)
		{
		}
	}

	@Override
	public Class<?> valueType()
	{
		return Double.class;
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
		if(type == Double.class || type == double.class)
		{
			field.set(object, value);
		}
		else if(type == Float.class || type == float.class)
		{
			field.set(object, value.floatValue());
		}
		else
		{
			return false;
		}

		return true;
	}
}
