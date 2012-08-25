package uristqwerty.CraftGuide.ui;

import java.util.ArrayList;
import java.util.List;

import uristqwerty.CraftGuide.CraftType;
import uristqwerty.CraftGuide.RecipeCache;
import uristqwerty.CraftGuide.ui.IButtonListener.Event;
import uristqwerty.CraftGuide.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.ui.Rendering.GuiTexture;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.minecraft.Image;

import net.minecraft.src.Item;
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
			recipeCache.filter(itemResults.get(cell));
			display.onButtonEvent(backButton, Event.PRESS);
		}
	}

	@Override
	public void mouseMovedCell(int cell, int x, int y, boolean inBounds)
	{
		if(inBounds && cell < itemResults.size())
		{
			overItem = true;
			itemName.setText(CraftingDisplay.itemText(displayItem(cell)));
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
	public void mouseMoved(int x, int y)
	{
		overItem = false;
		super.mouseMoved(x, y);
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
		}
		else
		{
			for(Object item: items)
			{
				ItemStack stack = ((CraftType)item).getDisplayStack();
				
				if(Item.itemsList[stack.itemID] != null)
				{
					try
					{
						String name = Item.itemsList[stack.itemID].getItemDisplayName(stack);
						
						if(name != null && name.toLowerCase().contains(text.toLowerCase()))
						{
							itemResults.add(stack);
						}
					}
					catch (Exception e)
					{
					}
				}
			}
		}

		setCells(itemResults.size());
	}


	@Override
	public void onChange(RecipeCache cache)
	{
	}

	@Override
	public void onSubmit(GuiTextInput input)
	{
	}

	@Override
	public void onTextChanged(GuiTextInput input)
	{
		search(input.getText());
	}
}
