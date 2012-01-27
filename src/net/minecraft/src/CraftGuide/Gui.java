package net.minecraft.src.CraftGuide;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.mod_CraftGuide;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.GuiTextInput;
import net.minecraft.src.CraftGuide.ui.GuiWindow;

public class Gui extends GuiScreen
{
	private GuiRenderer renderer = new GuiRenderer();
	protected GuiWindow guiWindow;
	
	private int repeatKey;
	private char repeatChar;
	private long nextRepeat;

	public Gui(int windowWidth, int windowHeight)
	{
		super();

		guiWindow = new GuiWindow(0, 0, windowWidth, windowHeight, renderer);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		guiWindow.setMaxSize(width, height);
		
		if(nextRepeat != 0 && System.currentTimeMillis() > nextRepeat)
		{
			nextRepeat = System.currentTimeMillis() + keyboardRepeatRate();
			keyRepeat(repeatChar, repeatKey);
		}
		
		if(doesGuiPauseGame())
		{
			drawDefaultBackground();
		}

		renderer.startFrame(mc, this);
		guiWindow.draw();
		renderer.endFrame();
	}

	protected long keyboardRepeatRate()
	{
		return 200;
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey)
	{
		super.keyTyped(eventChar, eventKey);
		
		repeatKey = eventKey;
		repeatChar = eventChar;
		nextRepeat = System.currentTimeMillis() + mod_CraftGuide.keyboardRepeatDelay;
		
		if(GuiTextInput.inFocus != null)
		{
			GuiTextInput.inFocus.onKeyTyped(eventChar, eventKey);
		}
		else if(eventKey == Keyboard.KEY_ESCAPE || eventKey == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }
        else
        {
        	guiWindow.onKeyTyped(eventChar, eventKey);
        }
	}
	
	private void keyRepeat(char eventChar, int eventKey)
	{
		if(GuiTextInput.inFocus != null)
		{
			GuiTextInput.inFocus.onKeyTyped(eventChar, eventKey);
		}
        else
        {
        	guiWindow.onKeyTyped(eventChar, eventKey);
        }
	}
	
	private void keyReleased(char eventChar, int eventKey)
	{
		nextRepeat = 0;
	}
	
	@Override
	public void handleMouseInput()
	{
        int x = (Mouse.getEventX() * width) / mc.displayWidth;
        int y = height - (Mouse.getEventY() * height) / mc.displayHeight - 1;
        
        if(Mouse.getEventButton() == 0)
        {
        	guiWindow.updateMouseState(x, y, Mouse.getEventButtonState());
        }
        else if(Mouse.getEventButton() == -1)
        {
        	guiWindow.updateMouse(x, y);
		}
        
    	if(Mouse.getEventDWheel() != 0)
    	{
    		guiWindow.scrollWheelTurned(Mouse.getEventDWheel() > 0? -mouseWheelRate() : mouseWheelRate());
    	}
	}

	@Override
	public void handleKeyboardInput()
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
        else
        {
        	keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
	}
	
	public int mouseWheelRate()
	{
		return 1;
	}
	
	@Override
	public void onGuiClosed()
	{
		nextRepeat = 0;
		guiWindow.onGuiClosed();
	}
}