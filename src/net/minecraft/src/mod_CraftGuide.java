package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Slot;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.CraftGuideSide;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.ItemCraftGuide;
import uristqwerty.CraftGuide.ItemSlotImplementationImplementation;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.client.CraftGuideClient;

public class mod_CraftGuide extends BaseMod
{
	public static CraftGuideSide side = new CraftGuideClient();

	public static ItemCraftGuide itemCraftGuide;
	private static Properties config = new Properties();

	public static int resizeRate;
	public static int mouseWheelScrollRate;
	public static boolean pauseWhileOpen = true;
	public static boolean gridPacking = true;
	public static boolean alwaysShowID = false;
	public static boolean textSearchRequiresShift = false;
	public static boolean enableKeybind = true;
	public static boolean newerBackgroundStyle = false;
	public static boolean hideMundanePotionRecipes = true;

	private int itemCraftGuideID = 23361;

	private KeyBinding keyBinding;


	public void preInit()
	{
		CraftGuideLog.init(new File(configDirectory(), "CraftGuide.log"));

		side.preInit();
		ItemSlot.implementation = new ItemSlotImplementationImplementation();

		loadProperties();

		if(enableKeybind)
		{
			side.initKeybind();
		}
	}

	public void init()
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
		config.setProperty("enableKeybind", Boolean.toString(true));
		config.setProperty("newerBackgroundStyle", Boolean.toString(false));
		config.setProperty("hideMundanePotionRecipes", Boolean.toString(true));
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
		File oldConfigDir = new File(Minecraft.getMinecraftDir(), "config");
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
			itemCraftGuideID = Integer.valueOf(config.getProperty("itemCraftGuideID"));
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
		enableKeybind = Boolean.valueOf(config.getProperty("enableKeybind"));
		newerBackgroundStyle = Boolean.valueOf(config.getProperty("newerBackgroundStyle"));
		hideMundanePotionRecipes = Boolean.valueOf(config.getProperty("hideMundanePotionRecipes"));

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

	public static File configDirectory()
	{
		File dir = new File(new File(Minecraft.getMinecraftDir(), "config"), "CraftGuide");

		if(!dir.exists() && !dir.mkdirs())
		{
			return null;
		}

		return dir;
	}

	public static String getTranslation(String string)
	{
		if(string.equals("filter_type.input"))
		{
			return "Input";
		}
		else if(string.equals("filter_type.output"))
		{
			return "Output";
		}
		else if(string.equals("filter_type.machine"))
		{
			return "Machine";
		}
		else if(string.equals("filter"))
		{
			return "Filter";
		}
		else
		{
			return null;
		}
	}

	@Override
	public String getVersion()
	{
		return "1.6.2-modloader";
	}

	@Override
	public void load()
	{
		preInit();
		init();

		keyBinding = new KeyBinding("Open CraftGuide", Keyboard.KEY_G);
		ModLoader.registerKey(this, keyBinding, true);
	}

	@Override
	public void keyboardEvent(KeyBinding kb)
	{
		if(kb == keyBinding && enableKeybind)
		{
			Minecraft mc = ModLoader.getMinecraftInstance();
			GuiScreen screen = mc.currentScreen;
			if(screen == null)
			{
				side.openGUI(mc.thePlayer);
			}
			else if(screen instanceof GuiContainer)
			{
				try
				{
					int x = Mouse.getX() * screen.width / mc.displayWidth;
					int y = screen.height - (Mouse.getY() * screen.height / mc.displayHeight) - 1;
					int left = (Integer)getPrivateValue(GuiContainer.class, screen, "m", "guiLeft");
					int top = (Integer)getPrivateValue(GuiContainer.class, screen, "n", "guiTop");
					openRecipe((GuiContainer)screen, x - left, y - top);
				}
				catch(IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch(SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private <T> Object getPrivateValue(Class<? extends T> objectClass, T object, String obfuscatedName, String name)
	{
		try
		{
			Field field;
			try
			{
				field = objectClass.getDeclaredField(obfuscatedName);
			}
			catch(NoSuchFieldException e)
			{
				field = objectClass.getDeclaredField(name);
			}

			field.setAccessible(true);
			return field.get(object);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void openRecipe(GuiContainer screen, int x, int y)
	{
		Container container = screen.inventorySlots;

		for(int i = 0; i < container.inventorySlots.size(); i++)
		{
			Slot slot = (Slot)container.inventorySlots.get(i);
	        if(x > slot.xDisplayPosition - 2 && x < slot.xDisplayPosition + 17 && y > slot.yDisplayPosition - 2 && y < slot.yDisplayPosition + 17)
	        {
	        	ItemStack item = slot.getStack();

	        	if(item != null)
	        	{
	    			Minecraft mc = ModLoader.getMinecraftInstance();
	        		GuiCraftGuide.getInstance().setFilterItem(item);
					side.openGUI(mc.thePlayer);
	        	}

	        	break;
	        }
		}
	}
}
