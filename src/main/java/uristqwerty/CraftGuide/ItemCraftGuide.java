package uristqwerty.CraftGuide;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCraftGuide extends Item
{
	public ItemCraftGuide(String iconName)
	{
		setUnlocalizedName("craftguide_item");
		setRegistryName("craftguide_item");
		setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.registerItem(this);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
	{
		CraftGuide.side.openGUI(player);
		return itemstack;
	}
}
