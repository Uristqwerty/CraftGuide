package uristqwerty.CraftGuide.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.RecipeCache;
import uristqwerty.CraftGuide.RecipeCache.Task;
import uristqwerty.CraftGuide.api.CombinableItemFilter;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.client.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.client.ui.Rendering.Overlay;
import uristqwerty.CraftGuide.itemtype.ItemType;
import uristqwerty.gui_craftguide.rendering.Renderable;
import uristqwerty.gui_craftguide.rendering.TexturedRect;
import uristqwerty.gui_craftguide.texture.Texture;

public class FilterSelectGrid extends GuiScrollableGrid implements IRecipeCacheListener, ITextInputListener
{
	private GuiButton backButton;
	private GuiTabbedDisplay display;
	private RecipeCache recipeCache;
	private Renderable gridBackground;
	private Object[] items;
	private List<Object> itemResults = new ArrayList<>();
	private FloatingItemText itemName = new FloatingItemText("-No Item-");
	private Renderable itemNameOverlay = new Overlay(itemName);
	private boolean overItem = false;
	private String searchText = "";

	private NamedTexture textImage = Util.instance.getTexture("TextFilter");
	private NamedTexture overlayAny = Util.instance.getTexture("ItemStack-Any");
	private NamedTexture overlayForge = Util.instance.getTexture("ItemStack-OreDict");
	private NamedTexture errorIcon = Util.instance.getTexture("Error");
	private List<Object> nextResults = null;

	public FilterSelectGrid(int x, int y, int width, int height, GuiScrollBar scrollBar, Texture texture,
		RecipeCache recipeCache, GuiButton backButton, GuiTabbedDisplay display)
	{
		super(x, y, width, height, scrollBar, 18, 18);
		flexibleSize = true;

		this.backButton = backButton;
		this.display = display;
		this.recipeCache = recipeCache;
		recipeCache.addListener(this);
		setColumns();
		onReset(recipeCache);

		gridBackground = new TexturedRect(0, 0, 18, 18, texture, 238, 219);
	}

	@Override
	public void cellClicked(int cell, int x, int y)
	{
		ItemFilter newFilter;
		if(cell < itemResults.size())
		{
			newFilter = Util.instance.getCommonFilter(itemResults.get(cell));
		}
		else if(cell == itemResults.size() && searchText != null && !searchText.isEmpty())
		{
			newFilter = Util.instance.getCommonFilter(searchText);
		}
		else
		{
			return;
		}

		ItemFilter oldFilter = recipeCache.getFilter();
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

		boolean returnToRecipeList = true;

		if((shift || ctrl) && oldFilter instanceof CombinableItemFilter)
		{
			CombinableItemFilter f = (CombinableItemFilter)oldFilter;
			ItemFilter result = shift? f.addItemFilter(newFilter) : f.subtractItemFilter(newFilter);

			if(result != null)
			{
				newFilter = result;
				returnToRecipeList = false;
			}
		}

		recipeCache.filter(newFilter);

		if(returnToRecipeList)
		{
			display.openTab(backButton);
		}
	}

	@Override
	public void mouseMoved(int x, int y)
	{
		overItem = false;
		lastMouseX = x;
		lastMouseY = y;
		super.mouseMoved(x, y);
	}

	@Override
	public void mouseMovedCell(int cell, int x, int y, boolean inBounds)
	{
		if(inBounds)
		{
			if(cell < itemResults.size())
			{
				overItem = true;
				Object stack = itemResults.get(cell);
				if(stack instanceof String)
				{
					itemName.setText(Arrays.asList(((String)stack).split("\n")));
				}
				else
				{
					itemName.setText(CommonUtilities.getExtendedItemStackText(stack));
				}
			}
			else if(cell == itemResults.size() && searchText != null && !searchText.isEmpty())
			{
				overItem = true;
				itemName.setText("\u00a77Text search: '" + searchText + "'");
			}
		}
	}

	@Override
	public void renderGridCell(GuiRenderer renderer, int xOffset, int yOffset, int cell)
	{
		if(cell < itemResults.size())
		{
			gridBackground.render(renderer, xOffset, yOffset);
			ItemStack stack = displayItem(cell);

			if(stack != null)
			{
				renderer.drawItemStack(stack, xOffset + 1, yOffset + 1);

				if(CommonUtilities.getItemDamage(stack) == CraftGuide.DAMAGE_WILDCARD)
				{
					renderer.renderRect(xOffset, yOffset, 18, 18, overlayAny);
				}

				if(itemResults.get(cell) instanceof ArrayList)
				{
					renderer.renderRect(xOffset, yOffset, 18, 18, overlayForge);
				}
			}
			else if(itemResults.get(cell) instanceof String)
			{
				renderer.renderRect(xOffset, yOffset, 18, 18, errorIcon);
			}
		}
		else if(cell == itemResults.size() && searchText != null && !searchText.isEmpty())
		{
			gridBackground.render(renderer, xOffset, yOffset);
			renderer.renderRect(xOffset + 1, yOffset + 1, 16, 16, textImage);
		}
	}

	private ItemStack displayItem(int cell)
	{
		Object item = itemResults.get(cell);

		if(item instanceof ItemStack)
		{
			return (ItemStack)item;
		}
		else if(item instanceof ArrayList && ((ArrayList<ItemStack>)item).size() > 0)
		{
			return ((ArrayList<ItemStack>)item).get(0);
		}
		else
		{
			return null;
		}
	}

	@Override
	public void draw()
	{
		checkAsyncResults();
		super.draw();

		if(overItem)
		{
			render(itemNameOverlay);
		}
	}

	@Override
	public void onReset(RecipeCache cache)
	{
		items = cache.getAllItems().toArray();
		search(searchText);
	}

	private static int searchId = 0;
	public void search(final String text)
	{
		searchText = text;
		itemResults.clear();

		if(text == null || text.isEmpty())
		{
			for(Object item: items)
			{
				itemResults.add(((ItemType)item).getStack());
			}

			setCells(itemResults.size());
			mouseMoved(lastMouseX, lastMouseY);
		}
		else
		{
			final int id = ++searchId;
			RecipeCache.runTask(new Task(){
				@Override
				public void run()
				{
					String search = text.toLowerCase();
					List<Object> results = new ArrayList<>();

					int prevSize = 0;
					int iters = 0;
					for(Object item: items)
					{
						Object stack = ((ItemType)item).getStack();

						if(stack instanceof String)
						{
							continue;
						}

						if(CommonUtilities.searchExtendedItemStackText(stack, search))
						{
							results.add(stack);
						}
						if(++iters % 500 == 0 && results.size() != prevSize)
						{
							if(id != searchId)
							{
								break;
							}
							prevSize = results.size();
							updateResultsAsync(results);
							results = new ArrayList<>(results);
						}
					}

					updateResultsAsync(results);
				}});
		}
	}


	private void updateResultsAsync(List<Object> results)
	{
		synchronized(this)
		{
			nextResults  = results;
		}
	}

	private void checkAsyncResults()
	{
		if(nextResults != null)
		{
			synchronized(this)
			{
				itemResults = nextResults;
				nextResults = null;
				setCells(itemResults.size());
				mouseMoved(lastMouseX, lastMouseY);
			}
		}
	}

	@Override
	public void onChange(RecipeCache cache)
	{
	}

	@Override
	public void onSubmit(GuiTextInput input)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || CraftGuide.textSearchRequiresShift == false)
		{
			recipeCache.filter(Util.instance.getCommonFilter(input.getText()));
			display.openTab(backButton);
		}
	}

	@Override
	public void onTextChanged(GuiTextInput input)
	{
		search(input.getText());
	}

	public ItemStack stackAtCoords(int x, int y)
	{
		int cell = cellAtCoords(x, y);

		if(cell < itemResults.size())
		{
			Object content = itemResults.get(cell);

			if(content instanceof ItemStack)
			{
				return (ItemStack)content;
			}
			else if(content instanceof List && ((List<?>)content).size() > 0 && ((List<?>)content).get(0) instanceof ItemStack)
			{
				return (ItemStack)((List<?>)content).get(0);
			}
		}

		return null;
	}

	@Override
	protected int getMinCellHeight(int i)
	{
		return 18;
	}
}
