package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import net.minecraft.src.CraftGuide.DefaultRecipeProvider;
import net.minecraft.src.CraftGuide.ItemCraftGuide;
public class mod_CraftGuide extends BaseMod
{
	public static ItemCraftGuide itemCraftGuide;
	private static Properties config = new Properties();

	public static int resizeRate;
	public static int mouseWheelScrollRate;
	public static boolean pauseWhileOpen = true;
	public static boolean gridPacking = true;
	public static long keyboardRepeatDelay = 1000;
	public static long keyboardRepeatRate = 200;
	
	private int itemCraftGuideID = 23361;
	
	public mod_CraftGuide()
	{
	}

	@Override
	public String getVersion()
	{
		return "1.4.3 for Minecraft 1.1.0";
	}

	@Override
	public void load()
	{
		loadProperties();
		addItems();
		
		new DefaultRecipeProvider();
		
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

	private void addItems()
	{
		itemCraftGuide = new ItemCraftGuide(itemCraftGuideID);
		ModLoader.addName(itemCraftGuide, "Crafting Guide");
		
		ModLoader.addRecipe(new ItemStack(itemCraftGuide), new Object[]{
			"pbp",
			"bcb",
			"pbp",
			Character.valueOf('c'), Block.workbench,
			Character.valueOf('p'), Item.paper,
			Character.valueOf('b'), Item.book
			});
	}

	private void setConfigDefaults()
	{
		config.setProperty("itemCraftGuideID", "23361");
		config.setProperty("RecipeList_mouseWheelScrollRate", "3");
		config.setProperty("PauseWhileOpen", Boolean.toString(true));
		config.setProperty("resizeRate", "0");
		config.setProperty("gridPacking", Boolean.toString(true));
		config.setProperty("keyboardRepeatDelay", Long.toString(1000));
		config.setProperty("keyboardRepeatRate", Long.toString(200));
	}

	private void loadProperties()
	{
		File configDir = new File(Minecraft.getMinecraftDir(), "/config/");
		File configFile = new File(configDir, "CraftGuide.cfg");
		
		setConfigDefaults();
		
		if(configFile.exists() && configFile.canRead())
		{
			try {
				config.load(new FileInputStream(configFile));
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			itemCraftGuideID = Integer.valueOf(config.getProperty("itemCraftGuideID"));
		}
		catch(NumberFormatException e){}
		
		try
		{
			resizeRate = Integer.valueOf(config.getProperty("resizeRate"));
		}
		catch(NumberFormatException e){}
		
		try
		{
			mouseWheelScrollRate = Integer.valueOf(config.getProperty("RecipeList_mouseWheelScrollRate"));
		}
		catch(NumberFormatException e){}
		
		pauseWhileOpen = Boolean.valueOf(config.getProperty("PauseWhileOpen"));
		gridPacking = Boolean.valueOf(config.getProperty("gridPacking"));
		
		try
		{
			keyboardRepeatDelay = Long.valueOf(config.getProperty("keyboardRepeatDelay"));
		}
		catch(NumberFormatException e){}
		
		try
		{
			keyboardRepeatRate = Long.valueOf(config.getProperty("keyboardRepeatRate"));
		}
		catch(NumberFormatException e){}
		
		if(!configFile.exists())
		{
			try {
				configFile.createNewFile();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		if(configFile.exists() && configFile.canWrite())
		{
			try {
				config.store(new FileOutputStream(configFile), "");
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
