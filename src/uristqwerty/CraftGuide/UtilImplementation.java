package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.NamedTexture;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.Util;
import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.NamedTextureObject;
import uristqwerty.gui.minecraft.Image;
import uristqwerty.gui.texture.SubTexture;

public class UtilImplementation extends Util
{
	private NamedTextureObject itemStackAny = new NamedTextureObject(new SubTexture(Image.getImage("/gui/CraftGuide.png"), 238, 238, 18, 18));
	private NamedTextureObject itemStackOreDict = new NamedTextureObject(new SubTexture(Image.getImage("/gui/CraftGuide.png"), 238, 181, 18, 18));
	private NamedTextureObject itemStackBackground = new NamedTextureObject(new SubTexture(Image.getImage("/gui/CraftGuide.png"), 238, 219, 18, 18));
	
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
		
		return null;
	}

	@Override
	public IItemFilter getCommonFilter(Object stack)
	{
		if(stack == null)
		{
			return null;
		}
		else if(stack instanceof ItemStack)
		{
			return new SingleItemFilter((ItemStack)stack);
		}
		else if(stack instanceof List)
		{
			return new MultipleItemFilter((List)stack);
		}
		else if(stack instanceof String)
		{
			return new StringItemFilter((String)stack);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public List<String> getItemStackText(ItemStack stack)
	{
		if(stack.getItem() == null)
		{
			List<String> text = new ArrayList<String>(1);
			text.add("\247" + Integer.toHexString(15) + "Error: Item #" + Integer.toString(stack.itemID) + " does not exist");
			return text;
		}
		
		try
		{
			List list = stack.getItemNameandInformation();
			
			if(stack.getItemDamage() == -1 && (list.size() < 1 || (list.size() == 1 && (list.get(0) == null || (list.get(0) instanceof String && ((String)list.get(0)).isEmpty())))))
			{
				list = GuiRenderer.fixedItemStack(stack).getItemNameandInformation();
			}
			
			List<String> text = new ArrayList<String>(list.size());
			boolean first = true;
			
			for(Object o: list)
			{
				if(o instanceof String)
				{
					if(first)
					{
						text.add("\u00a7" + Integer.toHexString(stack.getRarity().rarityColor) + (String)o);
						
						if(CraftGuide.alwaysShowID)
						{
							text.add("\u00a77" + "ID: " + stack.itemID + "; data: " + stack.getItemDamage());
						}
						
						first = false;
					}
					else
					{
						text.add("\u00a77" + (String)o);
					}
				}
			}
			
			return text;
		}
		catch(Exception e)
		{
			CraftGuideLog.log(e);
			
			List<String> text = new ArrayList<String>(1);
			text.add("\247" + Integer.toHexString(15) + "Item #" + Integer.toString(stack.itemID) + " data " + Integer.toString(stack.getItemDamage()));
			return text;
		}
	}
}
