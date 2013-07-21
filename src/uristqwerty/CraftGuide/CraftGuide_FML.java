package uristqwerty.CraftGuide;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "craftguide", name = "CraftGuide", version = "1.5.2")
public class CraftGuide_FML implements CraftGuideLoaderSide
{
	private static Logger logger = Logger.getLogger("CraftGuide");

	@SidedProxy(clientSide = "uristqwerty.CraftGuide.client.fml.CraftGuideClient_FML",
				serverSide = "uristqwerty.CraftGuide.server.CraftGuideServer")
	public static CraftGuideSide side;

	private CraftGuide craftguide;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger.setParent(FMLLog.getLogger());
		CraftGuide.loaderSide = this;
		CraftGuide.side = side;
		craftguide = new CraftGuide();
		craftguide.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		craftguide.init();
	}

	@Override
	public boolean isModLoaded(String name)
	{
		return Loader.isModLoaded(name);
	}

	@Override
	public File getConfigDir()
	{
		return Loader.instance().getConfigDir();
	}

	@Override
	public void addRecipe(ItemStack output, Object[] recipe)
	{
		GameRegistry.addRecipe(output, recipe);
	}

	@Override
	public void addName(Item item, String name)
	{
		LanguageRegistry.addName(item, name);
	}

	@Override
	public void logConsole(String text)
	{
		logger.log(Level.INFO, text);
	}

	@Override
	public void logConsole(String text, Throwable e)
	{
		logger.log(Level.WARNING, text, e);
	}

	@Override
	public void initClientNetworkChannels()
	{
		// TODO Auto-generated method stub

	}
}
