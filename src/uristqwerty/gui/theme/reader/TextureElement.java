package uristqwerty.gui.theme.reader;

import org.xml.sax.Attributes;

import uristqwerty.gui.Rect;
import uristqwerty.gui.texture.Texture;
import uristqwerty.gui.texture.TextureClip;
import uristqwerty.gui.theme.Theme;

public class TextureElement implements ElementHandler
{
	private String type;
	private String id;

	private Texture texture = null;
	
	@Override
	public void startElement(Theme theme, String name, Attributes attributes)
	{
		for(int i = 0; i < attributes.getLength(); i++)
		{
			if(attributes.getLocalName(i).equalsIgnoreCase("type"))
			{
				type = attributes.getValue(i);
				
				if(type.equalsIgnoreCase("clip"))
				{
					texture = new TextureClip();
				}
			}
			else if(attributes.getLocalName(i).equalsIgnoreCase("id"))
			{
				id = attributes.getValue(i);
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
		if(name.equalsIgnoreCase("rect"))
		{
			return new RectTemplate();
		}
		else if(name.equalsIgnoreCase("source"))
		{
			return new TextureSourceElement();
		}
		else
		{
			return NullElement.instance;
		}
	}

	@Override
	public void endSubElement(Theme theme, ElementHandler handler, String name)
	{
		if(name.equalsIgnoreCase("rect"))
		{
			RectTemplate rectTemplate = (RectTemplate)handler;
			Rect rect = new Rect(rectTemplate.x, rectTemplate.y, rectTemplate.width, rectTemplate.height);
			
			if(texture instanceof TextureClip)
			{
				TextureClip clip = (TextureClip)texture;
				clip.rect = rect;
			}
		}
		else if(name.equalsIgnoreCase("source"))
		{
			TextureSourceElement source = (TextureSourceElement)handler;
			
			if(texture instanceof TextureClip)
			{
				TextureClip clip = (TextureClip)texture;
				clip.source = source.source;
			}
		}
	}

	@Override
	public void endElement(Theme theme, String name)
	{
		theme.addTexture(id, texture);
	}
}
