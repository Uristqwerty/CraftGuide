package uristqwerty.CraftGuide.ui;

import uristqwerty.CraftGuide.mod_CraftGuide;
import uristqwerty.CraftGuide.ui.Rendering.GridRect;

public class GuiScrollableGrid extends GuiElement
{
	private GridRect display;
	private GuiScrollBar scrollBar;
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
		
		scrollBar.setPageSize(height / rowHeight);
		
		recalculateColumns();
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
		int scrollY = (int)(scrollBar.getValue() * rowHeight) + y - this.y;
		int row = scrollY / rowHeight;
		
		mouseMovedRow(row, x - this.x, scrollY % rowHeight, isMouseOver(x, y));
		
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
		scrollBar.setPageSize(height / rowHeight);
		
		recalculateColumns();
		
		super.onResize(oldWidth, oldHeight);
	}
	
	public void recalculateColumns()
	{
		setColumns(Math.max(width / columnWidth, 1));
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
	
	public void setColumns()
	{
		setColumns(width / columnWidth);
	}
	
	public void setColumnWidth(int newWidth)
	{
		columnWidth = newWidth;
		recalculateColumns();
	}
	
	public void setRowHeight(int newHeight)
	{
		rowHeight = newHeight;
		setRows(rows);
		scrollBar.setPageSize(height / rowHeight);
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
		
		while(y < max && row < rowCount())
		{
			renderGridRow(renderer, xOffset, y, row);
			y += rowHeight;
			row++;
		}
	}

	public void renderGridRow(GuiRenderer renderer, int xOffset, int yOffset, int row)
	{
		for(int i = 0; i < columns && row * columns + i < cells; i++)
		{
			int columnX = columnOffset(i);
			
			renderGridCell(renderer, xOffset + columnX, yOffset, row * columns + i);
		}
	}

	private int columnOffset(int column)
	{
		if(mod_CraftGuide.gridPacking)
		{
			return column * columnWidth;
		}
		else
		{
			return columns < 2? 0 : (int)((width - columnWidth) * column / (float)(columns - 1));
		}
	}
	
	private int columnAtX(int x)
	{
		if(mod_CraftGuide.gridPacking)
		{
			return Math.min(x / columnWidth, columns - 1);
		}
		else
		{
			return (x * columns) / width;
		}
	}

	public void rowClicked(int row, int x, int y, boolean inBounds)
	{
		int column = columnAtX(x);
		int columnX = columnOffset(column);
		
		if(inBounds && x - columnX < columnWidth && row * columns + column < cells)
		{
			cellClicked(row * columns + column, x - columnX, y);
		}
	}

	public void mouseMovedRow(int row, int x, int y, boolean inBounds)
	{
		int column = columnAtX(x);
		
		if(column >= 0 && row * columns + column < cells)
		{
			int columnX = columnOffset(column);
			
			if(x >= columnX && x - columnX < columnWidth)
			{
				mouseMovedCell(row * columns + column, x - columnX, y, inBounds);
			}
		}
	}

	public int rowCount()
	{
		return rows;
	}

	public int firstVisibleRow()
	{
		return (int)(scrollBar.getValue());
	}

	public int lastVisibleRow()
	{
		return Math.min((int)(scrollBar.getValue() + (height + rowHeight - 1) / (float)rowHeight), rows);
	}
	
	public void mouseMovedCell(int cell, int x, int y, boolean inBounds)
	{
	}

	public void cellClicked(int cell, int x, int y)
	{
	}
	
	public void renderGridCell(GuiRenderer renderer, int xOffset, int yOffset, int cell)
	{
	}
}
