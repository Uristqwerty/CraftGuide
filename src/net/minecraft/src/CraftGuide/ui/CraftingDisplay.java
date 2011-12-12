package net.minecraft.src.CraftGuide.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.CraftGuide.Recipe;
import net.minecraft.src.CraftGuide.RecipeCache;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.ui.Rendering.GridRect;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.ToolTip;

public class CraftingDisplay extends GuiScrollableGrid
{
	int mouseX;
	int mouseY;
	private ToolTip itemName = new ToolTip("-No Item-");
	RecipeCache recipeCache;
	
	public CraftingDisplay(int x, int y, int width, int height, GuiScrollBar scrollBar, RecipeCache recipeCache)
	{
		super(x, y, width, height, scrollBar, 58);

		this.recipeCache = recipeCache;
		
		updateScrollbarSize();
		
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
		mouseX = x;
		mouseY = y;
		super.mouseMoved(x, y);
	}

	/*@Override
	public void mousePressed(int x, int y)
	{
		ItemStack stack = itemStackUnderMouse();
		
		if(stack != null)
		{
			setFilter(stack);
		}
		
		super.mousePressed(x, y);
	}*/
	
	@Override
	public void rowClicked(int row, int x, int y)
	{
		ItemStack stack = itemStackUnderMouse(row, x, y);
		
		if(stack != null)
		{
			setFilter(stack);
		}
		
		super.rowClicked(row, x, y);
	}

	@Override
	public void mouseMovedRow(int row, int x, int y)
	{
		super.mouseMovedRow(row, x, y);
	}

	@Override
	public void renderGridRow(GuiRenderer renderer, int xOffset, int yOffset, int row)
	{
		List<ICraftGuideRecipe> recipes = recipeCache.getRecipes();
		
		if(row * 2 < recipes.size())
		{
			renderRecipe(renderer, xOffset, yOffset, (Recipe)recipes.get(row * 2));
		}
		
		if(row * 2 < recipes.size() - 1)
		{
			renderRecipe(renderer, xOffset + 87, yOffset, (Recipe)recipes.get(row * 2 + 1));
		}
	}

	private void renderRecipe(GuiRenderer renderer, int xOffset, int yOffset, Recipe recipe)
	{
		boolean selected = false;
		
		if(isMouseOver(mouseX, mouseY))
		{
			ICraftGuideRecipe selectedRecipe = recipeAt(mouseX - this.x, mouseY - this.y);
			if(recipe == selectedRecipe)
			{
				selected = true;
			}
		}
		
		recipe.draw(renderer, xOffset, yOffset, selected);
		
		if(selected)
		{
			DrawSelectionBox(renderer, recipe);
		}
	}

	public void setFilter(ItemStack filter)
	{
		recipeCache.filter(filter);
		updateScrollbarSize();
	}

	private void updateScrollbarSize()
	{
		setRows(recipeCache.getRecipes().size() / 2);
	}

	private Recipe recipeAt(int x, int y)
	{
		if(x <= 86 && x > 78)
		{
			return null;
		}
		
		int row = (int)(scrollBar.getValue() + y / (float)58);
		int index = row * 2 + (x > 86? 1 : 0);
		
		if(index < recipeCache.getRecipes().size() && index >= 0)
		{
			return (Recipe)recipeCache.getRecipes().get(index);
		}
		
		return null;
	}

	private Recipe recipeAt(int row, int x, int y)
	{
		if(x <= 86 && x > 78)
		{
			return null;
		}
		
		int index = row * 2 + (x > 86? 1 : 0);
		
		if(index < recipeCache.getRecipes().size() && index >= 0)
		{
			return (Recipe)recipeCache.getRecipes().get(index);
		}
		
		return null;
	}
	
	private void DrawSelectionBox(GuiRenderer renderer, Recipe recipe)
	{
		int x = (mouseX - this.x > 86? mouseX - this.x - 87 : mouseX - this.x);
		int y = (mouseY - this.y  + (int)((scrollBar.getValue() % 1) * 58)) % 58;
		
		if(recipe.getItemUnderMouse(x, y) != null)
		{
			IRenderable selection = recipe.getSelectionBox(x, y);
			
			if (selection != null)
			{
				render(selection, (mouseX - this.x) - x, (mouseY - this.y) - y);
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
	
	private List<String> itemText(ItemStack stack)
	{
		List list = stack.func_40712_q();
		List<String> text = new ArrayList<String>(list.size());
		boolean first = true;
		
		for(Object o: list)
		{
			if(o instanceof String)
			{
				if(first)
				{
					text.add("\247" + Integer.toHexString(stack.func_40707_s().field_40535_e) + (String)o);
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

	private ItemStack itemStackUnderMouse()
	{
		if(isMouseOver(mouseX, mouseY))
		{
			Recipe recipe = recipeAt(mouseX - x, mouseY - y);
			
			if(recipe != null)
			{
				int x = (mouseX - this.x > 86? mouseX - this.x - 86 : mouseX - this.x);
				int y = (mouseY - this.y  + (int)((scrollBar.getValue() % 1) * 58)) % 58;
				
				return recipe.getItemUnderMouse(x, y);
			}
		}
		
		return null;
	}

	private ItemStack itemStackUnderMouse(int row, int x, int y)
	{
		if(x >= 0 && x < width && y >= 0 && y < height)
		{
			Recipe recipe = recipeAt(row, x, y);

			if(recipe != null)
			{
				if(x > 86)
				{
					return recipe.getItemUnderMouse(x - 86, y);
				}
				else
				{
					return recipe.getItemUnderMouse(x, y);
				}
			}
		}
		
		return null;
	}
}
