package uristqwerty.CraftGuide.client.fml;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.CraftGuideClient;

public class CraftGuideClient_FML extends CraftGuideClient
{
	private KeyBinding key;

	@Override
	public void initKeybind()
	{
		key = new KeyBinding("Open CraftGuide", CraftGuide.defaultKeybind, "key.categories.misc");
		ClientRegistry.registerKeyBinding(key);
	}

	@Override
	public void checkKeybind()
	{
		try
		{
			if(key != null && CraftGuide.enableKeybind && Keyboard.isKeyDown(key.getKeyCode()))
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
						int left = (Integer)CommonUtilities.getPrivateValue(GuiContainer.class, (GuiContainer)screen, "field_147003_i", "i", "guiLeft");
						int top = (Integer)CommonUtilities.getPrivateValue(GuiContainer.class, (GuiContainer)screen, "field_147009_r", "r", "guiTop");
						openRecipe((GuiContainer)screen, x - left, y - top);
					}
					catch(IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e)
					{
						CraftGuideLog.log(e, "Exception while trying to identify if there is an item at the current cursor position.", true);
					}
				}
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			CraftGuideLog.log(e, "Exception while checking keybind.", true);
		}
	}

	private void openRecipe(GuiContainer screen, int x, int y)
	{
		Container container = screen.inventorySlots;

		for(int i = 0; i < container.inventorySlots.size(); i++)
		{
			Slot slot = container.inventorySlots.get(i);
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
	public void openGUI(EntityPlayer player)
	{
		FMLClientHandler.instance().displayGuiScreen(player, GuiCraftGuide.getInstance());
	}

	boolean failed = false;

	@Override
	public void stopTessellating()
	{
		if(failed)
			return;

		try
		{
			if((Boolean)CommonUtilities.getPrivateValue(VertexBuffer.class, Tessellator.getInstance().getBuffer(),
					"isDrawing",
					//"field_78415_z", "x", /* Field names in 1.7.10's Tessellator */
					"field_179010_r", "n"  /* Field names in 1.8.9's WorldRenderer */
					))
			{
				Tessellator.getInstance().draw();
			}
		}
		catch(SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
			failed = true;
		}
	}
}
