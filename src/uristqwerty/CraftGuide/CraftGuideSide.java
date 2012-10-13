package uristqwerty.CraftGuide;

import net.minecraft.src.EntityPlayer;

public interface CraftGuideSide
{
	public void initKeybind();
	public void preInit();
	public void reloadRecipes();
	public void openGUI(EntityPlayer player);
}
