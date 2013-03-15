package uristqwerty.CraftGuide.client.modloader;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.client.texturepacks.TexturePackList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_CraftGuide;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.CraftGuideClient;


public class CraftGuideClient_ModLoader extends CraftGuideClient
{
	private static Field isDrawing = null;

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
				try
				{
					isDrawing = Tessellator.class.getField("z");
				}
				catch(NoSuchFieldException e)
				{
					try
					{
						isDrawing = Tessellator.class.getField("field_78415_z");
					}
					catch(NoSuchFieldException e2)
					{
						isDrawing = Tessellator.class.getField("isDrawing");
					}
				}

				isDrawing.setAccessible(true);
			}
			catch(Exception e)
			{
				CraftGuideLog.log(e);
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
