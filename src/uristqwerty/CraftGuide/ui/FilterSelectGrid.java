package uristqwerty.CraftGuide.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftType;
import uristqwerty.CraftGuide.RecipeCache;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.ui.Rendering.GuiTexture;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.minecraft.Image;

import net.minecraft.src.ItemStack;

public class FilterSelectGrid extends GuiScrollableGrid implements IRecipeCacheListener, ITextInputListener
{
	private GuiButton backButton;
	private GuiTabbedDisplay display;
	private RecipeCache recipeCache;
	private IRenderable background;
	private Object[] items;
	private List<Object> itemResults = new ArrayList<Object>();
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	private IRenderable itemNameOverlay = new Overlay(itemName);
	private boolean overItem = false;
	private String searchText = "";
	
	private int lastMouseX, lastMouseY;
	
	private NamedTexture textImage = Util.instance.getTexture("TextFilter");
	
	private static IRenderable overlayAny = new TexturedRect(
			0, 0, 18, 18, Image.getImage("/gui/CraftGuide.png"), 238, 238);
		private static IRenderable overlayForge = new TexturedRect(
			0, 0, 18, 18, Image.getImage("/gui/CraftGuide.png"), 238, 181);
	
	public FilterSelectGrid(int x, int y, int width, int height, GuiScrollBar scrollBar, GuiTexture guiTexture,
		RecipeCache recipeCache, GuiButton backButton, GuiTabbedDisplay display)
	{
		super(x, y, width, height, scrollBar, 18, 18);
		
		this.backButton = backButton;
		this.display = display;
		this.recipeCache = recipeCache;
		recipeCache.addListener(this);
		setColumns();
		onReset(recipeCache);

		background = new TexturedRect(0, 0, 18, 18, guiTexture.texture(), 238, 219);
	}

	@Override
	public void cellClicked(int cell, int x, int y)
	{
		if(cell < itemResults.size())
		{
			recipeCache.filter(Util.instance.getCommonFilter(itemResults.get(cell)));
			display.openTab(backButton);
		}
		else if(cell == itemResults.size() && searchText != null && !searchText.isEmpty())
		{
			recipeCache.filter(Util.instance.getCommonFilter(searchText));
			display.openTab(backButton);
		}
	}

	@Override
	public void mouseMoved(int x, int y)
	{
		overItem = false;
		lastMouseX = x;
		lastMouseY = y;
		super.mouseMoved(x, y);
	}

	@Override
	public void mouseMovedCell(int cell, int x, int y, boolean inBounds)
	{
		if(inBounds)
		{
			if(cell < itemResults.size())
			{
				overItem = true;
				itemName.setText(Util.instance.getItemStackText(displayItem(cell)));
			}
			else if(cell == itemResults.size() && searchText != null && !searchText.isEmpty())
			{
				overItem = true;
				itemName.setText("\u00a77Text search: '" + searchText + "'");
			}
		}
	}

	@Override
	public void renderGridCell(GuiRenderer renderer, int xOffset, int yOffset, int cell)
	{
		if(cell < itemResults.size())
		{
			background.render(renderer, xOffset, yOffset);
			ItemStack stack = displayItem(cell);
			
			renderer.drawItemStack(stack, xOffset + 1, yOffset + 1);
			
			if(stack.getItemDamage() == -1)
			{
				overlayAny.render(renderer, xOffset, yOffset);
			}
			
			if(itemResults.get(cell) instanceof ArrayList)
			{
				overlayForge.render(renderer, xOffset, yOffset);
			}
		}
		else if(cell == itemResults.size() && searchText != null && !searchText.isEmpty())
		{
			background.render(renderer, xOffset, yOffset);
			renderer.renderRect(xOffset + 1, yOffset + 1, 16, 16, textImage);
		}
	}

	private ItemStack displayItem(int cell)
	{
		Object item = itemResults.get(cell);
		
		if(item instanceof ItemStack)
		{
			return (ItemStack)item;
		}
		else if(item instanceof ArrayList && ((ArrayList)item).size() > 0)
		{
			return (ItemStack)((ArrayList)item).get(0);
		}
		else
		{
			return null;
		}
	}

	@Override
	public void draw()
	{
		super.draw();
		
		if(overItem)
		{
			render(itemNameOverlay);
		}
	}

	@Override
	public void onReset(RecipeCache cache)
	{
		items = cache.getAllItems().toArray();
		search(searchText);
	}
	
	public void search(String text)
	{
		searchText = text;
		itemResults.clear();
		
		if(text == null || text.isEmpty())
		{
			for(Object item: items)
			{
				itemResults.add(((CraftType)item).getStack());
			}
			
			setCells(itemResults.size());
		}
		else
		{
			for(Object item: items)
			{
				ItemStack stack = ((CraftType)item).getDisplayStack();
				
				try
				{
					for(String line: (List<String>)stack.getItemNameandInformation())
					{
						if(line != null && line.toLowerCase().contains(text.toLowerCase()))
						{
							itemResults.add(stack);
						}
					}
				}
				catch (Exception e)
				{
				}
			}
			
			setCells(itemResults.size() + 1);
		}
		
		mouseMoved(lastMouseX, lastMouseY);
	}


	@Override
	public void onChange(RecipeCache cache)
	{
	}

	@Override
	public void onSubmit(GuiTextInput input)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || CraftGuide.textSearchRequiresShift == false)
		{
			recipeCache.filter(Util.instance.getCommonFilter(input.getText()));
			display.openTab(backButton);
		}
	}

	@Override
	public void onTextChanged(GuiTextInput input)
	{
		search(input.getText());
	}
}
