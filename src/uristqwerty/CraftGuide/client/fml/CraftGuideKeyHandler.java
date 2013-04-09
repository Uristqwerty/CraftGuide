package uristqwerty.CraftGuide.client.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
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
					int left = (Integer)CommonUtilities.getPrivateValue(GuiContainer.class, (GuiContainer)screen, "field_74198_m", "n", "guiLeft");
					int top = (Integer)CommonUtilities.getPrivateValue(GuiContainer.class, (GuiContainer)screen, "field_74197_n", "o", "guiTop");
					openRecipe((GuiContainer)screen, x - left, y - top);
				}
				catch(IllegalArgumentException e)
				{
					CraftGuideLog.log(e, "Exception while trying to identify if there is an item at the current cursor position.", true);
				}
				catch(SecurityException e)
				{
					CraftGuideLog.log(e, "Exception while trying to identify if there is an item at the current cursor position.", true);
				}
				catch(NoSuchFieldException e)
				{
					CraftGuideLog.log(e, "Exception while trying to identify if there is an item at the current cursor position.", true);
				}
				catch(IllegalAccessException e)
				{
					CraftGuideLog.log(e, "Exception while trying to identify if there is an item at the current cursor position.", true);
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
	    			Minecraft mc = FMLClientHandler.instance().getClient();
	        		GuiCraftGuide.getInstance().setFilterItem(item);
					CraftGuide.side.openGUI(mc.thePlayer);
	        	}

	        	break;
	        }
		}
	}
}
