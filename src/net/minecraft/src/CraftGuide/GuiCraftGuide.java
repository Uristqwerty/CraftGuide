package net.minecraft.src.CraftGuide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.mod_CraftGuide;
import net.minecraft.src.CraftGuide.ui.CraftingDisplay;
import net.minecraft.src.CraftGuide.ui.GuiButton;
import net.minecraft.src.CraftGuide.ui.GuiElement;
import net.minecraft.src.CraftGuide.ui.GuiImage;
import net.minecraft.src.CraftGuide.ui.GuiItemStack;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.GuiRightAlignedText;
import net.minecraft.src.CraftGuide.ui.GuiSlider;
import net.minecraft.src.CraftGuide.ui.GuiScrollBar;
import net.minecraft.src.CraftGuide.ui.GuiText;
import net.minecraft.src.CraftGuide.ui.GuiValueButton;
import net.minecraft.src.CraftGuide.ui.GuiWindow;
import net.minecraft.src.CraftGuide.ui.IButtonListener;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;

public class GuiCraftGuide extends GuiScreen
{
	private GuiWindow guideWindow;
	private GuiRenderer renderer;
	private RecipeCache recipeCache = new RecipeCache();
	private GuiScrollBar scrollBar;
	private GuiRightAlignedText rowText;
	private GuiItemStack filterStack;

	private class FilterClearCallback implements IButtonListener
	{
		private CraftingDisplay display;
		
		public void onButtonEvent(GuiButton button, Event eventType)
		{
			if(eventType == Event.PRESS)
			{
				display.setFilter(null);
			}
		}
	}
	
	public GuiCraftGuide()
	{
		final int windowWidth = 256;
		final int windowHeight = 198;

		GuiTexture guiTexture = GuiTexture.getInstance("/gui/CraftGuide.png");
		FilterClearCallback clearCallback = new FilterClearCallback();
		GuiButton clearButton = new GuiButton(10, 179, 50, 14, guiTexture, 48, 200, 0, 14);
		clearButton.addButtonListener(clearCallback);

		renderer = new GuiRenderer();
		guideWindow = new GuiWindow(0, 0, windowWidth, windowHeight, renderer);
		guideWindow.addElement(new GuiImage(0, 0, windowWidth, windowHeight, guiTexture, 0, 0));
		guideWindow.addElement(new GuiText(11, 163, "Filter", 0xff000000));
		guideWindow.addElement(clearButton);
		guideWindow.addElement(new GuiText(22, 183, "Clear", 0xff000000));
		
		rowText = new GuiRightAlignedText(233, 6, "", 0xff000000);
		guideWindow.addElement(rowText);
		
		scrollBar = 
			new GuiScrollBar(238, 6, 12, 184, 
				new GuiSlider(0, 21, 12, 144, 12, 15, guiTexture, 0, 199),
				new GuiValueButton[]{
    				new GuiValueButton(0,   0, 12, 11, guiTexture, 0, 234, -30),
    				new GuiValueButton(0,  11, 12, 10, guiTexture, 0, 214, -3 ),
    				new GuiValueButton(0, 165, 12, 10, guiTexture, 0, 224,  3 ),
    				new GuiValueButton(0, 175, 12, 11, guiTexture, 0, 245,  30)
				}
			);
		
		guideWindow.addElement(scrollBar);
		
		CraftingDisplay craftingDisplay = new CraftingDisplay(72, 18, 162, 174, scrollBar, recipeCache);
		
		guideWindow.addElement(craftingDisplay);
		clearCallback.display = craftingDisplay;
		
		filterStack = new GuiItemStack(43, 158, false);
		guideWindow.addElement(filterStack);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		rowText.setText("Rows " + ((int)scrollBar.getValue() + 1) + "-" +  + ((int)scrollBar.getValue() + 3)  + " of " + ((int)scrollBar.getMax() + 3));
		guideWindow.centerOn(width / 2, height / 2);
		filterStack.setItem(recipeCache.getFilterItem());
		
		drawDefaultBackground();

		renderer.startFrame(mc, this);
		GuiTexture.refreshTextures(mc.renderEngine);
		guideWindow.draw();
		renderer.endFrame();
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey)
	{
		super.keyTyped(eventChar, eventKey);
        if(eventKey == Keyboard.KEY_ESCAPE || eventKey == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }
        else
        {
        	switch(eventKey)
        	{
        		case Keyboard.KEY_UP:
        			scrollBar.scroll(-1, true);
        			break;
        			
        		case Keyboard.KEY_DOWN:
        			scrollBar.scroll(1, true);
        			break;
        			
        		case Keyboard.KEY_LEFT:
        		case Keyboard.KEY_PRIOR:
        			scrollBar.scroll(-3, true);
        			break;
        			
        		case Keyboard.KEY_RIGHT:
        		case Keyboard.KEY_NEXT:
        			scrollBar.scroll(3, true);
        			break;
        			
        		case Keyboard.KEY_HOME:
        			scrollBar.scrollToStart();
        			break;
        			
        		case Keyboard.KEY_END:
        			scrollBar.scrollToEnd();
        			break;
        	}
        }
	}
	
	@Override
	public void handleMouseInput()
	{
        int x = (Mouse.getEventX() * width) / mc.displayWidth;
        int y = height - (Mouse.getEventY() * height) / mc.displayHeight - 1;
        
        if(Mouse.getEventButton() == 0)
        {
        	guideWindow.updateMouseState(x, y, Mouse.getEventButtonState());
        }
        else if(Mouse.getEventButton() == -1)
        {
        	guideWindow.updateMouse(x, y);
		}
        
    	if(Mouse.getEventDWheel() != 0)
    	{
    		if(Mouse.getEventDWheel() > 0)
    		{
    			scrollBar.scroll(-mod_CraftGuide.mouseWheelScrollRate, true);
    		}
    		else
    		{
    			scrollBar.scroll(mod_CraftGuide.mouseWheelScrollRate, true);
    		}
    	}
	}

	@Override
	public void handleKeyboardInput()
	{
		super.handleKeyboardInput();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			scrollBar.setScrollMultiplier(10);
		}
		else
		{
			scrollBar.setScrollMultiplier(1);
		}
	}
}
