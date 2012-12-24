package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.item.ItemStack;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 */

@Deprecated
public interface IItemFilter
{
	public boolean matches(Object item);

	public ItemStack getDisplayStack();
	public Object getItem();
}
