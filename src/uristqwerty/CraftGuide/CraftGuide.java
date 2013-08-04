package uristqwerty.CraftGuide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.RecipeGeneratorImplementation.RecipeGeneratorForgeExtension;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.gui_craftguide.theme.ThemeManager;

public class CraftGuide
{
	public static CraftGuideSide side;
	public static CraftGuideLoaderSide loaderSide;

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
	public static boolean insertBetterWithRenewablesRecipes = false;
	public static boolean enableItemRecipe;

	public static boolean betterWithRenewablesDetected = false;
	public static boolean needsRecipeRefresh = false;

	private int itemCraftGuideID = 23361;

	public static final int DAMAGE_WILDCARD = 32767;

	public void preInit()
	{
		CraftGuideLog.init(new File(configDirectory(), "CraftGuide.log"));

		initForgeExtensions();
		loadProperties();

		side.preInit();

		ItemSlot.implementation = new ItemSlotImplementationImplementation();

		if(enableKeybind)
		{
			side.initKeybind();
		}

		addItem();
	}

	public void init()
	{
		try
		{
			Class.forName("uristqwerty.CraftGuide.recipes.DefaultRecipeProvider").newInstance();
			Class.forName("uristqwerty.CraftGuide.recipes.BrewingRecipes").newInstance();
		}
		catch(InstantiationException e)
		{
			CraftGuideLog.log(e);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e);
		}
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e);
		}

		loadModRecipes("BTW", "uristqwerty.CraftGuide.recipes.BTWRecipes");
		loadModRecipes("IC2", "uristqwerty.CraftGuide.recipes.IC2Recipes");
		loadModRecipes("gregtech_addon", "uristqwerty.CraftGuide.recipes.GregTechRecipes");
		loadModRecipes("extendedWorkbench", "uristqwerty.CraftGuide.recipes.ExtendedWorkbench");
		loadModRecipes("BuildCraft|Factory", "uristqwerty.CraftGuide.recipes.BuildCraftRecipes");
		side.initNetworkChannels();
	}

	private void loadModRecipes(String modID, String recipeClass)
	{
		if(loaderSide.isModLoaded(modID))
		{
			try
			{
				Class.forName(recipeClass).newInstance();
			}
			catch(ClassNotFoundException e)
			{
				CraftGuideLog.log(e);
			}
			catch(Exception e)
			{
				CraftGuideLog.log(e, "", true);
			}
		}
	}

	private void initForgeExtensions()
	{
		if(loaderSide.isModLoaded("Forge"))
		{
			try
			{
				RecipeGeneratorImplementation.forgeExt = (RecipeGeneratorForgeExtension)Class.forName("uristqwerty.CraftGuide.ForgeStuff").newInstance();
			}
			catch(InstantiationException e)
			{
				CraftGuideLog.log(e);
			}
			catch(IllegalAccessException e)
			{
				CraftGuideLog.log(e);
			}
			catch(ClassNotFoundException e)
			{
				CraftGuideLog.log(e);
			}
		}
	}

	private void addItem()
	{
		itemCraftGuide = new ItemCraftGuide(itemCraftGuideID);
		loaderSide.addName(itemCraftGuide, "Crafting Guide");

		if(enableItemRecipe)
		{
			loaderSide.addRecipe(new ItemStack(itemCraftGuide), new Object[] {"pbp",
					"bcb", "pbp", Character.valueOf('c'), Block.workbench,
					Character.valueOf('p'), Item.paper, Character.valueOf('b'),
					Item.book});
		}
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
		config.setProperty("enableItemRecipe", Boolean.toString(true));
		config.setProperty("newerBackgroundStyle", Boolean.toString(false));
		config.setProperty("hideMundanePotionRecipes", Boolean.toString(true));
		config.setProperty("insertBetterWithRenewablesRecipes", Boolean.toString(false));
		config.setProperty("logThemeDebugInfo", Boolean.toString(false));
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
		File oldConfigDir = loaderSide.getConfigDir();
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
		insertBetterWithRenewablesRecipes = Boolean.valueOf(config.getProperty("insertBetterWithRenewablesRecipes"));
		ThemeManager.debugOutput = Boolean.valueOf(config.getProperty("logThemeDebugInfo"));
		enableItemRecipe = Boolean.valueOf(config.getProperty("enableItemRecipe"));

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
		File dir = new File(loaderSide.getConfigDir(), "CraftGuide");

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
}
