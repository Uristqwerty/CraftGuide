package uristqwerty.CraftGuide;


import org.lwjgl.input.Keyboard;

import uristqwerty.CraftGuide.ui.ButtonTemplate;
import uristqwerty.CraftGuide.ui.CraftTypeDisplay;
import uristqwerty.CraftGuide.ui.CraftingDisplay;
import uristqwerty.CraftGuide.ui.FilterSelectGrid;
import uristqwerty.CraftGuide.ui.GuiBorderedRect;
import uristqwerty.CraftGuide.ui.GuiButton;
import uristqwerty.CraftGuide.ui.GuiElement;
import uristqwerty.CraftGuide.ui.GuiImage;
import uristqwerty.CraftGuide.ui.GuiItemStack;
import uristqwerty.CraftGuide.ui.GuiRenderer;
import uristqwerty.CraftGuide.ui.GuiResizeHandle;
import uristqwerty.CraftGuide.ui.GuiRightAlignedText;
import uristqwerty.CraftGuide.ui.GuiScrollBar;
import uristqwerty.CraftGuide.ui.GuiSlider;
import uristqwerty.CraftGuide.ui.GuiTabbedDisplay;
import uristqwerty.CraftGuide.ui.GuiText;
import uristqwerty.CraftGuide.ui.GuiTextInput;
import uristqwerty.CraftGuide.ui.GuiWindow;
import uristqwerty.CraftGuide.ui.IButtonListener;
import uristqwerty.CraftGuide.ui.RowCount;
import uristqwerty.CraftGuide.ui.GuiElement.AnchorPoint;
import uristqwerty.CraftGuide.ui.Rendering.GuiTexture;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;

public class GuiCraftGuide extends Gui
{
	private RecipeCache recipeCache = new RecipeCache();
	private GuiRightAlignedText rowText;
	private GuiItemStack filterStack;
	private CraftingDisplay craftingDisplay;
	private GuiWindow guiOverlay;

	private static GuiCraftGuide instance;
	
	public static GuiCraftGuide getInstance()
	{
		if(instance == null)
		{
			instance = new GuiCraftGuide();
		}
		
		return instance;
	}

	public void setFilterItem(ItemStack item)
	{
		recipeCache.filter(item.copy());
	}
	
	private static final int initialWindowWidth = 256;
	private static final int initialWindowHeight = 198;
	
	public GuiCraftGuide()
	{
		super(initialWindowWidth, initialWindowHeight);

		GuiTexture guiTexture = GuiTexture.getInstance("/gui/CraftGuide.png");
		
		ButtonTemplate buttonTemplate = new ButtonTemplate(guiTexture, 48, 204, 50, 13, 0, 13);


		new GuiResizeHandle(initialWindowWidth - 8, initialWindowHeight - 8, 8, 8, guiWindow);
		
		guiWindow.addElement(
			new GuiBorderedRect(
				0, 0, initialWindowWidth, initialWindowHeight,
				guiTexture, 1, 1, 4, 64
			)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
		);
		
		guiWindow.addElement(
			new GuiButton(initialWindowWidth - 8, initialWindowHeight - 8, 8, 8, guiTexture, 0, 191)
				.anchor(AnchorPoint.BOTTOM_RIGHT)
			);
		
		guiWindow.addElement(
			new GuiBorderedRect(
				5, 5, 58, 86,
				guiTexture, 78, 1, 2, 32
			)
		);
		
		guiWindow.addElement(
			new GuiTabbedDisplay(0, 0, initialWindowWidth, initialWindowHeight)
				.addTab(
					generateRecipeTab(guiTexture, buttonTemplate)
						.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT),
					new GuiButton(6, 6, 28, 28, guiTexture, 1, 76)
						.setToolTip("Recipe list"))
				.addTab(
					generateTypeTab(guiTexture, buttonTemplate)
						.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT),
					new GuiButton(34, 6, 28, 28, guiTexture, 1, 104)
						.setToolTip("Show/Hide recipes by crafting type"))
				.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT));
		

		guiOverlay = new GuiWindow(0, 0, 0, 0, new GuiRenderer());
		
		guiOverlay.addElement(
			new GuiBorderedRect(
				0, 0, 0, 0,
				guiTexture, 78, 1, 2, 32
			)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
		);
	}

	private GuiElement generateRecipeTab(GuiTexture guiTexture, ButtonTemplate buttonTemplate)
	{
		GuiElement recipeTab = new GuiElement(0, 0, initialWindowWidth, initialWindowHeight);
		
		recipeTab.addElement(
			new GuiBorderedRect(
				237, 5, 14, 188,
				guiTexture, 78, 1, 2, 32
			)
			.anchor(AnchorPoint.TOP_RIGHT, AnchorPoint.BOTTOM_RIGHT)
		);

		GuiButton clearButton = 
			(GuiButton) new GuiButton(8, 180, 50, 13, buttonTemplate, "Clear")
				.anchor(AnchorPoint.BOTTOM_LEFT);
		
		recipeTab.addElement(clearButton);
		
		recipeTab.addElement(
			new GuiText(9, 151, "Filter", 0xff000000)
				.anchor(AnchorPoint.BOTTOM_LEFT));

		recipeTab.addElement(
			new GuiImage(40, 146, 18, 18, guiTexture, 238, 219)
				.anchor(AnchorPoint.BOTTOM_LEFT));
		
		filterStack = new GuiItemStack(41, 147, false);
		filterStack.anchor(AnchorPoint.BOTTOM_LEFT);
		recipeTab.addElement(filterStack);
		
		GuiElement recipeArea = new GuiElement(0, 0, initialWindowWidth, initialWindowHeight)
			.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT);
		GuiElement itemListArea = new GuiElement(0, 0, initialWindowWidth, initialWindowHeight)
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
			(GuiButton) new GuiButton(8, 166, 50, 13, buttonTemplate, "Back")
				.anchor(AnchorPoint.BOTTOM_LEFT);
		
		GuiButton itemListButton = 
			(GuiButton) new GuiButton(8, 166, 50, 13, buttonTemplate, "Set item")
				.anchor(AnchorPoint.BOTTOM_LEFT);
		
		itemListArea.addElement(backButton);
		recipeArea.addElement(itemListButton);

		GuiTabbedDisplay recipeDisplay = 
			(GuiTabbedDisplay) new GuiTabbedDisplay(0, 0, initialWindowWidth, initialWindowHeight)
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
			(GuiTextInput) new GuiTextInput(0, 0, 93, 15, 2, 2)
				.addListener(filterGrid)
				.anchor(AnchorPoint.BOTTOM_LEFT, AnchorPoint.BOTTOM_RIGHT);
		
		itemListButton.addButtonListener(searchInput);
		
		itemListArea.addElement(
			new GuiBorderedRect(106, 179, 93, 15, guiTexture, 78, 1, 2, 32)
				.anchor(AnchorPoint.BOTTOM_LEFT, AnchorPoint.BOTTOM_RIGHT)
				.addElement(searchInput)
		);
		
		class ClearButtonListener implements IButtonListener
		{
			private GuiTextInput textInput;
			public ClearButtonListener(GuiTextInput textInput)
			{
				this.textInput = textInput;
			}
			public void onButtonEvent(GuiButton button, Event eventType)
			{
				textInput.setText("");
			}
		}
		
		itemListArea.addElement(
			new GuiButton(202, 180, 32, 13, buttonTemplate, "Clear")
				.addButtonListener(new ClearButtonListener(searchInput))
				.anchor(AnchorPoint.BOTTOM_RIGHT));
		
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

	private GuiElement generateTypeTab(GuiTexture guiTexture, ButtonTemplate buttonTemplate)
	{
		GuiElement typeTab = new GuiElement(0, 0, initialWindowWidth, initialWindowHeight);
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
		try
		{
			guiWindow.centerOn(width / 2, height / 2);
			
			if(recipeCache.getFilterItem() != null)
			{
				filterStack.setItem(recipeCache.getFilterItem().getItem());
			}
			else
			{
				filterStack.setItem(null);
			}
			
			super.drawScreen(mouseX, mouseY, f);
		}
		catch(Exception e)
		{
			CraftGuideLog.log(e);
		}
		catch(Error e)
		{
			CraftGuideLog.log(e);
			throw e;
		}
	}

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

	@Override
	public void handleKeyboardInput()
	{
		try
		{
			super.handleKeyboardInput();
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				GuiScrollBar.setScrollMultiplier(10);
			}
			else
			{
				GuiScrollBar.setScrollMultiplier(1);
			}
		}
		catch(Exception e)
		{
			CraftGuideLog.log(e);
		}
		catch(Error e)
		{
			CraftGuideLog.log(e);
			throw e;
		}
	}

	@Override
	public void handleMouseInput()
	{
		try
		{
			super.handleMouseInput();

		}
		catch(Exception e)
		{
			CraftGuideLog.log(e);
		}
		catch(Error e)
		{
			CraftGuideLog.log(e);
			throw e;
		}
	}

	@Override
	public int mouseWheelRate()
	{
		return CraftGuide.mouseWheelScrollRate;
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return CraftGuide.pauseWhileOpen;
	}
	
	public void reloadRecipes()
	{
		recipeCache.reset();
	}

	public static void onTickInGame(float f, Minecraft minecraft)
	{
		if(instance != null)
		{
			instance.drawOverlay(f, minecraft);
		}
	}
	
	public void drawOverlay(float f, Minecraft minecraft)
	{
		renderer.startFrame(mc, this);
		guiOverlay.draw();
		renderer.endFrame();
	}
	
	@Override
	public void onGuiClosed()
	{
        Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	public void initGui()
	{
		super.initGui();
        Keyboard.enableRepeatEvents(true);
	}
}
