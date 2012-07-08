package uristqwerty.CraftGuide.ui;

public class RowCount extends GuiElement
{
	GuiScrollableGrid grid;
	GuiRightAlignedText text;
	
	public RowCount(int x, int y, GuiScrollableGrid grid)
	{
		super(x, y, 0, 0);
		
		text = new GuiRightAlignedText(0, 0, "", 0x000000);
		addElement(text);
		this.grid = grid;
	}

	@Override
	public void draw()
	{
		text.setText("Rows " + (grid.firstVisibleRow() + 1) + "-" +  + (grid.lastVisibleRow())  + " of " + (grid.rowCount()));
		super.draw();
	}
}
