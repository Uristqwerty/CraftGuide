package net.minecraft.src.CraftGuide;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ExtraSlot;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.ShadedRect;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class Recipe implements ICraftGuideRecipe
{
	protected ItemSlot[] slots;
	protected IRenderable[] selection;
	protected ItemStack[] recipe;
	private IRenderable background;
	private IRenderable backgroundSelected;
	private static IRenderable overlayAny = new TexturedRect(
		-1, -1, 18, 18, GuiTexture.getInstance("/gui/CraftGuide.png"), 238, 238);
	
	private int width = 79, height = 58; 
	
	public Recipe(ItemSlot[] slots, ItemStack[] recipe, IRenderable background, IRenderable backgroundSelected)
	{
		this.slots = slots;
		this.recipe = new ItemStack[recipe.length];
		for(int i = 0; i < recipe.length; i++)
		{
			this.recipe[i] = recipe[i];
		}
		
		this.background = background;
		this.backgroundSelected = backgroundSelected;
		this.selection = new IRenderable[slots.length];
		
		for(ItemSlot slot: slots)
		{
			selection[slot.index] = new ShadedRect(slot.x, slot.y, slot.width, slot.height, 0xffffff, 0x80);

			if(this.recipe[slot.index] != null && slot.drawQuantity == false && this.recipe[slot.index].stackSize > 1)
			{
				ItemStack old = this.recipe[slot.index];
				
				this.recipe[slot.index] = new ItemStack(old.itemID, 1, old.getItemDamage());
			}
		}
	}
	
	public ItemStack getItem(int index)
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
				renderer.drawItemStack(recipe[slot.index], x + slot.x, y + slot.y);
				
				if(recipe[slot.index].getItemDamage() == -1)
				{
					overlayAny.render(renderer, x + slot.x, y + slot.y);
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

	public ItemStack getItemUnderMouse(int x, int y)
	{
		ItemSlot slot = getSlotUnderMouse(x, y);
		
		if(slot != null)
		{
			return getItem(slot.index);
		}
		
		return null;
	}

	public IRenderable getSelectionBox(int x, int y)
	{
		ItemSlot slot = getSlotUnderMouse(x, y);
		
		if(slot != null)
		{
			return selection[slot.index];
		}
		
		return null;
	}
	
	public boolean containsItem(ItemStack filter)
	{
		return containsItem(filter, false);
	}
	
	public boolean containsItem(ItemStack filter, boolean exactMatch)
	{
		for(ItemStack item: recipe)
		{
			if(areItemsEqual(item, filter, exactMatch))
			{
				return true;
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
	
	public ItemStack[] getItems()
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
