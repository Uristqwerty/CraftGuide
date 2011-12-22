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
	private int rows = 0, columns = 1, cells = 0;
	private int columnWidth = 1;
	
	public GuiScrollableGrid(int x, int y, int width, int height, GuiScrollBar scrollBar, int rowHeight, int columnWidth)
	{
		super(x, y, width, height);
		this.display = new GridRect(0, 0, width, height, this);
		this.rowHeight = rowHeight;
		this.scrollBar = scrollBar;
		this.columnWidth = columnWidth;
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
		
		rowClicked(row, x - this.x, scrollY % rowHeight, isMouseOver(x, y));
		super.mousePressed(x, y);
	}
	
	@Override
	public void onResize(int oldWidth, int oldHeight)
	{
		display.setSize(width, height);
		setRows(rows);
		
		super.onResize(oldWidth, oldHeight);
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
	
	public void setColumns(int columns)
	{
		this.columns = columns;
		setRows((cells + columns - 1) / columns);
	}
	
	public void setCells(int cells)
	{
		this.cells = cells;
		setRows((cells + columns - 1) / columns);
	}

	public void renderGridRows(GuiRenderer renderer, int xOffset, int yOffset)
	{
		int scrollY = (int)(scrollBar.getValue() * rowHeight);
		int y = yOffset - (scrollY % rowHeight);
		int row = scrollY / rowHeight;
		int max = yOffset + height;
		
		while(y < max && row < rows)
		{
			renderGridRow(renderer, xOffset, y, row);
			y += rowHeight;
			row++;
		}
	}

	public void renderGridRow(GuiRenderer renderer, int xOffset, int yOffset, int row)
	{
	}
	
	public void rowClicked(int row, int x, int y, boolean inBounds)
	{
	}
	
	public void mouseMovedRow(int row, int x, int y)
	{
	}
}
