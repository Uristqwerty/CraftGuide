package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.ItemStack;

import uristqwerty.CraftGuide.WIP_API.SlotType;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemSlotImplementation;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRenderer;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.Util;

public class ItemSlotImplementation implements IItemSlotImplementation
{
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
	public void draw(ItemSlot itemSlot, IRenderer renderer, int x, int y, Object data, boolean isMouseOver)
	{
		ItemStack stack = item(data);
		
		if(stack != null)
		{
			renderer.renderItemStack(x, y, stack);
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
	public boolean contains(ItemSlot itemSlot, Object search, Object data, SlotType type)
	{
		if(type != itemSlot.slotType && (type != SlotType.ANY_SLOT ||
				type == SlotType.DISPLAY_SLOT || type == SlotType.HIDDEN_SLOT)
				|| !(search instanceof ItemStack))
		{
			return false;
		}
		
		if(data == null)
		{
			return search == null;
		}
		else if(data instanceof ItemStack)
		{
			return compare((ItemStack)data, (ItemStack)search);
		}
		else if(data instanceof List)
		{
			for(Object content: (List)data)
			{
				if(content instanceof ItemStack)
				{
					return compare((ItemStack)content, (ItemStack)search);
				}
			}
		}
		
		return false;
	}

	private boolean compare(ItemStack object, ItemStack search)
	{
		return object.itemID == search.itemID && (
				object.getItemDamage() == search.getItemDamage() ||
				object.getItemDamage() == -1 ||
				search.getItemDamage() == -1);
	}
}
