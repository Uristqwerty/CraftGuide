package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.ItemStack;

import uristqwerty.CraftGuide.WIP_API.SlotType;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemSlotImplementation;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRenderer;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.NamedTexture;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.Util;

public class ItemSlotImplementation implements IItemSlotImplementation
{
	private NamedTexture overlayAny;
	private NamedTexture overlayForge;
	private NamedTexture background;

	public ItemSlotImplementation()
	{
		overlayAny = Util.instance.getTexture("ItemStack-Any");
		overlayForge = Util.instance.getTexture("ItemStack-OreDict");
		background = Util.instance.getTexture("ItemStack-Background");
	}
	
	@Override
	public List<String> getTooltip(ItemSlot itemSlot, Object data)
	{
		ItemStack stack = item(data);
		
		if(stack == null)
		{
			return null;
		}
		else
		{
			return Util.instance.getItemStackText(stack);
		}
	}

	@Override
	public void draw(ItemSlot itemSlot, IRenderer renderer, int recipeX, int recipeY, Object data, boolean isMouseOver)
	{
		int x = recipeX + itemSlot.x;
		int y = recipeY + itemSlot.y;
		ItemStack stack = item(data);
		
		if(itemSlot.drawBackground)
		{
			renderer.renderRect(x - 1, y - 1, 18, 18, background);
		}
		
		if(stack != null)
		{
			
			renderer.renderItemStack(x, y, stack);
			
			if(isMouseOver)
			{
				renderer.renderRect(x, y, 16, 16, 0xff, 0xff, 0xff, 0x80);
			}
			
			if(stack.getItemDamage() == -1)
			{
				renderer.renderRect(x - 1, y - 1, 18, 18, overlayAny);
			}
			
			if(data instanceof List)
			{
				renderer.renderRect(x - 1, y - 1, 18, 18, overlayForge);
			}
		}
	}
	
	private static ItemStack item(Object data)
	{
		if(data == null)
		{
			return null;
		}
		else if(data instanceof ItemStack)
		{
			return (ItemStack)data;
		}
		else if(data instanceof List && ((List)data).size() > 0)
		{
			return item(((List)data).get(0));
		}

		return null;
	}

	@Override
	public boolean matches(ItemSlot itemSlot, IItemFilter search, Object data, SlotType type)
	{
		if(type != itemSlot.slotType && (
				type != SlotType.ANY_SLOT ||
				type == SlotType.DISPLAY_SLOT ||
				type == SlotType.HIDDEN_SLOT))
		{
			return false;
		}
		
		if(data == null || data instanceof ItemStack)
		{
			return search.matches(data);
		}
		else if(data instanceof List)
		{
			for(Object content: (List)data)
			{
				if(search.matches(content))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean isPointInBounds(ItemSlot itemSlot, int x, int y)
	{
		return x >= itemSlot.x
			&& x < itemSlot.x + itemSlot.width
			&& y >= itemSlot.y
			&& y < itemSlot.y + itemSlot.height;
	}

	@Override
	public IItemFilter getClickedFilter(int x, int y, Object object)
	{
		return Util.instance.getCommonFilter(object);
	}
}
