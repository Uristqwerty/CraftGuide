package uristqwerty.CraftGuide.client.ui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.client.ui.GuiButton.ButtonState;
import uristqwerty.CraftGuide.client.ui.text.TextSource;
import uristqwerty.CraftGuide.client.ui.text.TextSource.TextChangeListener;
import uristqwerty.CraftGuide.client.ui.text.TranslatedTextSource;
import uristqwerty.gui_craftguide.components.GuiElement;
import uristqwerty.gui_craftguide.minecraft.MultilineText;

public class ConfigList extends GuiScrollableContent
{
	static final int BORDER_THICKNESS = 3;

	class ConfigEntry extends GuiElement implements TextChangeListener
	{
		MultilineText textDisplay;
		TextSource translatedText;
		GuiElement element;

		public ConfigEntry(String text, GuiElement element, int pos)
		{
			super(0, pos, ConfigList.this.width(), 0);

			this.element = element;
			textDisplay = new MultilineText(BORDER_THICKNESS + element.width() + BORDER_THICKNESS, BORDER_THICKNESS, null);
			translatedText = new TranslatedTextSource(text);
			translatedText.addListener(this);
			addElement(element);
			recalculateHeight();
		}

		@Override
		public void onTextChanged(TextSource source)
		{
			int prevHeight = height();
			recalculateHeight();

			if(height() != prevHeight)
			{
				ConfigList.this.entryHeightChanged(this);
			}
		}

		private void recalculateHeight()
		{
			int textLeft = BORDER_THICKNESS + element.width() + BORDER_THICKNESS;
			int textRight = ConfigEntry.this.width() - BORDER_THICKNESS;

			String[] lines = layoutText(translatedText.getText(), textRight - textLeft);
			textDisplay.setText(lines);

			int textHeight = lines.length * 10 + BORDER_THICKNESS * 2;
			int elementHeight = element.height() + BORDER_THICKNESS * 2;
			int height = Math.max(textHeight, elementHeight);

			setSize(width(), height);

			if(textHeight > elementHeight)
			{
				textDisplay.setPosition(textLeft, BORDER_THICKNESS);
				element.setPosition(BORDER_THICKNESS, (height - element.height()) / 2);
			}
			else
			{
				textDisplay.setPosition(textLeft, (height - lines.length * 10 + 1) / 2);
				element.setPosition(BORDER_THICKNESS, BORDER_THICKNESS);
			}
		}

		@Override
		public void draw()
		{
			super.draw();
			render(textDisplay);
		}
	}

	private List<ConfigEntry> options = new ArrayList<ConfigEntry>();

	public ConfigList(int x, int y, int width, int height, GuiScrollBar scrollBar, ButtonTemplate toggleTemplate)
	{
		super(x, y, width, height, scrollBar);

		addRow("craftguide.gui.config.pause",
				new ToggleButton(BORDER_THICKNESS, BORDER_THICKNESS, 13, 13, toggleTemplate)
					.setState(CraftGuide.pauseWhileOpen? ButtonState.DOWN : ButtonState.UP)
					.addButtonListener(new IButtonListener()
					{
						@Override
						public void onButtonEvent(GuiButton button, Event eventType)
						{
							if(eventType == Event.PRESS)
							{
								CraftGuide.pauseWhileOpen = true;
							}
							else if(eventType == Event.RELEASE)
							{
								CraftGuide.pauseWhileOpen = false;
							}

							CraftGuide.saveConfig();
						}
					})
					.anchor(AnchorPoint.TOP_RIGHT));

		updateScrollbarScale();
	}

	private void addRow(String text, GuiElement element)
	{
		int pos = 0;
		if(options.size() > 0)
		{
			ConfigEntry last = options.get(options.size() - 1);
			pos = last.relativeY() + last.height();
		}

		ConfigEntry entry = new ConfigEntry(text, element, pos);
		options.add(entry);
		addElement(entry);
		//entry.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.TOP_RIGHT);
	}

	@Override
	public void onResize(int oldWidth, int oldHeight)
	{
		super.onResize(oldWidth, oldHeight);

		if(width() != oldWidth)
		{
			widthChanged();
		}
	}

	private void widthChanged()
	{
		boolean heightChange = false;

		for(ConfigEntry entry: options)
		{
			int prevHeight = entry.height();
			entry.setSize(width(), entry.height());
			entry.recalculateHeight();

			if(entry.height() != prevHeight)
			{
				heightChange = true;
			}
		}

		if(heightChange)
		{
			recalculatePositions();
		}
	}

	private void recalculatePositions()
	{
		int pos = 0;

		for(ConfigEntry entry: options)
		{
			entry.setPosition(0, pos);
			pos += entry.height();
		}

		updateScrollbarScale();
	}

	public void entryHeightChanged(ConfigEntry configEntry)
	{
		recalculatePositions();
	}

	public void updateScrollbarScale()
	{
		float end = 0;

		if(options.size() > 0)
		{
			ConfigEntry last = options.get(options.size() - 1);
			end = last.relativeY() + last.height() - height();
		}

		if(end < 0)
		{
			end = 0;
		}

		scrollBar.setScale(0, end);
	}

	static String[] layoutText(String text, int width)
	{
		if(width < 64)
		{
			width = 64; // Arbitrary minimum width, to prevent excessive wrapping. At least 4 characters.
		}

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		List<String> lines = fr.listFormattedStringToWidth(text, width);

		return lines.toArray(new String[lines.size()]);
	}
}
