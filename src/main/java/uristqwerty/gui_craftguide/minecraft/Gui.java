package uristqwerty.gui_craftguide.minecraft;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import uristqwerty.CraftGuide.client.ui.GuiRenderer;
import uristqwerty.CraftGuide.client.ui.GuiTextInput;
import uristqwerty.gui_craftguide.components.GuiElement;
import uristqwerty.gui_craftguide.components.Window;
import uristqwerty.gui_craftguide.rendering.RendererBase;

public class Gui extends GuiScreen
{
	protected GuiRenderer renderer = (GuiRenderer)RendererBase.instance;
	protected Window guiWindow;
	private boolean firstFrame = false;

	public Gui(int windowWidth, int windowHeight)
	{
		super();

		guiWindow = new Window(0, 0, windowWidth, windowHeight, renderer);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		guiWindow.setMaxSize(width, height);
		guiWindow.update();

		if(doesGuiPauseGame())
		{
			drawDefaultBackground();
		}

		renderer.startFrame(this);
		guiWindow.draw();
		renderer.endFrame();
		firstFrame = false;
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey) throws IOException
	{
		super.keyTyped(eventChar, eventKey);

		if(GuiTextInput.inFocus != null)
		{
			if(!firstFrame)
			{
				GuiTextInput.inFocus.onKeyTyped(eventChar, eventKey);
			}
		}
		else if(eventKey == Keyboard.KEY_ESCAPE || eventKey == mc.gameSettings.keyBindInventory.getKeyCode())
		{
			mc.thePlayer.closeScreen();
		}
		else
		{
			guiWindow.onKeyTyped(eventChar, eventKey);
		}
	}

	@Override
	public void handleMouseInput()
	{
		int x = (Mouse.getEventX() * width) / mc.displayWidth;
		int y = height - (Mouse.getEventY() * height) / mc.displayHeight - 1;
		int mouseButton = Mouse.getEventButton();

		if(mouseButton != -1)
		{
			if(mouseButton == 1 || (mouseButton == 0 && Minecraft.IS_RUNNING_ON_MAC && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157))))
			{
				guiWindow.updateMouseState(x, y, Mouse.getEventButtonState(), GuiElement.MouseClick.RIGHT_CLICK);
			}
			else if(mouseButton == 0)
			{
				guiWindow.updateMouseState(x, y, Mouse.getEventButtonState(), GuiElement.MouseClick.LEFT_CLICK);
			}
		}
		else
		{
			guiWindow.updateMouse(x, y);
		}

		if(Mouse.getEventDWheel() != 0)
		{
			guiWindow.scrollWheelTurned(Mouse.getEventDWheel() > 0? -mouseWheelRate() : mouseWheelRate());
		}
	}

	@Override
	public void handleKeyboardInput() throws IOException
	{
		if(Keyboard.getEventKeyState())
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_F11)
			{
				mc.toggleFullscreen();
				return;
			}
			else
			{
				keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
		}
	}

	public int mouseWheelRate()
	{
		return 1;
	}

	@Override
	public void onGuiClosed()
	{
		guiWindow.onGuiClosed();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		firstFrame = true;
	}
}