package uristqwerty.gui_craftguide.theme.reader;

import java.lang.reflect.Field;

import org.xml.sax.Attributes;

import uristqwerty.gui_craftguide.theme.Theme;

public abstract class PrimitiveValueTemplate implements ValueTemplate
{
	@Override
	public void startElement(Theme theme, String name, Attributes attributes)
	{
	}

	@Override
	public ElementHandler getSubElement(String name, Attributes attributes)
	{
		return NullElement.instance;
	}

	@Override
	public void endElement(Theme theme, String name)
	{
	}

	@Override
	public void endSubElement(Theme theme, ElementHandler handler, String name)
	{
	}

	public abstract boolean setField(Field field, Object object) throws IllegalArgumentException, IllegalAccessException;
}
