package uristqwerty.CraftGuide.client;

import java.lang.reflect.Field;
import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Slot;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.GuiCraftGuide;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class CraftGuideKeyHandler extends KeyHandler
{
	private static KeyBinding[] keyBindings = new KeyBinding[] {
		new KeyBinding("Open CraftGuide", Keyboard.KEY_G),
	};

	private static boolean[] repeatings = new boolean[] {
		false,
	};

	private EnumSet<TickType> tickTypes = EnumSet.of(TickType.CLIENT);

	public CraftGuideKeyHandler()
	{
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return tickTypes;
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		if(kb == keyBindings[0] && CraftGuide.enableKeybind)
		{
			Minecraft mc = FMLClientHandler.instance().getClient();
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
					int left = (Integer)getPrivateValue(GuiContainer.class, screen, "m");
					int top = (Integer)getPrivateValue(GuiContainer.class, screen, "n");
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

	private Object getPrivateValue(Class<GuiContainer> classToAccess, Object object, String string) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
        Field f = classToAccess.getDeclaredField(string);
        f.setAccessible(true);
        return f.get(object);
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
	    			Minecraft mc = FMLClientHandler.instance().getClient();
	        		GuiCraftGuide.getInstance().setFilterItem(item);
					CraftGuide.side.openGUI(mc.thePlayer);
	        	}

	        	break;
	        }
		}
	}
}
