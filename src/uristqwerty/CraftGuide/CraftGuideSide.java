package uristqwerty.CraftGuide;

import net.minecraft.entity.player.EntityPlayer;

public interface CraftGuideSide
{
	public void initKeybind();
	public void preInit();
	public void reloadRecipes();
	public void openGUI(EntityPlayer player);
	public void initNetworkChannels();
}
