package uristqwerty.gui.theme.reader;

import java.lang.reflect.Field;

import uristqwerty.gui.Color;
import uristqwerty.gui.Rect;
import uristqwerty.gui.editor.TextureMeta.ListSize;

public class ValueType
{
	public static ValueTemplate getTemplate(Field field)
	{
		if(field.getType().isArray() && field.isAnnotationPresent(ListSize.class))
		{
			ListSize size = field.getAnnotation(ListSize.class);
			return new ListTemplate(field.getType(), size.value());
		}

		return getTemplate(field.getType());
	}

	public static ValueTemplate getTemplate(Class type)
	{
		if(type.equals(Color.class))
		{
			return new ColorTemplate();
		}
		else if(type.equals(Rect.class))
		{
			return new RectTemplate();
		}
		else if(type.isArray())
		{
			return new ListTemplate(type);
		}
		return null;
	}
}
