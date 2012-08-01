package net.minecraft.src.CraftGuide;

import uristqwerty.CraftGuide.RecipeTemplate;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ExtraSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.OutputSlot;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.IRecipeTemplateResizable;

public class FakeRecipeTemplate implements IRecipeTemplateResizable
{
	private RecipeTemplate actualTemplate;

	public FakeRecipeTemplate(net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot[] slots, ItemStack craftingType,
			TexturedRect texturedRect, TexturedRect texturedRect2)
	{
		actualTemplate = new RecipeTemplate(convertItemSlots(slots), craftingType, texturedRect, texturedRect2);
	}

	private ItemSlot[] convertItemSlots(
			net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot[] oldSlots)
	{
		ItemSlot[] slots = new ItemSlot[oldSlots.length];
		
		for(int i = 0; i < slots.length; i++)
		{
			slots[i] = convertSlot(oldSlots[i]);
		}
		
		return slots;
	}
	
	private ItemSlot convertSlot(
			net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot slot)
	{
		if(slot instanceof net.minecraft.src.CraftGuide.API.ExtraSlot2)
		{
			return new ExtraSlot(slot.x, slot.y, slot.width, slot.height, slot.index,
					((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).displayed)
				.clickable(((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).canClick)
				.filterable(((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).canFilter)
				.showName(((net.minecraft.src.CraftGuide.API.ExtraSlot2)slot).showName);
		}
		else if(slot instanceof net.minecraft.src.CraftGuide.API.ExtraSlot)
		{
			return new ExtraSlot(slot.x, slot.y, slot.width, slot.height, slot.index,
					((net.minecraft.src.CraftGuide.API.ExtraSlot)slot).displayed);
		}
		else if(slot instanceof net.minecraft.src.CraftGuide.API.OutputSlot)
		{
			return new OutputSlot(slot.x, slot.y, slot.width, slot.height, slot.index, slot.drawQuantity);
		}
		else
		{
			return new ItemSlot(slot.x, slot.y, slot.width, slot.height, slot.index, slot.drawQuantity);
		}
	}

	public uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeTemplate getRealTemplate()
	{
		return actualTemplate;
	}

	@Override
	public IRecipeTemplateResizable setSize(int width, int height)
	{
		actualTemplate.setSize(width, height);
		return this;
	}
}
