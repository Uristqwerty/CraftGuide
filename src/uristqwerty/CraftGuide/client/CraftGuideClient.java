package uristqwerty.CraftGuide.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideSide;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.client.ui.GuiRenderer;
import uristqwerty.gui.rendering.RendererBase;
import uristqwerty.gui.theme.ThemeManager;
import cpw.mods.fml.client.registry.KeyBindingRegistry;

public class CraftGuideClient implements CraftGuideSide
{
	@Override
	public void initKeybind()
	{
		KeyBindingRegistry.registerKeyBinding(new CraftGuideKeyHandler());
	}

	@Override
	public void preInit()
	{
		RendererBase.instance = new GuiRenderer();
		Util.instance = new UtilImplementationClient();
		extractResources();
		ThemeManager.instance.reload();
		ThemeManager.currentTheme = ThemeManager.instance.buildTheme("theme_base");
	}

	@Override
	public void reloadRecipes()
	{
		GuiCraftGuide.getInstance().reloadRecipes();
	}

	@Override
	public void openGUI(EntityPlayer player)
	{
    	ModLoader.openGUI(player, GuiCraftGuide.getInstance());
	}

	public static File themeDirectory()
	{
		File configDir = CraftGuide.configDirectory();

		if(configDir == null)
		{
			return null;
		}

		File dir = new File(configDir, "themes");

		if(!dir.exists() && !dir.mkdirs())
		{
			return null;
		}

		return dir;
	}

	private void extractResources()
	{
		File outputBase = themeDirectory();

		if(outputBase == null)
		{
			return;
		}

		try
		{
			InputStream stream = getClass().getResourceAsStream("CraftGuideResources.zip");

			if(stream != null)
			{
				ZipInputStream resources = new ZipInputStream(stream);
				byte[] buffer = new byte[1024 * 16];
				ZipEntry entry;
				while((entry = resources.getNextEntry()) != null)
				{
					File destination = new File(outputBase, entry.getName());

					if(!destination.exists())
					{
						if(entry.isDirectory())
						{
							destination.mkdirs();
						}
						else
						{
							System.out.println("CraftGuide: Extracting '" + entry.getName() + "' to '" + destination.getCanonicalPath() + "'");
							destination.getParentFile().mkdirs();
							destination.createNewFile();
							FileOutputStream output = new FileOutputStream(
									destination);
							int len;

							while((len = resources.read(buffer, 0, buffer.length)) != -1)
							{
								output.write(buffer, 0, len);
							}

							output.flush();
							output.close();
						}
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
