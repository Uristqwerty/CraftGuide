package net.minecraft.src.CraftGuide.ui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.CraftType;
import net.minecraft.src.CraftGuide.Recipe;
import net.minecraft.src.CraftGuide.RecipeCache;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.ui.IButtonListener.Event;
import net.minecraft.src.CraftGuide.ui.Rendering.FloatingItemText;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class FilterSelectGrid extends GuiScrollableGrid implements IRecipeCacheListener, ITextInputListener
{
	private GuiButton backButton;
	private GuiTabbedDisplay display;
	private RecipeCache recipeCache;
	private IRenderable background;
	private Object[] items;
	private List<ItemStack> itemResults = new ArrayList<ItemStack>();
	private static IRenderable overlayAny = new TexturedRect(
		-1, -1, 18, 18, GuiTexture.getInstance("/gui/CraftGuide.png"), 238, 238);
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	private boolean overItem = false;
	private String searchText = "";
	
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

		background = new TexturedRect(0, 0, 18, 18, guiTexture, 238, 219);
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
			itemName.setText(CraftingDisplay.itemText(itemResults.get(cell)));
		}
	}

	@Override
	public void renderGridCell(GuiRenderer renderer, int xOffset, int yOffset, int cell)
	{
		if(cell < itemResults.size())
		{
			background.render(renderer, xOffset, yOffset);
			ItemStack stack = itemResults.get(cell);
			
			renderer.drawItemStack(stack, xOffset + 1, yOffset + 1);
			
			if(stack.getItemDamage() == -1)
			{
				overlayAny.render(renderer, xOffset, yOffset);
			}
		}
	}

	@Override
	public void draw()
	{
		super.draw();
		
		if(overItem)
		{
			render(itemName);
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
				ItemStack stack = ((CraftType)item).getStack();
				if(Item.itemsList[stack.itemID] != null)
				{
					String name = Item.itemsList[stack.itemID].getItemDisplayName(stack);
					
					if(name.toLowerCase().contains(text.toLowerCase()))
					{
						itemResults.add(stack);
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
