package uristqwerty.CraftGuide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.Util;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
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
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		loadProperties();
		initKeybind();
		Util.instance = new UtilImplementation();
		ItemSlot.implementation = new ItemSlotImplementationImplementation();
	}

	@Init
	public void init(FMLInitializationEvent event)
	{
		addItems();
		extractResources();

		CraftGuideLog.init(new File(Minecraft.getMinecraftDir(),
				"CraftGuide.log"));

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
		try
		{
			ZipInputStream resources = new ZipInputStream(getClass()
					.getResourceAsStream("resources.zip"));
			byte[] buffer = new byte[1024 * 16];
			ZipEntry entry;
			while((entry = resources.getNextEntry()) != null)
			{
				File destination = new File(Minecraft.getMinecraftDir(), entry.getName());

				if(!destination.exists())
				{
					if(entry.isDirectory())
					{
						destination.mkdir();
					}
					else
					{
						System.out.println("Extracting " + entry.getName());
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

	private void loadProperties()
	{
		File configDir = new File(Minecraft.getMinecraftDir(), "/config/");
		File configFile = new File(configDir, "CraftGuide.cfg");

		setConfigDefaults();

		if(configFile.exists() && configFile.canRead())
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

		if(!configFile.exists())
		{
			try
			{
				configFile.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		if(configFile.exists() && configFile.canWrite())
		{
			try
			{
				config.store(new FileOutputStream(configFile), "");
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
}
