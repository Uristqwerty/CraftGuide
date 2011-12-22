package net.minecraft.src.CraftGuide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class ItemCraftGuide extends Item
{
	public ItemCraftGuide(int i)
	{
		super(i);
		setIconCoord(11, 3);
		setItemName("CraftGuideItem");
	}

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
    	ModLoader.OpenGUI(player, GuiCraftGuide.getInstance());
        return itemstack;
    }
    
    public int getColorFromDamage(int i)
    {
        return 0x9999ff;
    }
}
