package net.minecraft.src.CraftGuide.API;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;

public interface IRecipeGenerator
{
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, String backgroundTexture, int backgroundX, int backgroundY, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, String backgroundTexture, int backgroundX, int backgroundY, String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, int backgroundSelectedX, int backgroundSelectedY);
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType, String backgroundTexture, int backgroundX, int backgroundY, String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY);

	public void addRecipe(IRecipeTemplate template, ItemStack[] items);
}
