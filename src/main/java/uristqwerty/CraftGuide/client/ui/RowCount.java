package uristqwerty.CraftGuide.client.ui;

import uristqwerty.CraftGuide.RecipeCache;
import uristqwerty.CraftGuide.client.ui.text.TranslatedTextSource;
import uristqwerty.gui_craftguide.components.GuiElement;
import uristqwerty.gui_craftguide.texture.DynamicTexture;
import uristqwerty.gui_craftguide.texture.Texture;

public class RowCount extends GuiElement
{
	GuiScrollableGrid grid;
	GuiRightAlignedText text;
	GuiElement spinner;
	Texture spinnerBackground = DynamicTexture.instance("spinner");

	public RowCount(int x, int y, GuiScrollableGrid grid)
	{
		super(x, y, 0, 0);

		text = new GuiRightAlignedText(0, 0, "", 0xff000000);
		addElement(text);
		spinner = new GuiElement(0, 0, 24, 15);
		addElement(spinner);
		this.grid = grid;
	}

	private static final TranslatedTextSource workingText = new TranslatedTextSource("craftguide.gui.task_running");
	@Override
	public void draw()
	{
		if(RecipeCache.hasActiveTask())
		{
			text.setText(workingText.getText());
			spinner.setBackground(spinnerBackground);
			spinner.setPosition(this.width() - 24 - text.getTextWidth() - 2, -4);
		}
		else
		{
			spinner.setBackground(null);
			text.setText("Rows " + (grid.firstVisibleRow() + 1) + "-" +  + (grid.lastVisibleRow() + 1)  + " of " + (grid.rowCount()));
		}
		super.draw();
	}
}
