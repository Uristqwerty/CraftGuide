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
		
		for(ItemSlot slot: slots)
		{
			selection[slot.index] = new ShadedRect(slot.x, slot.y, slot.width, slot.height, 0xffffff, 0x80);

			if(this.recipe[slot.index] != null && slot.drawQuantity == false && displayStack(slot.index) != null && displayStack(slot.index).stackSize > 1)
			{
				ItemStack old = displayStack(slot.index);
				
				this.recipe[slot.index] = new ItemStack(old.itemID, 1, old.getItemDamage());
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
		
		for(ItemSlot slot: slots)
		{
			if(slot instanceof ExtraSlot)
			{
				renderer.drawItemStack(((ExtraSlot)slot).displayed, x + slot.x, y + slot.y);
				continue;
			}
			
			if(recipe[slot.index] != null)
			{
				renderer.drawItemStack(displayStack(slot.index), x + slot.x, y + slot.y);
				
				if(displayStack(slot.index).getItemDamage() == -1)
				{
					overlayAny.render(renderer, x + slot.x, y + slot.y);
				}
				
				if(recipe[slot.index] instanceof ArrayList)
				{
					overlayForge.render(renderer, x + slot.x, y + slot.y);
				}
			}
		}
	}
	
	@Override
	public ItemSlot getSlotUnderMouse(int x, int y)
	{
		for(ItemSlot slot: slots)
		{
			if(y >= slot.y && y < slot.y + slot.height
			&& x >= slot.x && x < slot.x + slot.width)
			{
				return slot;
			}
		}
		
		return null;
	}
	
	public ItemSlot getSlotUnderMouse(int x, int y, SlotReason reason)
	{
		ItemSlot slot = getSlotUnderMouse(x, y);
		
		if(slot instanceof ExtraSlot)
		{
			if(reason == SlotReason.REASON_CLICK && !((ExtraSlot)slot).canClick
			|| reason == SlotReason.REASON_NAME && !((ExtraSlot)slot).showName
			|| reason == SlotReason.REASON_SEARCH && !((ExtraSlot)slot).canFilter)
			{
				return null;
			}
		}
		else if(slot instanceof ExtraSlot)
		{
			if(reason != SlotReason.REASON_OTHER)
			{
				return null;
			}
		}
		
		return slot;
	}

	public ItemStack getItemUnderMouse(int x, int y)
	{
		return getItemStackUnderMouse(x, y, SlotReason.REASON_OTHER);
	}

	public ItemStack getItemStackUnderMouse(int x, int y, SlotReason reason)
	{
		ItemSlot slot = getSlotUnderMouse(x, y, reason);
		
		if(slot == null)
		{
			return null;
		}
		else if(slot instanceof ExtraSlot)
		{
			return ((ExtraSlot)slot).displayed;
		}

		return getItemStack(slot.index);
	}

	public Object getItemUnderMouse(int x, int y, SlotReason reason)
	{
		ItemSlot slot = getSlotUnderMouse(x, y, reason);
		
		if(slot == null)
		{
			return null;
		}
		else if(slot instanceof ExtraSlot)
		{
			return ((ExtraSlot)slot).displayed;
		}

		return getItem(slot.index);
	}

	public IRenderable getSelectionBox(int x, int y)
	{
		ItemSlot slot = getSlotUnderMouse(x, y, SlotReason.REASON_CLICK);
		
		if(slot != null)
		{
			return selection[slot.index];
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
