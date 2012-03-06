package net.minecraft.src.CraftGuide.newAPI;

import net.minecraft.src.ItemStack;

public interface NewICraftGuideRecipe
{
	public ItemStack getItem(int index);
	public NewItemSlot getSlotUnderMouse(int x, int y);
	
	public boolean containsItem(ItemStack filter);
	public boolean containsItem(ItemStack filter, boolean exactMatch);
	public ItemStack[] getItems();
}
