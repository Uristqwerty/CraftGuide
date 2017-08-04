package uristqwerty.CraftGuide;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCraftGuide extends Item
{
	public ItemCraftGuide(String iconName)
	{
		setUnlocalizedName("craftguide_item");
		setRegistryName("craftguide_item");
		setCreativeTab(CreativeTabs.MISC);
		GameRegistry.register(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		CraftGuide.side.openGUI(playerIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
}
