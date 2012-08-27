package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.List;

import uristqwerty.CraftGuide.WIP_API.SlotType;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRenderer;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ISlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.Util;
import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.ItemStack;

public class Recipe implements ICraftGuideRecipe
{
	protected ISlot[] slots;
	protected IRenderable[] selection;
	protected Object[] recipe;
	private IRenderable background;
	private IRenderable backgroundSelected;
	
	private int width = 79, height = 58; 
	
	public Recipe(ISlot[] slots, Object[] items, IRenderable background, IRenderable backgroundSelected)
	{
		this.slots = slots;
		this.recipe = new Object[items.length];
		for(int i = 0; i < items.length; i++)
		{
			this.recipe[i] = items[i];
		}
		
		this.background = background;
		this.backgroundSelected = backgroundSelected;
		
		for(int i = 0; i < slots.length; i++)
		{
			if(this.recipe[i] != null && slots[i] instanceof ItemSlot && !((ItemSlot)slots[i]).drawQuantity && displayStack(i) != null && displayStack(i).stackSize > 1)
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
	public void draw(IRenderer renderer, int x, int y, boolean mouseOverRecipe, int mouseX, int mouseY)
	{
		if(mouseOverRecipe)
		{
			backgroundSelected.render((GuiRenderer)renderer, x, y);
		}
		else
		{
			background.render((GuiRenderer)renderer, x, y);
		}
		
		for(int i = 0; i < slots.length; i++)
		{
			if(mouseOverRecipe)
			{
				boolean mouseOverSlot = slots[i].isPointInBounds(mouseX, mouseY, recipe, i);
				slots[i].draw(renderer, x, y, recipe, i, mouseOverSlot);
			}
			else
			{
				slots[i].draw(renderer, x, y, recipe, i, false);
			}
		}
	}
	
	public int getSlotIndexUnderMouse(int x, int y)
	{
		for(int i = 0; i < slots.length; i++)
		{
			if(slots[i].isPointInBounds(x, y, recipe, i))
			{
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public boolean containsItem(IItemFilter filter)
	{
		for(int i = 0; i < slots.length; i++)
		{
			if(slots[i].matches(filter, recipe, i, SlotType.ANY_SLOT))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean containsItem(ItemStack stack)
	{
		IItemFilter filter = Util.instance.getCommonFilter(stack);
		
		return containsItem(filter);
	}

	@Override
	public Object[] getItems()
	{
		return recipe;
	}
	
	@Override
	public int width()
	{
		return width;
	}

	@Override
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

	@Override
	public List<String> getItemText(int x, int y)
	{
		int slot = getSlotIndexUnderMouse(x, y);
		
		if(slot == -1 || slots[slot] == null)
		{
			return null;
		}
		else
		{
			return slots[slot].getTooltip(x, y, recipe, slot);
		}
	}

	@Override
	public IItemFilter getRecipeClickedResult(int x, int y)
	{
		int slot = getSlotIndexUnderMouse(x, y);
		
		if(slot == -1 || slots[slot] == null)
		{
			return null;
		}
		else
		{
			return slots[slot].getClickedFilter(x, y, recipe, slot);
		}
	}
}
