package uristqwerty.CraftGuide.client.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StatCollector;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.client.ui.GuiButton.ButtonState;
import uristqwerty.CraftGuide.client.ui.text.TextSource;
import uristqwerty.CraftGuide.client.ui.text.TextSource.TextChangeListener;
import uristqwerty.CraftGuide.client.ui.text.TranslatedTextSource;
import uristqwerty.CraftGuide.dump.JsonRecipeDump;
import uristqwerty.gui_craftguide.components.GuiElement;
import uristqwerty.gui_craftguide.minecraft.MultilineText;
import uristqwerty.gui_craftguide.theme.ThemeManager;

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

	static interface ToggleConfig
	{
		public void onToggle(boolean newState);
	}

	public ConfigList(int x, int y, int width, int height, GuiScrollBar scrollBar, ButtonTemplate buttonTemplate, ButtonTemplate toggleTemplate)
	{
		super(x, y, width, height, scrollBar);

		addToggle("craftguide.gui.config.pause", toggleTemplate, CraftGuide.pauseWhileOpen,
				new ToggleConfig()
				{
					@Override
					public void onToggle(boolean newState)
					{
						CraftGuide.pauseWhileOpen = newState;
						CraftGuide.saveConfig();
					}
				});

		addToggle("craftguide.gui.config.show_ids", toggleTemplate, CraftGuide.alwaysShowID,
				new ToggleConfig()
				{
					@Override
					public void onToggle(boolean newState)
					{
						CraftGuide.alwaysShowID = newState;
						CraftGuide.saveConfig();
					}
				});

		addToggle("craftguide.gui.config.enable_keybind", toggleTemplate, CraftGuide.enableKeybind,
				new ToggleConfig()
				{
					@Override
					public void onToggle(boolean newState)
					{
						CraftGuide.enableKeybind = newState;
						CraftGuide.saveConfig();
					}
				});

		addToggle("craftguide.gui.config.enable_item_recipe", toggleTemplate, CraftGuide.enableItemRecipe,
				new ToggleConfig()
				{
					@Override
					public void onToggle(boolean newState)
					{
						CraftGuide.enableItemRecipe = newState;
						CraftGuide.saveConfig();
					}
				});

		addToggle("craftguide.gui.config.hide_mundane_potions", toggleTemplate, CraftGuide.hideMundanePotionRecipes,
				new ToggleConfig()
				{
					@Override
					public void onToggle(boolean newState)
					{
						CraftGuide.hideMundanePotionRecipes = newState;
						CraftGuide.saveConfig();
					}
				});

		addToggle("craftguide.gui.config.theme_debugging", toggleTemplate, ThemeManager.debugOutput,
				new ToggleConfig()
				{
					@Override
					public void onToggle(boolean newState)
					{
						ThemeManager.debugOutput = newState;
						CraftGuide.saveConfig();
					}
				});

		addButton("craftguide.gui.config.reload_recipes", "craftguide.gui.config.reload_recipes.button", buttonTemplate,
				new IButtonListener()
				{
					@Override
					public void onButtonEvent(GuiButton button, Event event)
					{
						if(event == Event.PRESS)
						{
							GuiCraftGuide.getInstance().reloadRecipes();
						}
					}
				});

		addButton("craftguide.gui.config.export_displayed_recipes", "craftguide.gui.config.export_displayed_recipes.button", buttonTemplate,
				new IButtonListener()
				{
					@Override
					public void onButtonEvent(GuiButton button, Event event)
					{
						if(event == Event.PRESS)
						{
							try
							{
								new JsonRecipeDump().exportDisplayedRecipes(new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, "CraftGuide_DisplayedRecipes.json")));
							}
							catch(IOException e)
							{
								CraftGuideLog.log(e, "", true);
							}
						}
					}
				});

		addButton("craftguide.gui.config.export_crafting_recipes", "craftguide.gui.config.export_crafting_recipes.button", buttonTemplate,
				new IButtonListener()
				{
					@Override
					public void onButtonEvent(GuiButton button, Event event)
					{
						if(event == Event.PRESS)
						{
							try
							{
								new JsonRecipeDump().dumpCraftingRecipes(new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, "CraftGuide_CraftingRecipes.json")));
							}
							catch(IOException e)
							{
								CraftGuideLog.log(e, "", true);
							}
						}
					}
				});
		updateScrollbarScale();
	}

	private void addButton(String text, String buttonText, ButtonTemplate buttonTemplate, IButtonListener listener)
	{
		String translatedButtonText = StatCollector.translateToLocal(buttonText);
		int textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(translatedButtonText);

		addRow(text,
				new GuiButton(BORDER_THICKNESS, BORDER_THICKNESS, Math.max(32, textWidth + 4), 13, buttonTemplate, translatedButtonText)
					.addButtonListener(listener)
					.anchor(AnchorPoint.TOP_RIGHT));
	}

	private void addToggle(String text, ButtonTemplate toggleTemplate, boolean initialState, final ToggleConfig onToggle)
	{
		addRow(text,
				new ToggleButton(BORDER_THICKNESS, BORDER_THICKNESS, 13, 13, toggleTemplate)
					.setState(initialState? ButtonState.DOWN : ButtonState.UP)
					.addButtonListener(new IButtonListener()
					{
						@Override
						public void onButtonEvent(GuiButton button, Event eventType)
						{
							if(eventType == Event.PRESS)
							{
								onToggle.onToggle(true);
							}
							else if(eventType == Event.RELEASE)
							{
								onToggle.onToggle(false);
							}
						}
					})
					.anchor(AnchorPoint.TOP_RIGHT));
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
			width = 64; // Arbitrary minimum width, to prevent excessive wrapping. Space for at least 4 characters.
		}

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		List<String> lines = fr.listFormattedStringToWidth(text, width);

		return lines.toArray(new String[lines.size()]);
	}

	@Override
	public void drawChildren()
	{
		GuiRenderer renderer = getRenderer();
		renderer.setClippingRegion(absoluteX() + 1, absoluteY() + 1, width() - 2, height() - 2);
		int y = bounds.y();
		setPosition(bounds.x(), y - (int)scrollBar.getValue());
		try
		{
			super.drawChildren();
		}
		finally
		{
			setPosition(bounds.x(), y);
			renderer.clearClippingRegion();
		}
	}
}
