package net.minecraft.src.CraftGuide.newAPI;

import net.minecraft.src.ItemStack;

public interface IRecipeGenerator
{
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType);
	
	public void addRecipe(IRecipeTemplate template, ItemStack[] items);
}
