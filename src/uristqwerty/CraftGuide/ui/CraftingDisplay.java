package uristqwerty.CraftGuide.ui;

import java.util.List;

import uristqwerty.CraftGuide.RecipeCache;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IItemFilter;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRenderer;
import uristqwerty.CraftGuide.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;

public class CraftingDisplay extends GuiScrollableGrid implements IRecipeCacheListener
{
	int mouseX;
	int mouseY;
	int mouseRow, mouseRowX, mouseRowY;
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	private IRenderable itemNameOverlay = new Overlay(itemName);
	private RecipeCache recipeCache;
	private ICraftGuideRecipe recipeUnderMouse;
	
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
			renderRecipe(renderer, xOffset, yOffset, recipes.get(cell));
		}
	}

	private void renderRecipe(IRenderer renderer, int xOffset, int yOffset, ICraftGuideRecipe recipe)
	{
		if(recipe == recipeUnderMouse)
		{
			recipe.draw(renderer, xOffset, yOffset, true, mouseX, mouseY);
		}
		else
		{
			recipe.draw(renderer, xOffset, yOffset, false, -1, -1);
		}
	}

	public void setFilter(IItemFilter filter)
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
			maxWidth = Math.max(maxWidth, recipe.width());
			maxHeight = Math.max(maxHeight, recipe.height());
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
			ICraftGuideRecipe recipe = recipes.get(cell);
			
			if(x < recipe.width() && y < recipe.height())
			{
				mouseX = x;
				mouseY = y;
				recipeUnderMouse = recipe;
			}
		}
	}
	
	private void drawSelectionName()
	{
		if(recipeUnderMouse != null)
		{
			List<String> text = recipeUnderMouse.getItemText(mouseX, mouseY);
			
			if(text != null)
			{
				itemName.setText(text);
				render(itemNameOverlay);
			}
		}
	}

	@Override
	public void cellClicked(int cell, int x, int y)
	{
		List<ICraftGuideRecipe> recipes = recipeCache.getRecipes();
		
		if(cell < recipes.size())
		{
			recipeClicked(recipes.get(cell), x, y);
		}
	}

	private void recipeClicked(ICraftGuideRecipe recipe, int x, int y)
	{
		IItemFilter stack = recipe.getRecipeClickedResult(x, y);
		
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
