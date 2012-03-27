package net.minecraft.src.CraftGuide.ui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.Recipe;
import net.minecraft.src.CraftGuide.RecipeCache;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.FloatingItemText;

public class CraftingDisplay extends GuiScrollableGrid implements IRecipeCacheListener
{
	int mouseX;
	int mouseY;
	int mouseRow, mouseRowX, mouseRowY;
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	RecipeCache recipeCache;
	private Recipe recipeUnderMouse;
	
	public CraftingDisplay(int x, int y, int width, int height, GuiScrollBar scrollBar, RecipeCache recipeCache)
	{
		super(x, y, width, height, scrollBar, 58, 79);

		this.recipeCache = recipeCache;
		recipeCache.addListener(this);
		updateScrollbarSize();
		updateGridSize();
	}

	@Override
	public void draw()
	{
		super.draw();
		drawSelectionName();
	}

	@Override
	public void mouseMoved(int x, int y)
	{
		recipeUnderMouse = null;
		super.mouseMoved(x, y);
	}

	@Override
	public void renderGridCell(GuiRenderer renderer, int xOffset, int yOffset, int cell)
	{
		List<ICraftGuideRecipe> recipes = recipeCache.getRecipes();
		
		if(cell < recipes.size())
		{
			renderRecipe(renderer, xOffset, yOffset, (Recipe)recipes.get(cell));
		}
	}

	private void renderRecipe(GuiRenderer renderer, int xOffset, int yOffset, Recipe recipe)
	{
		recipe.draw(renderer, xOffset, yOffset, recipe == recipeUnderMouse);
		
		if(recipe == recipeUnderMouse)
		{
			DrawSelectionBox(renderer, xOffset, yOffset);
		}
	}

	public void setFilter(ItemStack filter)
	{
		recipeCache.filter(filter);
	}

	@Override
	public void onChange(RecipeCache cache)
	{
		updateScrollbarSize();
		updateGridSize();
	}
	
	private void updateGridSize()
	{
		List<ICraftGuideRecipe> recipes = recipeCache.getRecipes();
		int maxWidth = 16, maxHeight = 16;
		
		for(ICraftGuideRecipe recipe: recipes)
		{
			maxWidth = Math.max(maxWidth, ((Recipe)recipe).width());
			maxHeight = Math.max(maxHeight, ((Recipe)recipe).height());
		}
		
		setColumnWidth(maxWidth);
		setRowHeight(maxHeight);
	}

	private void updateScrollbarSize()
	{
		setCells(recipeCache.getRecipes().size());
	}

	@Override
	public void mouseMovedCell(int cell, int x, int y, boolean inBounds)
	{
		List<ICraftGuideRecipe> recipes = recipeCache.getRecipes();
		
		if(inBounds && cell < recipes.size())
		{
			Recipe recipe = (Recipe) recipes.get(cell);
			
			if(x < recipe.width() && y < recipe.height())
			{
				mouseX = x;
				mouseY = y;
				recipeUnderMouse = recipe;
			}
		}
	}
	
	private void DrawSelectionBox(GuiRenderer renderer, int xOffset, int yOffset)
	{
		if(itemStackUnderMouse() != null)
		{
			IRenderable selection = recipeUnderMouse.getSelectionBox(mouseX, mouseY);
			
			if (selection != null)
			{
				selection.render(renderer, xOffset, yOffset);
			}
		}
	}
	
	private void drawSelectionName()
	{
		ItemStack stack = itemStackUnderMouse();
		
		if(stack != null)
		{
			itemName.setText(itemText(stack));
			render(itemName);
		}
	}
	
	public static List<String> itemText(ItemStack stack)
	{
		if(stack.getItem() == null)
		{
			return itemTextErr(stack);
		}
		
		try
		{
			List list = stack.getItemNameandInformation();//.func_40712_q();
			List<String> text = new ArrayList<String>(list.size());
			boolean first = true;
			
			for(Object o: list)
			{
				if(o instanceof String)
				{
					if(first)
					{
						text.add("\247" + Integer.toHexString(stack.getRarity().field_40535_e) + (String)o);
						first = false;
					}
					else
					{
						text.add("\2477" + (String)o);
					}
				}
			}
			
			return text;
		}
		catch(Exception e)
		{
			List<String> text = new ArrayList<String>(1);
			text.add("\247" + Integer.toHexString(15) + "Item #" + Integer.toString(stack.itemID) + " data " + Integer.toString(stack.getItemDamage()));
			return text;
		}
	}

	private static List<String> itemTextErr(ItemStack stack)
	{
		List<String> text = new ArrayList<String>(1);
		text.add("\247" + Integer.toHexString(15) + "Error: Item #" + Integer.toString(stack.itemID) + " does not exist");
		return text;
	}

	private ItemStack itemStackUnderMouse()
	{
		if(recipeUnderMouse != null)
		{
			return recipeUnderMouse.getItemUnderMouse(mouseX, mouseY);
		}
		
		return null;
	}

	@Override
	public void cellClicked(int cell, int x, int y)
	{
		List<ICraftGuideRecipe> recipes = recipeCache.getRecipes();
		
		if(cell < recipes.size())
		{
			recipeClicked((Recipe)recipes.get(cell), x, y);
		}
	}

	private void recipeClicked(Recipe recipe, int x, int y)
	{
		ItemStack stack = recipe.getItemUnderMouse(x, y);
		
		if(stack != null)
		{
			setFilter(stack);
		}
	}

	@Override
	public void onReset(RecipeCache cache)
	{
	}
}
