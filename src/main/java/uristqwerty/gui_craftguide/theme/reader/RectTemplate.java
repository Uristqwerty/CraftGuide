package uristqwerty.gui_craftguide.theme.reader;

import org.xml.sax.Attributes;

import uristqwerty.gui_craftguide.Rect;
import uristqwerty.gui_craftguide.editor.TextureMeta.Unit;
import uristqwerty.gui_craftguide.editor.TextureMeta.WithUnits;
import uristqwerty.gui_craftguide.theme.Theme;

public class RectTemplate implements ValueTemplate
{
	public int x, y, width, height;
	private final WithUnits units;

	public RectTemplate(WithUnits units)
	{
		this.units = units;
	}

	@Override
	public void startElement(Theme theme, String name, Attributes attributes)
	{
		for(int i = 0; i < attributes.getLength(); i++)
		{
			if(attributes.getLocalName(i).equalsIgnoreCase("x"))
			{
				x = num(attributes.getValue(i));
			}
			else if(attributes.getLocalName(i).equalsIgnoreCase("y"))
			{
				y = num(attributes.getValue(i));
			}
			else if(attributes.getLocalName(i).equalsIgnoreCase("width"))
			{
				width = num(attributes.getValue(i));
			}
			else if(attributes.getLocalName(i).equalsIgnoreCase("height"))
			{
				height = num(attributes.getValue(i));
			}
		}
	}

	@Override
	public void characters(Theme theme, char[] chars, int start, int length)
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

	private int num(String value)
	{
		double multiplier = 1;
		if(units != null)
		{
			String lower = value.toLowerCase();
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
				value = value.substring(0, value.length() - longestMatch).trim();
			}
		}
		try
		{
			return (int)(Integer.parseInt(value) * multiplier);
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}

	@Override
	public Class<?> valueType()
	{
		return Rect.class;
	}

	@Override
	public Object getValue()
	{
		return new Rect(x, y, width, height);
	}
}
