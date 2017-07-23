package uristqwerty.gui_craftguide.theme.reader;

import org.xml.sax.Attributes;

import uristqwerty.gui_craftguide.editor.TextureMeta.Unit;
import uristqwerty.gui_craftguide.editor.TextureMeta.WithUnits;
import uristqwerty.gui_craftguide.texture.AnimationFrame;
import uristqwerty.gui_craftguide.texture.Texture;
import uristqwerty.gui_craftguide.theme.Theme;

public class AnimationFrameElement implements ValueTemplate
{
	private AnimationFrame frame;
	private static final WithUnits units;

	static
	{
		try
		{
			units = AnimationFrame.class.getField("duration").getAnnotation(WithUnits.class);
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Can't handle this case");
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
			throw new RuntimeException("This really shouldn't be possible");
		}
	}

	@Override
	public void startElement(Theme theme, String name, Attributes attributes)
	{
		for(int i = 0; i < attributes.getLength(); i++)
		{
			if(attributes.getLocalName(i).equalsIgnoreCase("duration"))
			{
				frame = new AnimationFrame();
				frame.duration = num(attributes.getValue(i));
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
		if(frame != null && name.equalsIgnoreCase("texture"))
		{
			return new TextureElement();
		}

		return NullElement.instance;
	}

	@Override
	public void endElement(Theme theme, String name)
	{
	}

	@Override
	public void endSubElement(Theme theme, ElementHandler handler, String name)
	{
		if(frame != null && name.equalsIgnoreCase("texture"))
		{
			frame.source = (Texture)((TextureElement)handler).getValue();
		}
	}

	@Override
	public Class<?> valueType()
	{
		return AnimationFrame.class;
	}

	@Override
	public Object getValue()
	{
		return frame;
	}

	private double num(String str)
	{
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
			return Double.parseDouble(str) * multiplier;
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}
}
