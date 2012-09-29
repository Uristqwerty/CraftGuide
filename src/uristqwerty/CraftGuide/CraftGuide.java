package uristqwerty.CraftGuide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.gui.rendering.RendererBase;
import uristqwerty.gui.theme.Theme;
import uristqwerty.gui.theme.ThemeManager;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

@Mod(modid = "CraftGuide_forge", name = "CraftGuide", version = "1.5.2")
public class CraftGuide
{
	public static ItemCraftGuide itemCraftGuide;
	private static Properties config = new Properties();

	public static int resizeRate;
	public static int mouseWheelScrollRate;
	public static boolean pauseWhileOpen = true;
	public static boolean gridPacking = true;
	public static boolean alwaysShowID = false;
	public static boolean textSearchRequiresShift;

	private int itemCraftGuideID = 23361;
	
	public static Theme currentTheme;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		CraftGuideLog.init(new File(configDirectory(), "CraftGuide.log"));
		RendererBase.instance = new GuiRenderer();
		Util.instance = new UtilImplementation();
		ItemSlot.implementation = new ItemSlotImplementationImplementation();
		
		loadProperties();
		initKeybind();
		extractResources();
		
		ThemeManager.instance.reload();
		currentTheme = ThemeManager.instance.buildTheme("theme_base");
	}

	@Init
	public void init(FMLInitializationEvent event)
	{
		addItems();

		try
		{
			Class.forName("uristqwerty.CraftGuide.DefaultRecipeProvider").newInstance();
			Class.forName("uristqwerty.CraftGuide.BrewingRecipes").newInstance();
		}
		catch(InstantiationException e1)
		{
			e1.printStackTrace();
		}
		catch(IllegalAccessException e1)
		{
			e1.printStackTrace();
		}
		catch(ClassNotFoundException e1)
		{
			e1.printStackTrace();
		}

		if(ModLoader.isModLoaded("mod_RedPowerCore"))
		{
			try
			{
				System.out.println("Trying to load RP2Recipes...");
				Class.forName("RP2Recipes").newInstance();
				System.out.println("   Success!");
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("   Failure! ClassNotFoundException");
			}
			catch(InstantiationException e)
			{
				System.out.println("   Failure! InstantiationException");
			}
			catch(IllegalAccessException e)
			{
				System.out.println("   Failure! IllegalAccessException");
			}
		}
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

	private void addItems()
	{
		itemCraftGuide = new ItemCraftGuide(itemCraftGuideID);
		ModLoader.addName(itemCraftGuide, "Crafting Guide");

		ModLoader.addRecipe(new ItemStack(itemCraftGuide), new Object[] {"pbp",
				"bcb", "pbp", Character.valueOf('c'), Block.workbench,
				Character.valueOf('p'), Item.paper, Character.valueOf('b'),
				Item.book});
	}

	private void setConfigDefaults()
	{
		config.setProperty("itemCraftGuideID", "23361");
		config.setProperty("RecipeList_mouseWheelScrollRate", "3");
		config.setProperty("PauseWhileOpen", Boolean.toString(true));
		config.setProperty("resizeRate", "0");
		config.setProperty("gridPacking", Boolean.toString(true));
		config.setProperty("alwaysShowID", Boolean.toString(false));
		config.setProperty("textSearchRequiresShift", Boolean.toString(false));
	}

	/**
	 * Load configuration. If a configuration file exists in the new
	 * location, load from there. If not, but one exists in the old
	 * location, use that instead. If neither exists, just use the
	 * defaults.
	 * 
	 * Afterwards, save it back to the new configuration directory
	 * (to create it if it doesn't exist, or to update it if it was
	 * created by an earlier version of CraftGuide that didn't have
	 * exactly the same set of properties).
	 */
	private void loadProperties()
	{
		File oldConfigDir = new File(Minecraft.getMinecraftDir(), "/config/");
		File oldConfigFile = new File(oldConfigDir, "CraftGuide.cfg");
		File newConfigDir = configDirectory();
		File newConfigFile = newConfigDir == null? null : new File(newConfigDir, "CraftGuide.cfg");
		File configFile = null;

		if(newConfigFile != null && newConfigFile.exists())
		{
			configFile = newConfigFile;
		}
		else if(oldConfigFile.exists() && oldConfigFile.canRead())
		{
			configFile = oldConfigFile;
		}
		
		setConfigDefaults();

		if(configFile != null && configFile.exists() && configFile.canRead())
		{
			try
			{
				config.load(new FileInputStream(configFile));
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			itemCraftGuideID = Integer.valueOf(config
					.getProperty("itemCraftGuideID"));
		}
		catch(NumberFormatException e)
		{
		}

		try
		{
			resizeRate = Integer.valueOf(config.getProperty("resizeRate"));
		}
		catch(NumberFormatException e)
		{
		}

		try
		{
			mouseWheelScrollRate = Integer.valueOf(config
					.getProperty("RecipeList_mouseWheelScrollRate"));
		}
		catch(NumberFormatException e)
		{
		}

		pauseWhileOpen = Boolean.valueOf(config.getProperty("PauseWhileOpen"));
		gridPacking = Boolean.valueOf(config.getProperty("gridPacking"));
		alwaysShowID = Boolean.valueOf(config.getProperty("alwaysShowID"));
		textSearchRequiresShift = Boolean.valueOf(config.getProperty("textSearchRequiresShift"));

		if(newConfigFile != null && !newConfigFile.exists())
		{
			try
			{
				newConfigFile.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		if(newConfigFile != null && newConfigFile.exists() && newConfigFile.canWrite())
		{
			try
			{
				config.store(new FileOutputStream(newConfigFile), "");
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void initKeybind()
	{
		KeyBindingRegistry.registerKeyBinding(new CraftGuideKeyHandler());
	}

	public static File themeDirectory()
	{
		File configDir = configDirectory();
		
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

	private static File configDirectory()
	{
		File dir = new File(Loader.instance().getConfigDir(), "CraftGuide");
		
		if(!dir.exists() && !dir.mkdirs())
		{
			return null;
		}
		
		return dir;
	}
}
