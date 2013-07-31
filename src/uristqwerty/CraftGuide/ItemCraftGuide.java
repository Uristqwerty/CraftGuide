package uristqwerty.CraftGuide;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemCraftGuide extends Item
{
	public ItemCraftGuide(int i)
	{
		super(i);
		setUnlocalizedName("craftguide_item");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
		CraftGuide.side.openGUI(player);
        return itemstack;
    }

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		return 0x9999ff;
	}
}
