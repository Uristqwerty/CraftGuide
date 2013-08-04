package uristqwerty.CraftGuide.client.modloader;

import java.lang.reflect.Field;

import net.minecraft.client.renderer.Tessellator;
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
	public void stopTessellating()
	{
		if(isDrawing == null)
		{
			try
			{
				isDrawing = CommonUtilities.getPrivateField(Tessellator.class, "field_78415_z", "z", "isDrawing");
			}
			catch(NoSuchFieldException e)
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
