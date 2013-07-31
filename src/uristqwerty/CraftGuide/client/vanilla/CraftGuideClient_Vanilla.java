package uristqwerty.CraftGuide.client.vanilla;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.src.CraftGuide_Vanilla;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TexturePackList;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.CraftGuideClient;


public class CraftGuideClient_Vanilla extends CraftGuideClient
{
	private static Field isDrawing = null;

	@Override
	public void initKeybind()
	{
		((CraftGuide_Vanilla)CraftGuide.loaderSide).initKeybind();
	}

	@Override
	public void openGUI(EntityPlayer player)
	{
		Minecraft.getMinecraft().displayGuiScreen(GuiCraftGuide.getInstance());
	}

	@Override
	public Minecraft getMinecraftInstance()
	{
		return Minecraft.getMinecraft();
	}

	@Override
	public ITexturePack getSelectedTexturePack()
	{
		RenderEngine renderEngine = getMinecraftInstance().renderEngine;

		try
		{
			TexturePackList texturePackList = (TexturePackList)CommonUtilities.getPrivateValue(RenderEngine.class, renderEngine, "g", "texturePack", "field_78366_k");
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

	@Override
	public void stopTessellating()
	{
		if(isDrawing == null)
		{
			try
			{
				isDrawing = CommonUtilities.getPrivateField(Tessellator.class, "z", "isDrawing", "field_78415_z");
			}
			catch(NoSuchFieldException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			if(isDrawing != null && isDrawing.getBoolean(Tessellator.instance))
			{
				Tessellator.instance.draw();
			}
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e);
		}
	}
}
