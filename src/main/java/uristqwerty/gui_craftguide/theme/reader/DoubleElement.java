package uristqwerty.gui_craftguide.theme.reader;

import java.lang.reflect.Field;

import uristqwerty.gui_craftguide.theme.Theme;

public class DoubleElement extends PrimitiveValueTemplate
{
	Double value = 0.0;

	@Override
	public void characters(Theme theme, char[] chars, int start, int length)
	{
		try
		{
			value = Double.parseDouble(String.valueOf(chars, start, length));
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
