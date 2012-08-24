package uristqwerty.CraftGuide.ui;

import java.util.ArrayList;
import java.util.List;

import uristqwerty.CraftGuide.Recipe;
import uristqwerty.CraftGuide.RecipeCache;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.Recipe.SlotReason;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;

import net.minecraft.src.ItemStack;

public class CraftingDisplay extends GuiScrollableGrid implements IRecipeCacheListener
{
	int mouseX;
	int mouseY;
	int mouseRow, mouseRowX, mouseRowY;
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	private IRenderable itemNameOverlay = new Overlay(itemName);
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

	public void setFilter(Object filter)
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
		IRenderable selection = recipeUnderMouse.getSelectionBox(mouseX, mouseY);
		
		if (selection != null)
		{
			selection.render(renderer, xOffset, yOffset);
		}
	}
	
	private void drawSelectionName()
	{
		ItemStack stack = itemStackUnderMouse();
		
		if(stack != null)
		{
			itemName.setText(itemText(stack));
			render(itemNameOverlay);
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
			List list = stack.getItemNameandInformation();
			
			if(stack.getItemDamage() == -1 && (list.size() < 1 || (list.size() == 1 && (list.get(0) == null || (list.get(0) instanceof String && ((String)list.get(0)).isEmpty())))))
			{
				list = GuiRenderer.fixedItemStack(stack).getItemNameandInformation();
			}
			
			List<String> text = new ArrayList<String>(list.size());
			boolean first = true;
			
			for(Object o: list)
			{
				if(o instanceof String)
				{
					if(first)
					{
						text.add("\u00a7" + Integer.toHexString(stack.getRarity().rarityColor) + (String)o);
						
						if(CraftGuide.alwaysShowID)
						{
							text.add("\u00a77" + "ID: " + stack.itemID + "; data: " + stack.getItemDamage());
						}
						
						first = false;
					}
					else
					{
						text.add("\u00a77" + (String)o);
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
			return recipeUnderMouse.getItemStackUnderMouse(mouseX, mouseY, SlotReason.REASON_NAME);
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
		Object stack = recipe.getItemUnderMouse(x, y, SlotReason.REASON_CLICK);
		
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
