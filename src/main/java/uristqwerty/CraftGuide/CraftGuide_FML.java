package uristqwerty.CraftGuide;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "craftguide", name = "CraftGuide", version = "@MOD_VERSION@")
public class CraftGuide_FML implements CraftGuideLoaderSide
{
	private static Logger logger;

	@SidedProxy(clientSide = "uristqwerty.CraftGuide.client.fml.CraftGuideClient_FML",
				serverSide = "uristqwerty.CraftGuide.server.CraftGuideServer")
	public static CraftGuideSide side;

	private CraftGuide craftguide;

	public static class KeyCheckTick
	{
		@SubscribeEvent
		public void clientTick(TickEvent.ClientTickEvent event)
		{
			side.checkKeybind();
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		CraftGuide.loaderSide = this;
		CraftGuide.side = side;
		craftguide = new CraftGuide();
		boolean useTestIcon = false;
		craftguide.preInit(useTestIcon?"craftguide:palette_extension_test_icon":"craftguide:craftguide_item", false);

		MinecraftForge.EVENT_BUS.register(new KeyCheckTick());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		craftguide.init();
		ModelLoader.setCustomModelResourceLocation(CraftGuide.itemCraftGuide, 0, new ModelResourceLocation(CraftGuide.itemCraftGuide.getRegistryName(), "inventory"));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModel(ModelBakeEvent event)
	{
		//ModelLoader.setCustomModelResourceLocation(CraftGuide.itemCraftGuide, 0, new ModelResourceLocation(CraftGuide.itemCraftGuide.getRegistryName(), "inventory"));
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
	public File getLogDir()
	{
		File mcDir = null;
		try
		{
			mcDir = (File)CommonUtilities.getPrivateValue(Loader.class, null, "minecraftDir");
		}
		catch(SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}

		if(mcDir == null)
			return CraftGuide.configDirectory();

		File dir = new File(mcDir, "logs");

		if(!dir.exists() && !dir.mkdirs())
		{
			return CraftGuide.configDirectory();
		}

		return dir;
	}

	@Override
	public void addRecipe(ItemStack output, Object[] recipe)
	{
		GameRegistry.addRecipe(output, recipe);
	}

	@Override
	public void logConsole(String text)
	{
		logger.log(Level.INFO, text);
	}

	@Override
	public void logConsole(String text, Throwable e)
	{
		logger.log(Level.WARN, text, e);
	}

	@Override
	public void initClientNetworkChannels()
	{
	}
}
