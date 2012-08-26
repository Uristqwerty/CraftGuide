package uristqwerty.CraftGuide;

import java.util.ArrayList;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.ExtraSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.ShadedRect;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.minecraft.Image;
import net.minecraft.src.ItemStack;

public class Recipe implements ICraftGuideRecipe
{
	protected ItemSlot[] slots;
	protected IRenderable[] selection;
	protected Object[] recipe;
	private IRenderable background;
	private IRenderable backgroundSelected;
	
	private static IRenderable overlayAny = new TexturedRect(
		-1, -1, 18, 18, Image.getImage("/gui/CraftGuide.png"), 238, 238);
	private static IRenderable overlayForge = new TexturedRect(
			-1, -1, 18, 18, Image.getImage("/gui/CraftGuide.png"), 238, 181);
	
	private int width = 79, height = 58; 
	
	public enum SlotReason
	{
		REASON_CLICK,
		REASON_NAME,
		REASON_SEARCH,
		REASON_OTHER,
	}
	
	public Recipe(ItemSlot[] slots, Object[] items, IRenderable background, IRenderable backgroundSelected)
	{
		this.slots = slots;
		this.recipe = new Object[items.length];
		for(int i = 0; i < items.length; i++)
		{
			this.recipe[i] = items[i];
		}
		
		this.background = background;
		this.backgroundSelected = backgroundSelected;
		this.selection = new IRenderable[slots.length];
		
		for(int i = 0; i < slots.length; i++)
		{
			ItemSlot slot = slots[i];
			selection[i] = new ShadedRect(slot.x, slot.y, slot.width, slot.height, 0xffffff, 0x80);

			if(this.recipe[i] != null && slot.drawQuantity == false && displayStack(i) != null && displayStack(i).stackSize > 1)
			{
				ItemStack old = displayStack(i);
				
				this.recipe[i] = new ItemStack(old.itemID, 1, old.getItemDamage());
			}
		}
	}
	
	private ItemStack displayStack(int index)
	{
		if(recipe[index] == null)
		{
			return null;
		}
		else if(recipe[index] instanceof ItemStack)
		{
			return (ItemStack)recipe[index];
		}
		else if(recipe[index] instanceof ArrayList)
		{
			if(((ArrayList)recipe[index]).size() < 1)
			{
				return null;
			}
			else
			{
				return (ItemStack)((ArrayList)recipe[index]).get(0);
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getItemStack(int index)
	{
		if(index >= 0 && index < slots.length)
		{
			return displayStack(index);
		}
		
		return null;
	}

	@Override
	public Object getItem(int index)
	{
		if(index >= 0 && index < slots.length)
		{
			return recipe[index];
		}
		
		return null;
	}
	
	public void draw(GuiRenderer renderer, int x, int y, boolean selected)
	{
		if(selected)
		{
			backgroundSelected.render(renderer, x, y);
		}
		else
		{
			background.render(renderer, x, y);
		}
		
		for(int i = 0; i < slots.length; i++)
		{
			ItemSlot slot = slots[i];
			
			if(slot instanceof ExtraSlot)
			{
				renderer.drawItemStack(((ExtraSlot)slot).displayed, x + slot.x, y + slot.y);
				continue;
			}
			
			if(displayStack(i) != null)
			{
				renderer.drawItemStack(displayStack(i), x + slot.x, y + slot.y);
				
				if(displayStack(i).getItemDamage() == -1)
				{
					overlayAny.render(renderer, x + slot.x, y + slot.y);
				}
				
				if(recipe[i] instanceof ArrayList)
				{
					overlayForge.render(renderer, x + slot.x, y + slot.y);
				}
			}
		}
	}
	
	public int getSlotIndexUnderMouse(int x, int y)
	{
		for(int i = 0; i < slots.length; i++)
		{
			ItemSlot slot = slots[i];
			if(slot != null && y >= slot.y && y < slot.y + slot.height
							&& x >= slot.x && x < slot.x + slot.width)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	@Override
	public ItemSlot getSlotUnderMouse(int x, int y)
	{
		int index = getSlotIndexUnderMouse(x, y);
		
		if(index != -1)
		{
			return slots[index];
		}
		
		return null;
	}

	private int getSlotIndexUnderMouse(int x, int y, SlotReason reason)
	{
		int index = getSlotIndexUnderMouse(x, y);
		
		if(index == -1)
		{
			return -1;
		}
		
		ItemSlot slot = slots[index];
		
		if(slot instanceof ExtraSlot)
		{
			if(reason == SlotReason.REASON_CLICK && !((ExtraSlot)slot).canClick
			|| reason == SlotReason.REASON_NAME && !((ExtraSlot)slot).showName
			|| reason == SlotReason.REASON_SEARCH && !((ExtraSlot)slot).canFilter)
			{
				return -1;
			}
		}
		else if(slot instanceof ExtraSlot)
		{
			if(reason != SlotReason.REASON_OTHER)
			{
				return -1;
			}
		}
		
		return index;
	}

	public ItemStack getItemUnderMouse(int x, int y)
	{
		return getItemStackUnderMouse(x, y, SlotReason.REASON_OTHER);
	}

	public ItemStack getItemStackUnderMouse(int x, int y, SlotReason reason)
	{
		int index = getSlotIndexUnderMouse(x, y, reason);
		
		if(index == -1 || slots[index] == null)
		{
			return null;
		}
		else if(slots[index] instanceof ExtraSlot)
		{
			return ((ExtraSlot)slots[index]).displayed;
		}

		return getItemStack(index);
	}

	public Object getItemUnderMouse(int x, int y, SlotReason reason)
	{
		int index = getSlotIndexUnderMouse(x, y, reason);
		
		if(index == -1 || slots[index] == null)
		{
			return null;
		}
		else if(slots[index] instanceof ExtraSlot)
		{
			return ((ExtraSlot)slots[index]).displayed;
		}

		return getItem(index);
	}

	public IRenderable getSelectionBox(int x, int y)
	{
		int index = getSlotIndexUnderMouse(x, y, SlotReason.REASON_CLICK);
		ItemSlot slot = null;
		
		if(index != -1)
		{
			slot = slots[index];
		}
		
		if(slot != null)
		{
			return selection[index];
		}
		
		return null;
	}

	@Override
	public boolean containsItem(IItemFilter filter)
	{
		for(Object item: recipe)
		{
			if(filter.matches(item))
			{
				return true;
			}
		}
		
		for(ItemSlot slot: slots)
		{
			if(slot instanceof ExtraSlot && ((ExtraSlot)slot).canFilter)
			{
				if(filter.matches(((ExtraSlot)slot).displayed))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean containsItem(ItemStack filter)
	{
		return containsItem(filter, false);
	}

	@Override
	public boolean containsItem(ItemStack filter, boolean exactMatch)
	{
		for(Object item: recipe)
		{
			if(item instanceof ItemStack)
			{
				if(areItemsEqual((ItemStack)item, filter, exactMatch))
				{
					return true;
				}
			}
			else if(item instanceof ArrayList)
			{
				for(ItemStack stack: (ArrayList<ItemStack>)item)
				{
					if(areItemsEqual(stack, filter, exactMatch))
					{
						return true;
					}
				}
			}
		}
		
		for(ItemSlot slot: slots)
		{
			if(slot instanceof ExtraSlot && ((ExtraSlot)slot).canFilter)
			{
				if(areItemsEqual(((ExtraSlot)slot).displayed, filter, exactMatch))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean areItemsEqual(ItemStack first, ItemStack second, boolean exactMatch)
	{
		if(first == null)
		{
			return (second == null);
		}
		else if(exactMatch)
		{
			return first.isItemEqual(second);
		}
		else
		{
			return areItemsEqual(first, second);
		}
	}
	
	private boolean areItemsEqual(ItemStack first, ItemStack second)
	{
		return first != null
			&& second != null
			&& first.itemID == second.itemID
			&& (
				first.getItemDamage() == -1 ||
				second.getItemDamage() == -1 ||
				first.getItemDamage() == second.getItemDamage()
			);
	}

	@Override
	public Object[] getItems()
	{
		return recipe;
	}
	
	public int width()
	{
		return width;
	}
	
	public int height()
	{
		return height;
	}

	public Recipe setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		return this;
	}
}
