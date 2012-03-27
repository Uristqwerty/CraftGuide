package net.minecraft.src.CraftGuide;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.API.IRecipeTemplateResizable;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class RecipeTemplate implements IRecipeTemplateResizable
{
	private ItemSlot[] slots;
	private TexturedRect background, backgroundSelected;
	private int width = 79, height = 58; 
	
	ItemStack craftingType;
	
	public RecipeTemplate(ItemSlot[] slots, ItemStack craftingType, TexturedRect background, TexturedRect backgroundSelected)
	{
		this.slots = slots;
		this.background = background;
		this.backgroundSelected = backgroundSelected;
		this.craftingType = craftingType;
	}

	public ICraftGuideRecipe generate(ItemStack[] items)
	{
		return new Recipe(slots, items, background, backgroundSelected).setSize(width, height);
	}

	@Override
	public IRecipeTemplateResizable setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		if(background != null)
		{
			background.setSize(width, height);
		}
		
		if(backgroundSelected != null)
		{
			backgroundSelected.setSize(width, height);
		}
		
		return this;
	}
}
