package uristqwerty.CraftGuide.client.ui;

import uristqwerty.CraftGuide.client.ui.text.PlainTextSource;
import uristqwerty.CraftGuide.client.ui.text.TextSource;
import uristqwerty.CraftGuide.client.ui.text.TextSource.TextChangeListener;
import uristqwerty.gui_craftguide.components.GuiElement;
import uristqwerty.gui_craftguide.minecraft.Text;

public class GuiText extends GuiElement implements TextChangeListener
{
	private TextSource textSource;
	private Text text;

	public GuiText(int x, int y, TextSource text, int color)
	{
		super(x, y, 0, 0);

		this.text = new Text(0, 0, text.getText(), color);
		setText(text);
	}

	public GuiText(int x, int y, String text, int color)
	{
		this(x, y, new PlainTextSource(text), color);
	}

	public GuiText(int x, int y, String text)
	{
		this(x, y, text, 0xffffffff);
	}

	public void setText(String text)
	{
		setText(new PlainTextSource(text));
	}

	public void setText(TextSource text)
	{
		if(textSource != null)
		{
			textSource.removeListener(this);
		}

		text.addListener(this);
		textSource = text;

		this.text.setText(text.getText());
	}

	@Override
	public void draw()
	{
		render(text);

		super.draw();
	}

	@Override
	public void onTextChanged(TextSource source)
	{
		this.text.setText(source.getText());
	}
}
