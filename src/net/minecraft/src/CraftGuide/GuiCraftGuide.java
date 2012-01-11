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
import net.minecraft.src.CraftGuide.ui.CraftTypeDisplay;
import net.minecraft.src.CraftGuide.ui.CraftingDisplay;
import net.minecraft.src.CraftGuide.ui.FilterSelectGrid;
import net.minecraft.src.CraftGuide.ui.GuiBorderedRect;
import net.minecraft.src.CraftGuide.ui.GuiButton;
import net.minecraft.src.CraftGuide.ui.GuiElement;
import net.minecraft.src.CraftGuide.ui.GuiElement.AnchorPoint;
import net.minecraft.src.CraftGuide.ui.GuiImage;
import net.minecraft.src.CraftGuide.ui.GuiItemStack;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.GuiResizeHandle;
import net.minecraft.src.CraftGuide.ui.GuiRightAlignedText;
import net.minecraft.src.CraftGuide.ui.GuiSlider;
import net.minecraft.src.CraftGuide.ui.GuiScrollBar;
import net.minecraft.src.CraftGuide.ui.GuiTabbedDisplay;
import net.minecraft.src.CraftGuide.ui.GuiText;
import net.minecraft.src.CraftGuide.ui.GuiWindow;
import net.minecraft.src.CraftGuide.ui.IButtonListener;
import net.minecraft.src.CraftGuide.ui.RowCount;
import net.minecraft.src.CraftGuide.ui.GuiTextInput;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiSubTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.ITexture;
import net.minecraft.src.CraftGuide.ui.Rendering.ShadedRect;

public class GuiCraftGuide extends GuiScreen
{
	private GuiRenderer renderer = new GuiRenderer();
	private RecipeCache recipeCache = new RecipeCache();
	private GuiWindow guideWindow;
	private GuiRightAlignedText rowText;
	private GuiItemStack filterStack;
	private CraftingDisplay craftingDisplay;
	private int repeatKey;
	private char repeatChar;
	private long nextRepeat;

	private static GuiCraftGuide instance;

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
	
	public static GuiCraftGuide getInstance()
	{
		if(instance == null)
		{
			instance = new GuiCraftGuide();
		}
		
		return instance;
	}
	
	public GuiCraftGuide()
	{
		final int windowWidth = 256;
		final int windowHeight = 198;

		GuiTexture guiTexture = GuiTexture.getInstance("/gui/CraftGuide.png");

		guideWindow = new GuiWindow(0, 0, windowWidth, windowHeight, renderer);

		new GuiResizeHandle(windowWidth - 8, windowHeight - 8, 8, 8, guideWindow);
		
		guideWindow.addElement(
			new GuiBorderedRect(
				0, 0, windowWidth, windowHeight,
				guiTexture, 1, 1, 4, 64
			)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
		);
		
		guideWindow.addElement(
			new GuiButton(windowWidth - 8, windowHeight - 8, 8, 8, guiTexture, 0, 191)
				.anchor(AnchorPoint.BOTTOM_RIGHT)
			);
		
		guideWindow.addElement(
			new GuiBorderedRect(
				5, 5, 58, 86,
				guiTexture, 78, 1, 2, 32
			)
		);
		
		guideWindow.addElement(
			new GuiTabbedDisplay(0, 0, windowWidth, windowHeight)
				.addTab(
					generateRecipeTab(windowHeight, windowWidth, guiTexture)
						.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT),
					new GuiButton(6, 6, 28, 28, guiTexture, 1, 76)
						.setToolTip("Recipe list"))
				.addTab(
					generateTypeTab(windowHeight, windowWidth, guiTexture)
						.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT),
					new GuiButton(34, 6, 28, 28, guiTexture, 1, 104)
						.setToolTip("Show/Hide recipes by crafting type"))
				.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT));
	}

	private GuiElement generateRecipeTab(int windowWidth, int windowHeight, GuiTexture guiTexture)
	{
		GuiElement recipeTab = new GuiElement(0, 0, windowWidth, windowHeight);
		
		recipeTab.addElement(
			new GuiBorderedRect(
				237, 5, 14, 188,
				guiTexture, 78, 1, 2, 32
			)
			.anchor(AnchorPoint.TOP_RIGHT, AnchorPoint.BOTTOM_RIGHT)
		);

		GuiButton clearButton = 
			(GuiButton) new GuiButton(8, 180, 50, 13, guiTexture, 48, 204, 0, 13)
				.anchor(AnchorPoint.BOTTOM_LEFT);
		
		recipeTab.addElement(clearButton);
		
		recipeTab.addElement(
			new GuiText(9, 151, "Filter", 0xff000000)
				.anchor(AnchorPoint.BOTTOM_LEFT));
		
		recipeTab.addElement(
			new GuiText(20, 183, "Clear", 0xff000000)
				.anchor(AnchorPoint.BOTTOM_LEFT));

		recipeTab.addElement(
			new GuiImage(40, 146, 18, 18, guiTexture, 238, 219)
				.anchor(AnchorPoint.BOTTOM_LEFT));
		
		filterStack = new GuiItemStack(41, 147, false);
		filterStack.anchor(AnchorPoint.BOTTOM_LEFT);
		recipeTab.addElement(filterStack);
		
		GuiElement recipeArea = new GuiElement(0, 0, windowWidth, windowHeight)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT);
		GuiElement itemListArea = new GuiElement(0, 0, windowWidth, windowHeight)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT);

		recipeArea.addElement(
			new GuiBorderedRect(
				67, 17, 168, 176,
				guiTexture, 78, 1, 2, 32
			)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
		);
		
		itemListArea.addElement(
			new GuiBorderedRect(
				67, 17, 168, 160,
				guiTexture, 78, 1, 2, 32
			)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
		);

		GuiButton backButton =
			(GuiButton) new GuiButton(8, 166, 50, 13, guiTexture, 48, 204, 0, 13)
				.anchor(AnchorPoint.BOTTOM_LEFT)
				.addElement(
					new GuiText(13, 3, "Back", 0xff000000));
		
		GuiButton itemListButton = 
			(GuiButton) new GuiButton(8, 166, 50, 13, guiTexture, 48, 204, 0, 13)
				.anchor(AnchorPoint.BOTTOM_LEFT)
				.addElement(
					new GuiText(6, 3, "Set item", 0xff000000));
		
		itemListArea.addElement(backButton);
		recipeArea.addElement(itemListButton);

		GuiTabbedDisplay recipeDisplay = 
			(GuiTabbedDisplay) new GuiTabbedDisplay(0, 0, windowWidth, windowHeight)
				.addTab(recipeArea, backButton, false)
				.addTab(itemListArea, itemListButton, false)
				.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT);
		
		recipeTab.addElement(recipeDisplay);
		
		GuiScrollBar filterSelectScrollBar = 
			(GuiScrollBar) new GuiScrollBar(238, 6, 12, 186,
				(GuiSlider) new GuiSlider(0, 21, 12, 144, 12, 15, guiTexture, 0, 199)
					.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT))
				.addButton(new GuiButton(0,   0, 12, 11, guiTexture, 0, 234), -10, true)
				.addButton(new GuiButton(0,  11, 12, 10, guiTexture, 0, 214), -1, true)
				.addButton(
					(GuiButton)new GuiButton(0, 165, 12, 10, guiTexture, 0, 224)
						.anchor(AnchorPoint.BOTTOM_RIGHT),
					1, true)
				.addButton(
					(GuiButton)new GuiButton(0, 175, 12, 11, guiTexture, 0, 245)
						.anchor(AnchorPoint.BOTTOM_RIGHT),
					10, true)
				.anchor(AnchorPoint.TOP_RIGHT, AnchorPoint.BOTTOM_RIGHT);
		
		itemListArea.addElement(filterSelectScrollBar);
		
		FilterSelectGrid filterGrid = 
			(FilterSelectGrid) new FilterSelectGrid(68, 18, 166, 158, 
			filterSelectScrollBar, guiTexture,
			recipeCache, (GuiButton)backButton, recipeDisplay)
				.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT);
		
		itemListArea.addElement(new RowCount(233, 6, filterGrid).anchor(AnchorPoint.TOP_RIGHT));
		itemListArea.addElement(filterGrid);

		itemListArea.addElement(
			new GuiText(68, 183, "Search", 0xff000000)
				.anchor(AnchorPoint.BOTTOM_LEFT));
		
		GuiTextInput searchInput = 
			(GuiTextInput) new GuiTextInput(0, 0, 129, 15, 2, 2)
				.addListener(filterGrid)
				.anchor(AnchorPoint.BOTTOM_LEFT, AnchorPoint.BOTTOM_RIGHT);
		
		itemListButton.addButtonListener(searchInput);
		
		itemListArea.addElement(
			new GuiBorderedRect(106, 179, 129, 15, guiTexture, 78, 1, 2, 32)
				.anchor(AnchorPoint.BOTTOM_LEFT, AnchorPoint.BOTTOM_RIGHT)
				.addElement(searchInput)
		);
		
		GuiScrollBar scrollBar = 
			(GuiScrollBar) new GuiScrollBar(238, 6, 12, 186, 
				(GuiSlider) new GuiSlider(0, 21, 12, 144, 12, 15, guiTexture, 0, 199)
					.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT))
				.addButton(new GuiButton(0,   0, 12, 11, guiTexture, 0, 234), -10, true)
				.addButton(new GuiButton(0,  11, 12, 10, guiTexture, 0, 214), -1, true)
				.addButton(
					(GuiButton)new GuiButton(0, 165, 12, 10, guiTexture, 0, 224)
						.anchor(AnchorPoint.BOTTOM_RIGHT),
					1, true)
				.addButton(
					(GuiButton)new GuiButton(0, 175, 12, 11, guiTexture, 0, 245)
						.anchor(AnchorPoint.BOTTOM_RIGHT),
					10, true)
				.anchor(AnchorPoint.TOP_RIGHT, AnchorPoint.BOTTOM_RIGHT);
		
		recipeArea.addElement(scrollBar);
		
		craftingDisplay = new CraftingDisplay(68, 18, 166, 174, scrollBar, recipeCache);
		recipeArea.addElement(new RowCount(233, 6, craftingDisplay).anchor(AnchorPoint.TOP_RIGHT));
		FilterClearCallback clearCallback = new FilterClearCallback();
		clearButton.addButtonListener(clearCallback);
		clearCallback.display = craftingDisplay;

		craftingDisplay.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT);
		recipeArea.addElement(craftingDisplay);
		
		return recipeTab;
	}

	private GuiElement generateTypeTab(int windowWidth, int windowHeight, GuiTexture guiTexture)
	{
		GuiElement typeTab = new GuiElement(0, 0, windowWidth, windowHeight);
		typeTab.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT);

		typeTab.addElement(
			new GuiBorderedRect(
				67, 5, 168, 188,
				guiTexture, 78, 1, 2, 32
			)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
		);
		
		typeTab.addElement(
			new GuiBorderedRect(
				237, 5, 14, 188,
				guiTexture, 78, 1, 2, 32
			)
			.anchor(AnchorPoint.TOP_RIGHT, AnchorPoint.BOTTOM_RIGHT)
		);
		
		GuiScrollBar scrollBar = 
			new GuiScrollBar(238, 6, 12, 186, 
				(GuiSlider) new GuiSlider(0, 10, 12, 166, 12, 15, guiTexture, 0, 199)
					.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT))
				.addButton(new GuiButton(0, 0, 12, 10, guiTexture, 0, 214), -1, true)
				.addButton(
					(GuiButton) new GuiButton(0, 176, 12, 10, guiTexture, 0, 224)
						.anchor(AnchorPoint.BOTTOM_RIGHT),
					1, true);

		scrollBar.anchor(AnchorPoint.TOP_RIGHT, AnchorPoint.BOTTOM_RIGHT);
		typeTab.addElement(scrollBar);
		
		typeTab.addElement(
			new CraftTypeDisplay(
				68, 6, 166, 186, scrollBar, guiTexture, recipeCache
			)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
		);
		
		return typeTab;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f)
	{
		guideWindow.centerOn(width / 2, height / 2);
		guideWindow.setMaxSize(width, height);
		filterStack.setItem(recipeCache.getFilterItem());
		
		if(nextRepeat != 0 && System.currentTimeMillis() > nextRepeat)
		{
			nextRepeat = System.currentTimeMillis() + mod_CraftGuide.keyboardRepeatRate;
			keyRepeat(repeatChar, repeatKey);
		}
		
		if(mod_CraftGuide.pauseWhileOpen)
		{
			drawDefaultBackground();
		}

		renderer.startFrame(mc, this);
		guideWindow.draw();
		renderer.endFrame();
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
        	guideWindow.onKeyTyped(eventChar, eventKey);
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
        	guideWindow.onKeyTyped(eventChar, eventKey);
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
        	guideWindow.updateMouseState(x, y, Mouse.getEventButtonState());
        }
        else if(Mouse.getEventButton() == -1)
        {
        	guideWindow.updateMouse(x, y);
		}
        
    	if(Mouse.getEventDWheel() != 0)
    	{
    		guideWindow.scrollWheelTurned(Mouse.getEventDWheel() > 0? -mod_CraftGuide.mouseWheelScrollRate : mod_CraftGuide.mouseWheelScrollRate);
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			GuiScrollBar.setScrollMultiplier(10);
		}
		else
		{
			GuiScrollBar.setScrollMultiplier(1);
		}
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return mod_CraftGuide.pauseWhileOpen;
	}
}
