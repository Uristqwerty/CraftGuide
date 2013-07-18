package uristqwerty.CraftGuide.client.fml;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.CraftGuideClient;
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

	@Override
	public ITexturePack getSelectedTexturePack()
	{
		return getMinecraftInstance().renderEngine.texturePack.getSelectedTexturePack();
	}

	@Override
	public void stopTessellating()
	{
		if(Tessellator.instance.isDrawing)
		{
			Tessellator.instance.draw();
		}
	}
}
