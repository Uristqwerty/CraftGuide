package net.minecraft.src.CraftGuide.ui;

import java.util.List;

import net.minecraft.src.CraftGuide.Recipe;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe;
import net.minecraft.src.CraftGuide.ui.Rendering.GridRect;

public class GuiScrollableGrid extends GuiElement
{
	private GridRect display;
	/*private*/ GuiScrollBar scrollBar;
	private int rowHeight;
	private int rows = 0;
	
	public GuiScrollableGrid(int x, int y, int width, int height, GuiScrollBar scrollBar, int rowHeight)
	{
		super(x, y, width, height);
		this.display = new GridRect(0, 0, width, height, this);
		this.rowHeight = rowHeight;
		this.scrollBar = scrollBar;
	}

	@Override
	public void draw()
	{
		render(display);
		super.draw();
	}
	
	@Override
	public void mouseMoved(int x, int y)
	{
		int scrollY = (int)(scrollBar.getValue() * rowHeight) + y;
		int row = scrollY / rowHeight;
		
		mouseMovedRow(row, x, scrollY % rowHeight);
		
		super.mousePressed(x, y);
	}
	
	@Override
	public void mousePressed(int x, int y)
	{
		int scrollY = (int)(scrollBar.getValue() * rowHeight) + y - this.y;
		int row = scrollY / rowHeight;
		
		rowClicked(row, x - this.x, scrollY % rowHeight);
		super.mousePressed(x, y);
	}
	
	public void setRows(int rowCount)
	{
		rows = rowCount;
		
		float end = rows - height / (float)rowHeight;
		
		if(end < 0)
		{
			end = 0;
		}
		
		scrollBar.setScale(0, end);
	}

	public void renderGridRows(GuiRenderer renderer, int xOffset, int yOffset)
	{
		int scrollY = (int)(scrollBar.getValue() * rowHeight);
		int y = yOffset - (scrollY % rowHeight);
		int row = scrollY / rowHeight;
		int max = yOffset + height;
		
		for(; y < max; y += rowHeight, row++)
		{
			renderGridRow(renderer, xOffset, y, row);
		}
	}

	public void renderGridRow(GuiRenderer renderer, int xOffset, int yOffset, int row)
	{
	}
	
	public void rowClicked(int row, int x, int y)
	{
	}
	
	public void mouseMovedRow(int row, int x, int y)
	{
	}
}
