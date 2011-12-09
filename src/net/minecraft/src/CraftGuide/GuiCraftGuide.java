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
import net.minecraft.src.CraftGuide.ui.GuiBorderedRect;
import net.minecraft.src.CraftGuide.ui.GuiButton;
import net.minecraft.src.CraftGuide.ui.GuiElement;
import net.minecraft.src.CraftGuide.ui.GuiImage;
import net.minecraft.src.CraftGuide.ui.GuiItemStack;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.GuiRightAlignedText;
import net.minecraft.src.CraftGuide.ui.GuiSlider;
import net.minecraft.src.CraftGuide.ui.GuiScrollBar;
import net.minecraft.src.CraftGuide.ui.GuiTabbedDisplay;
import net.minecraft.src.CraftGuide.ui.GuiText;
import net.minecraft.src.CraftGuide.ui.GuiValueButton;
import net.minecraft.src.CraftGuide.ui.GuiWindow;
import net.minecraft.src.CraftGuide.ui.IButtonListener;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiSubTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.ITexture;

public class GuiCraftGuide extends GuiScreen
{
	private GuiRenderer renderer = new GuiRenderer();
	private RecipeCache recipeCache = new RecipeCache();
	private GuiWindow guideWindow;
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

		guideWindow = new GuiWindow(0, 0, windowWidth, windowHeight, renderer);
		
		guideWindow.addElement(
			new GuiBorderedRect(
				0, 0, windowWidth, windowHeight, guiTexture, 1, 1, 4, 64
			)
		);
		
		guideWindow.addElement(
			new GuiBorderedRect(
				5, 5, 58, 86,
				guiTexture, 78, 1, 2, 32
			)
		);
		
		guideWindow.addElement(
			new GuiTabbedDisplay(
				0, 0, windowWidth, windowHeight,
				new Object[][]{
					{
						generateRecipeTab(windowHeight, windowWidth, guiTexture),
						new GuiButton(
							6, 6, 28, 28,
							guiTexture, 1, 76
						)
					},
					{
						generateTypeTab(windowHeight, windowWidth, guiTexture),
						new GuiButton(
							34, 6, 28, 28,
							guiTexture, 1, 104
						)
					},
				}
			)
		);
	}

	private GuiElement generateRecipeTab(int windowWidth, int windowHeight, GuiTexture guiTexture)
	{
		GuiElement recipeTab = new GuiElement(0, 0, windowWidth, windowHeight);
		
		recipeTab.addElement(
			new GuiBorderedRect(
				67, 17, 168, 176,
				guiTexture, 78, 1, 2, 32
			)
		);
		
		recipeTab.addElement(
			new GuiBorderedRect(
				237, 5, 14, 188,
				guiTexture, 78, 1, 2, 32
			)
		);

		GuiButton clearButton = new GuiButton(8, 179, 50, 14, guiTexture, 48, 200, 0, 14);
		recipeTab.addElement(clearButton);
		recipeTab.addElement(new GuiText(9, 163, "Filter", 0xff000000));
		recipeTab.addElement(new GuiText(20, 183, "Clear", 0xff000000));
		
		rowText = new GuiRightAlignedText(233, 6, "", 0xff000000);
		recipeTab.addElement(rowText);
		
		scrollBar = 
			new GuiScrollBar(238, 6, 12, 186, 
				new GuiSlider(0, 21, 12, 144, 12, 15, guiTexture, 0, 199),
				new GuiValueButton[]{
    				new GuiValueButton(0,   0, 12, 11, guiTexture, 0, 234, -30),
    				new GuiValueButton(0,  11, 12, 10, guiTexture, 0, 214, -3 ),
    				new GuiValueButton(0, 165, 12, 10, guiTexture, 0, 224,  3 ),
    				new GuiValueButton(0, 175, 12, 11, guiTexture, 0, 245,  30)
				}
			);
		
		recipeTab.addElement(scrollBar);
		
		CraftingDisplay craftingDisplay = new CraftingDisplay(68, 18, 166, 174, scrollBar, recipeCache);
		FilterClearCallback clearCallback = new FilterClearCallback();
		clearButton.addButtonListener(clearCallback);
		clearCallback.display = craftingDisplay;

		recipeTab.addElement(craftingDisplay);

		recipeTab.addElement(new GuiImage(40, 157, 18, 18, guiTexture, 238, 219));
		filterStack = new GuiItemStack(41, 158, false);
		recipeTab.addElement(filterStack);
		
		return recipeTab;
	}

	private Object generateTypeTab(int windowWidth, int windowHeight, GuiTexture guiTexture)
	{
		GuiElement typeTab = new GuiElement(0, 0, windowWidth, windowHeight);

		typeTab.addElement(
			new GuiBorderedRect(
				67, 5, 168, 188,
				guiTexture, 78, 1, 2, 32
			)
		);
		
		typeTab.addElement(
			new GuiBorderedRect(
				237, 5, 14, 188,
				guiTexture, 78, 1, 2, 32
			)
		);
		
		GuiElement scrollBar = 
			new GuiScrollBar(238, 6, 12, 186, 
				new GuiSlider(0, 10, 12, 166, 12, 15, guiTexture, 0, 199),
				new GuiValueButton[]{
    				new GuiValueButton(0,   0, 12, 10, guiTexture, 0, 214, -3 ),
    				new GuiValueButton(0, 176, 12, 10, guiTexture, 0, 224,  3 ),
				}
			);
		
		typeTab.addElement(scrollBar);
		
		return typeTab;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		rowText.setText("Rows " + ((int)scrollBar.getValue() + 1) + "-" +  + ((int)scrollBar.getValue() + 3)  + " of " + ((int)scrollBar.getMax() + 3));
		guideWindow.centerOn(width / 2, height / 2);
		filterStack.setItem(recipeCache.getFilterItem());
		
		if(mod_CraftGuide.pauseWhileOpen)
		{
			drawDefaultBackground();
		}

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

	@Override
	public boolean doesGuiPauseGame()
	{
		return mod_CraftGuide.pauseWhileOpen;
	}
}
