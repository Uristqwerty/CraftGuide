package uristqwerty.gui_craftguide.theme.reader;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.xml.sax.Attributes;

import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.gui_craftguide.editor.TextureMeta.GeneratedFrom;
import uristqwerty.gui_craftguide.editor.TextureMeta.ListElementType;
import uristqwerty.gui_craftguide.editor.TextureMeta.TextureParameter;
import uristqwerty.gui_craftguide.editor.TextureMeta.WithUnits;
import uristqwerty.gui_craftguide.texture.DynamicTexture;
import uristqwerty.gui_craftguide.texture.Texture;
import uristqwerty.gui_craftguide.theme.Theme;
import uristqwerty.gui_craftguide.theme.ThemeManager;

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
					catch(InstantiationException | IllegalAccessException e)
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
					WithUnits units = field.getAnnotation(WithUnits.class);

					ValueTemplate template = ValueType.getTemplate(field, units);

					if(template != null)
					{
						return template;
					}
				}
				else if(field.isAnnotationPresent(GeneratedFrom.class))
				{
					GeneratedFrom from = field.getAnnotation(GeneratedFrom.class);
					Class<?> type = field.getType();
					if(ArrayList.class.isAssignableFrom(type))
					{
						type = field.getAnnotation(ListElementType.class).value();
					}

					for(Class<?> src: from.value())
					{
						throw new RuntimeException("Not implemented yet; ideas of a better system might replace this feature entirely" + src);
					}
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
							if(handler instanceof ValueTemplate)
							{
								ValueTemplate template = (ValueTemplate)handler;

								if(field.getType().isAssignableFrom(template.valueType()))
								{
									field.set(texture, template.getValue());
								}
								else if(handler instanceof PrimitiveValueTemplate)
								{
									((PrimitiveValueTemplate)handler).setField(field, texture);
								}
							}
						}
						catch(IllegalArgumentException | IllegalAccessException e)
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
	public Class<?> valueType()
	{
		return Texture.class;
	}

	@Override
	public Object getValue()
	{
		return texture;
	}
}
