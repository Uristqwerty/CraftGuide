package uristqwerty.CraftGuide;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import uristqwerty.CraftGuide.client.liteLoader.CraftGuideClient_LiteLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;

import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;

public class LiteMod_CraftGuide implements LiteMod, InitCompleteListener, CraftGuideLoaderSide
{
	private File configDir;
	private CraftGuide craftguide;

	private CraftGuideSide side;

	public LiteMod_CraftGuide()
	{
		side = new CraftGuideClient_LiteLoader();
	}

	@Override
	public String getName()
	{
		return "CraftGuide";
	}

	@Override
	public String getVersion()
	{
		return "@MOD_VERSION@";
	}

	@Override
	public void init(File configPath)
	{
		configDir = configPath;

		CraftGuide.loaderSide = this;
		CraftGuide.side = side;
		craftguide = new CraftGuide();
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
	{
		side.checkKeybind();
	}

	@Override
	public void onInitCompleted(Minecraft minecraft, LiteLoader loader)
	{
		craftguide.preInit("craftguide:craftguide_item", true);
		craftguide.init();
	}

	private boolean checkedForFML = false;
	private Method isModLoaded = null;

	@Override
	public boolean isModLoaded(String name)
	{
		if(!checkedForFML)
		{
			checkedForFML = true;

			try
			{
				isModLoaded = Class.forName("cpw.mods.fml.common.Loader").getMethod("isModLoaded", String.class);
			}
			catch(SecurityException e)
			{
				CraftGuideLog.log(e, "", true);
			}
			catch(NoSuchMethodException | ClassNotFoundException e)
			{
			}
		}

		if(isModLoaded != null)
		{
			try
			{
				return (Boolean)isModLoaded.invoke(null, name);
			}
			catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e)
			{
				CraftGuideLog.log(e, "", true);
			}
		}

		return false;
	}

	@Override
	public File getConfigDir()
	{
		return configDir;
	}

	@Override
	public File getLogDir()
	{
		File dir = new File(Minecraft.getMinecraft().mcDataDir, "logs");

		if(!dir.exists() && !dir.mkdirs())
		{
			return null;
		}

		return dir;
	}

	@Override
	public void addRecipe(ItemStack output, Object[] input)
	{
		// TODO: Replace with either a LiteLoader-compatible way to call CraftingManager.addRecipe, or a proper implementation.
		ItemStack table = new ItemStack(Blocks.crafting_table);
		ItemStack paper = new ItemStack(Items.paper);
		ItemStack book = new ItemStack(Items.book);
		ItemStack[] inputItems = new ItemStack[]{paper, book, paper, book, table, book, paper, book, paper};
		CraftingManager.getInstance().getRecipeList().add(new ShapedRecipes(3, 3, inputItems, output));
	}

	@Override
	public void logConsole(String text)
	{
		System.out.println(text);
	}

	@Override
	public void logConsole(String text, Throwable e)
	{
		System.out.println(text);
		e.printStackTrace();
	}

	@Override
	public void initClientNetworkChannels()
	{
	}
}
