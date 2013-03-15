package uristqwerty.CraftGuide;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
