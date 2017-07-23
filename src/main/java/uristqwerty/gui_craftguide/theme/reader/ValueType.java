package uristqwerty.gui_craftguide.theme.reader;

import java.lang.reflect.Field;

import uristqwerty.gui_craftguide.Color;
import uristqwerty.gui_craftguide.Rect;
import uristqwerty.gui_craftguide.editor.TextureMeta.ListSize;
import uristqwerty.gui_craftguide.editor.TextureMeta.WithUnits;
import uristqwerty.gui_craftguide.texture.AnimationFrame;
import uristqwerty.gui_craftguide.texture.Texture;

public class ValueType
{
	public static ValueTemplate getTemplate(Field field, WithUnits units)
	{
		if(field.getType().isArray() && field.isAnnotationPresent(ListSize.class))
		{
			ListSize size = field.getAnnotation(ListSize.class);
			return new ListTemplate(field.getType(), size.value(), units);
		}

		return getTemplate(field.getType(), units);
	}

	public static ValueTemplate getTemplate(Class<?> type, WithUnits units)
	{
		if(type.equals(Color.class))
		{
			return new ColorTemplate(units);
		}
		else if(type.equals(Rect.class))
		{
			return new RectTemplate(units);
		}
		else if(type.equals(Texture.class))
		{
			return new TextureElement();
		}
		else if(type.equals(AnimationFrame.class))
		{
			return new AnimationFrameElement();
		}
		else if(type.equals(int.class) || type.equals(Integer.class) || type.equals(long.class) || type.equals(Long.class) ||
				type.equals(short.class) || type.equals(Short.class) || type.equals(byte.class) || type.equals(Byte.class))
		{
			return new IntegerElement(units);
		}
		else if(type.equals(float.class) || type.equals(Float.class) || type.equals(double.class) || type.equals(Double.class))
		{
			return new DoubleElement(units);
		}
		else if(type.isArray())
		{
			return new ListTemplate(type, units);
		}

		return null;
	}
}
