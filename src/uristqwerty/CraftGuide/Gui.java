package uristqwerty.CraftGuide;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.GuiTextInput;
import uristqwerty.CraftGuide.ui.GuiWindow;

import net.minecraft.src.GuiScreen;

public class Gui extends GuiScreen
{
	protected GuiRenderer renderer = new GuiRenderer();
	protected GuiWindow guiWindow;

	public Gui(int windowWidth, int windowHeight)
	{
		super();

		guiWindow = new GuiWindow(0, 0, windowWidth, windowHeight, renderer);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		guiWindow.setMaxSize(width, height);
		
		if(doesGuiPauseGame())
		{
			drawDefaultBackground();
		}

		renderer.startFrame(mc, this);
		guiWindow.draw();
		renderer.endFrame();
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey)
	{
		super.keyTyped(eventChar, eventKey);
		
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
}