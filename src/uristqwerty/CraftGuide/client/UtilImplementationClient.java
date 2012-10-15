package uristqwerty.CraftGuide.client;

import uristqwerty.CraftGuide.UtilImplementationCommon;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.client.ui.NamedTextureObject;
import uristqwerty.gui.texture.DynamicTexture;

public class UtilImplementationClient extends UtilImplementationCommon
{
	private NamedTextureObject textFilter = new NamedTextureObject(DynamicTexture.instance("text-filter"));
	private NamedTextureObject itemStackAny = new NamedTextureObject(DynamicTexture.instance("stack-any"));
	private NamedTextureObject itemStackOreDict = new NamedTextureObject(DynamicTexture.instance("stack-oredict"));
	private NamedTextureObject itemStackBackground = new NamedTextureObject(DynamicTexture.instance("stack-background"));

	@Override
	public NamedTexture getTexture(String identifier)
	{
		if("ItemStack-Any".equalsIgnoreCase(identifier))
		{
			return itemStackAny;
		}
		else if("ItemStack-OreDict".equalsIgnoreCase(identifier))
		{
			return itemStackOreDict;
		}
		else if("ItemStack-Background".equalsIgnoreCase(identifier))
		{
			return itemStackBackground;
		}
		else if("TextFilter".equalsIgnoreCase(identifier))
		{
			return textFilter;
		}

		return null;
	}
}
