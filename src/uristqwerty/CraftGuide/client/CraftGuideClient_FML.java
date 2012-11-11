package uristqwerty.CraftGuide.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import uristqwerty.CraftGuide.GuiCraftGuide;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;

public class CraftGuideClient_FML extends CraftGuideClient
{
	@Override
	public void initKeybind()
	{
		KeyBindingRegistry.registerKeyBinding(new CraftGuideKeyHandler());
	}

	@Override
	public Minecraft getMinecraftInstance()
	{
		return FMLClientHandler.instance().getClient();
	}

	@Override
	public void openGUI(EntityPlayer player)
	{
		FMLClientHandler.instance().displayGuiScreen(player, GuiCraftGuide.getInstance());
	}
}
