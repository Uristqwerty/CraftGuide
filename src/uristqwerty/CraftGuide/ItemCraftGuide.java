package uristqwerty.CraftGuide;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemCraftGuide extends Item
{

	public ItemCraftGuide(int i)
	{
		super(i);
		setIconCoord(11, 3);
		setItemName("CraftGuideItem");
	}

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
		CraftGuide.side.openGUI(player);
        return itemstack;
    }
	
	

	@Override
	public int func_82790_a(ItemStack stack, int i)
	{
		return 0x9999ff;
	}
}
