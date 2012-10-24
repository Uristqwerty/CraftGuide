package uristqwerty.gui.theme.reader;

import java.lang.reflect.Field;

import org.xml.sax.Attributes;

import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.gui.Rect;
import uristqwerty.gui.editor.TextureMeta.TextureParameter;
import uristqwerty.gui.texture.Texture;
import uristqwerty.gui.theme.Theme;
import uristqwerty.gui.theme.ThemeManager;

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
				Class<? extends Texture> textureClass = ThemeManager.textureTypes.get(type.toLowerCase());

				if(textureClass != null)
				{
					try
					{
						texture = textureClass.newInstance();
					}
					catch(InstantiationException e)
					{
						CraftGuideLog.log(e);
					}
					catch(IllegalAccessException e)
					{
						CraftGuideLog.log(e);
					}
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
		if(texture != null)
		{
			for(Field field: texture.getClass().getFields())
			{
				if(field.getName().equalsIgnoreCase(name))
				{
					if(field.isAnnotationPresent(TextureParameter.class))
					{
						if(field.getType().equals(Texture.class))
						{
							return new TextureSourceElement();
						}
						else if(field.getType().equals(Rect.class))
						{
							return new RectTemplate();
						}
					}

					break;
				}
			}
		}

		return NullElement.instance;
	}

	@Override
	public void endSubElement(Theme theme, ElementHandler handler, String name)
	{
		if(texture != null)
		{
			for(Field field: texture.getClass().getFields())
			{
				if(field.getName().equalsIgnoreCase(name))
				{
					if(field.isAnnotationPresent(TextureParameter.class))
					{
						try
						{
							if(field.getType().equals(Texture.class) && handler instanceof TextureSourceElement)
							{
								TextureSourceElement source = (TextureSourceElement)handler;
								field.set(texture, source.source);
							}
							else if(field.getType().equals(Rect.class) && handler instanceof RectTemplate)
							{
								RectTemplate rectTemplate = (RectTemplate)handler;
								field.set(texture, new Rect(rectTemplate.x, rectTemplate.y, rectTemplate.width, rectTemplate.height));
							}
						}
						catch(IllegalArgumentException e)
						{
							CraftGuideLog.log(e);
						}
						catch(IllegalAccessException e)
						{
							CraftGuideLog.log(e);
						}
					}

					break;
				}
			}
		}
	}

	@Override
	public void endElement(Theme theme, String name)
	{
		theme.addTexture(id, texture);
	}
}
