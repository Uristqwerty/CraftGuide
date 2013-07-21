package net.minecraft.src;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLoaderSide;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.BWRData;
import uristqwerty.CraftGuide.client.modloader.CraftGuideClient_ModLoader;

public class mod_CraftGuide extends BaseMod implements CraftGuideLoaderSide
{
	private CraftGuide craftguide;
	private KeyBinding keyBinding;

	public mod_CraftGuide()
	{
		try
		{
			Class loader = Class.forName("cpw.mods.fml.common.Loader");
			insertResources_FML(loader);
		}
		catch(ClassNotFoundException e)
		{
			insertResources_ModLoader();
		}
	}

	private void insertResources_ModLoader()
	{
		try
		{
			File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			addResourcePack(file);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void insertResources_FML(Class loader)
	{
		try
		{
			List mods = (List)loader.getMethod("getModList").invoke(loader.getMethod("instance").invoke(null));
			Class container = Class.forName("cpw.mods.fml.common.modloader.ModLoaderModContainer");
			Method getModId = container.getMethod("getModId");

			for(Object mod: mods)
			{
				if(container.isAssignableFrom(mod.getClass()) && ((String)getModId.invoke(mod)).equalsIgnoreCase("mod_craftguide"))
				{
					addResourcePack((File)container.getMethod("getSource").invoke(mod));
					return;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void addResourcePack(File file) throws IllegalAccessException, NoSuchFieldException
	{
		List resourcePacks = (List)CommonUtilities.getPrivateField(Minecraft.class, "aq", "field_110449_ao").get(Minecraft.getMinecraft());
		if(file.exists())
		{
			if(file.isDirectory())
			{
				resourcePacks.add(new FolderResourcePack(file));
			}
			else
			{
				resourcePacks.add(new FileResourcePack(file));
			}
		}
	}

	public void initKeybind()
	{
		keyBinding = new KeyBinding("Open CraftGuide", Keyboard.KEY_G);
		ModLoader.registerKey(this, keyBinding, false);
	}

	@Override
	public boolean isModLoaded(String name)
	{
		if(name.equals("BTW"))
		{
			return isClassLoaded("mod_FCBetterThanWolves");
		}
		else if(name.equals("Forge"))
		{
			return isClassLoaded("net.minecraftforge.oredict.ShapedOreRecipe");
		}

		return false;
	}

	private boolean isClassLoaded(String classname)
	{
		try
		{
			Class c = Class.forName(classname);
			return c != null;
		}
		catch(ClassNotFoundException e)
		{
			return false;
		}
	}

	@Override
	public File getConfigDir()
	{
		return new File(Minecraft.getMinecraft().mcDataDir, "config");
	}

	@Override
	public void addRecipe(ItemStack itemStack, Object[] objects)
	{
		ModLoader.addRecipe(itemStack, objects);
	}

	@Override
	public void addName(Item item, String name)
	{
		ModLoader.addName(item, name);
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
	public String getVersion()
	{
		return "1.6.3-modloader";
	}

	@Override
	public void load()
	{
		CraftGuide.loaderSide = this;
		CraftGuide.side = new CraftGuideClient_ModLoader();
		craftguide = new CraftGuide();
		craftguide.preInit();
		craftguide.init();
	}

	@Override
	public void keyboardEvent(KeyBinding kb)
	{
		if(kb == keyBinding && CraftGuide.enableKeybind)
		{
			Minecraft mc = Minecraft.getMinecraft();
			GuiScreen screen = mc.currentScreen;
			if(screen == null)
			{
				CraftGuide.side.openGUI(mc.thePlayer);
			}
			else if(screen instanceof GuiContainer)
			{
				try
				{
					int x = Mouse.getX() * screen.width / mc.displayWidth;
					int y = screen.height - (Mouse.getY() * screen.height / mc.displayHeight) - 1;
					int left = (Integer)CommonUtilities.getPrivateValue(GuiContainer.class, (GuiContainer)screen, "e", "n", "field_74198_m", "guiLeft");
					int top = (Integer)CommonUtilities.getPrivateValue(GuiContainer.class, (GuiContainer)screen, "o", "field_74197_n", "guiTop");
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
				catch(NoSuchFieldException e)
				{
					e.printStackTrace();
				}
				catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
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
					Minecraft mc = Minecraft.getMinecraft();
					GuiCraftGuide.getInstance().setFilterItem(item);
					CraftGuide.side.openGUI(mc.thePlayer);
				}

				break;
			}
		}
	}

	@Override
	public void initClientNetworkChannels()
	{
		ModLoader.registerPacketChannel(this, "BWR|VC");
		ModLoader.registerPacketChannel(this, "craftguide");
		ModLoader.registerPacketChannel(this, "CftGde");
		//ModLoader.registerPacketChannel(this, "CG");
	}

	@Override
	public void clientCustomPayload(NetClientHandler handler, Packet250CustomPayload packet)
	{
		if(packet.channel.equals("BWR|VC"))
		{
			CraftGuide.betterWithRenewablesDetected = true;
			CraftGuide.needsRecipeRefresh = true;
		}
		else if(packet.channel.equals("craftguide") || packet.channel.equals("CftGde"))
		{
			CraftGuide.side.handlePacket(handler, packet);
		}
	}

	@Override
	public void clientDisconnect(NetClientHandler handler)
	{
		if(CraftGuide.betterWithRenewablesDetected)
		{
			CraftGuide.betterWithRenewablesDetected = false;
			CraftGuide.needsRecipeRefresh = true;
			BWRData.hasRecipes = false;
		}
	}

	@Override
	public void clientConnect(NetClientHandler handler)
	{
		if(CraftGuide.betterWithRenewablesDetected)
		{
			CraftGuide.betterWithRenewablesDetected = false;
			CraftGuide.needsRecipeRefresh = true;
			BWRData.hasRecipes = false;
		}
	}
}
