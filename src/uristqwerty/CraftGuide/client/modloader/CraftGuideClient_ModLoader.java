package uristqwerty.CraftGuide.client.modloader;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.mod_CraftGuide;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.CraftGuideClient;


public class CraftGuideClient_ModLoader extends CraftGuideClient
{

	@Override
	public void initKeybind()
	{
		((mod_CraftGuide)CraftGuide.loaderSide).initKeybind();
	}

	@Override
	public void openGUI(EntityPlayer player)
	{
		ModLoader.openGUI(player, GuiCraftGuide.getInstance());
	}

	@Override
	public Minecraft getMinecraftInstance()
	{
		return ModLoader.getMinecraftInstance();
	}

	@Override
	public ITexturePack getSelectedTexturePack()
	{
		RenderEngine renderEngine = getMinecraftInstance().renderEngine;

		try
		{
			TexturePackList texturePackList = (TexturePackList)CommonUtilities.getPrivateValue(RenderEngine.class, renderEngine, "k", "texturePack");
			return texturePackList.getSelectedTexturePack();
		}
		catch(SecurityException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}

		return null;
	}
}
