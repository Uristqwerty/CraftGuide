package net.minecraft.src.CraftGuide;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class RecipeTemplate implements IRecipeTemplate
{
	private ItemSlot[] slots;
	private IRenderable background, backgroundSelected;
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
		return new Recipe(slots, items, background, backgroundSelected);
	}
}
