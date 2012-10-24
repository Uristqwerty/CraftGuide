package uristqwerty.gui.theme.reader;

import java.lang.reflect.Field;

import org.xml.sax.Attributes;

import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.gui.Color;
import uristqwerty.gui.Rect;
import uristqwerty.gui.editor.TextureMeta.ListSize;
import uristqwerty.gui.editor.TextureMeta.TextureParameter;
import uristqwerty.gui.texture.DynamicTexture;
import uristqwerty.gui.texture.Texture;
import uristqwerty.gui.theme.Theme;
import uristqwerty.gui.theme.ThemeManager;

public class TextureElement implements ValueTemplate
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
			else if(attributes.getLocalName(i).equalsIgnoreCase("sourceid"))
			{
				texture = DynamicTexture.instance(attributes.getValue(i));
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
						else if(field.getType().equals(Color.class))
						{
							return new ColorTemplate();
						}
						else if(field.getType().equals(Texture[].class))
						{
							ListSize size = field.getAnnotation(ListSize.class);

							if(size == null)
							{
								return new ListTemplate(Texture.class);
							}
							else
							{
								return new ListTemplate(Texture.class, size.value());
							}
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
							else if(field.getType().equals(Color.class) && handler instanceof ColorTemplate)
							{
								ColorTemplate colorTemplate = (ColorTemplate)handler;
								field.set(texture, new Color(colorTemplate.red, colorTemplate.green, colorTemplate.blue, colorTemplate.alpha));
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
		if(id != null)
		{
			theme.addTexture(id, texture);
		}
	}

	@Override
	public Class valueType()
	{
		return Texture.class;
	}

	@Override
	public Object getValue()
	{
		return texture;
	}
}
