package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import net.minecraft.src.ItemStack;

public abstract class Util
{
	public static Util instance;
	
	public abstract List<String> getItemStackText(ItemStack stack);
}
