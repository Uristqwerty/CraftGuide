package net.minecraft.src.CraftGuide.newAPI;

import net.minecraft.src.ItemStack;

public interface NewIRecipeGenerator
{
	public NewIRecipeTemplate createRecipeTemplate(NewItemSlot[] slots);
	public NewIRecipeTemplate createRecipeTemplate(NewItemSlot[] slots, ItemStack craftingType);
	
	public void addRecipe(NewIRecipeTemplate template, ItemStack[] items);
}
