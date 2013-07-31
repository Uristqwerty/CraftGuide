package net.minecraft.src;

import java.io.File;
import java.util.Arrays;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLoaderSide;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.vanilla.CraftGuideClient_Vanilla;

public class CraftGuide_Vanilla implements CraftGuideLoaderSide
{
	private CraftGuide craftguide;
	private KeyBinding keyBinding;

	@Override
	public boolean isModLoaded(String name)
	{
		if(name.equals("BTW"))
		{
			return isClassLoaded("FCBetterThanWolves");
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
		return new File(Minecraft.getMinecraftDir(), "config");
	}

	@Override
	public void addRecipe(ItemStack itemStack, Object[] objects)
	{
		CraftingManager.getInstance().addRecipe(itemStack, objects);
	}

	@Override
	public void addName(Item item, String name)
	{
		//ModLoader.addName(item, name);
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

	public void load()
	{
		CraftGuide.loaderSide = this;
		CraftGuide.side = new CraftGuideClient_Vanilla();
		craftguide = new CraftGuide();
		craftguide.preInit();
		craftguide.init();
	}

	@Override
	public void initClientNetworkChannels()
	{
		//ModLoader.registerPacketChannel(this, "BWR|VC");
		//ModLoader.registerPacketChannel(this, "craftguide");
		//ModLoader.registerPacketChannel(this, "CftGde");
		//ModLoader.registerPacketChannel(this, "CG");
	}

	public void initKeybind()
	{
		keyBinding = new KeyBinding("Open CraftGuide", Keyboard.KEY_G);
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		KeyBinding[] keyBindings = settings.keyBindings;
		keyBindings = Arrays.copyOf(keyBindings, keyBindings.length + 1);
		keyBindings[keyBindings.length - 1] = keyBinding;
		settings.keyBindings = keyBindings;
	}

	public void checkKeybind()
	{
		if(keyBinding.pressed && CraftGuide.enableKeybind)
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
}
