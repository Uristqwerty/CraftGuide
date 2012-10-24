package uristqwerty.CraftGuide.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import uristqwerty.gui.texture.BlankTexture;
import uristqwerty.gui.texture.BorderedTexture;
import uristqwerty.gui.texture.MultipleTextures;
import uristqwerty.gui.texture.SolidColorTexture;
import uristqwerty.gui.texture.SubTexture;
import uristqwerty.gui.texture.TextureClip;
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

		ThemeManager.addTextureType(SolidColorTexture.class);
		ThemeManager.addTextureType(MultipleTextures.class);
		ThemeManager.addTextureType(BorderedTexture.class);
		ThemeManager.addTextureType(BlankTexture.class);
		ThemeManager.addTextureType(TextureClip.class);
		ThemeManager.addTextureType(SubTexture.class);
	}

	private String readThemeChoice()
	{
		File dir = themeDirectory();

		if(dir == null)
		{
			return "base_texpack";
		}

		File file = new File(dir, "currentTheme.txt");

		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			if(file.canWrite())
			{
				try
				{
					FileWriter writer = new FileWriter(file);
					writer.write("base_texpack");
					writer.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if(file.canRead())
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				reader.close();
				return line;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		return "base_texpack";
	}

	@Override
	public void reloadRecipes()
	{
		GuiCraftGuide.getInstance().reloadRecipes();
	}

	@Override
	public void openGUI(EntityPlayer player)
	{
		ThemeManager.instance.reload();

		ThemeManager.currentTheme = ThemeManager.instance.buildTheme(readThemeChoice());

		if(ThemeManager.currentTheme == null)
		{
			ThemeManager.currentTheme = ThemeManager.instance.buildTheme("theme_base");
		}

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
			InputStream stream = CraftGuide.class.getResourceAsStream("CraftGuideResources.zip");

			if(stream != null)
			{
				ZipInputStream resources = new ZipInputStream(stream);
				byte[] buffer = new byte[1024 * 16];
				ZipEntry entry;
				while((entry = resources.getNextEntry()) != null)
				{
					File destination = new File(outputBase, entry.getName());

					if(entry.isDirectory())
					{
						destination.mkdirs();
					}
					else
					{
						System.out.println("CraftGuide: Extracting '" + entry.getName() + "' to '" + destination.getCanonicalPath() + "'");
						destination.getParentFile().mkdirs();
						destination.createNewFile();
						FileOutputStream output = new FileOutputStream(destination);
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
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
