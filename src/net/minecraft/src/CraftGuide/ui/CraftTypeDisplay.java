package net.minecraft.src.CraftGuide.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.CraftType;
import net.minecraft.src.CraftGuide.RecipeCache;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class CraftTypeDisplay extends GuiScrollableGrid implements IRecipeCacheListener
{
	private GuiBorderedRect background;
	private RecipeCache recipeCache;
	private Map<CraftType, Integer> settings = new HashMap<CraftType, Integer>();
	
	private TexturedRect buttons[] = new TexturedRect[6];
	
	public CraftTypeDisplay(int x, int y, int width, int height, GuiScrollBar scrollBar, GuiTexture guiTexture, RecipeCache recipeCache)
	{
		super(x, y, width, height, scrollBar, 32, 1);
		
		background = new GuiBorderedRect(
			0, 0, width, 32,
			guiTexture, 117, 1, 2, 32
		);
		
		this.recipeCache = recipeCache;
		recipeCache.addListener(this);
		
		buttons[0] = new TexturedRect(0, 0, 28, 28, guiTexture, 113,  76);
		buttons[1] = new TexturedRect(0, 0, 28, 28, guiTexture, 113, 104);
		buttons[2] = new TexturedRect(0, 0, 28, 28, guiTexture, 113, 132);
		buttons[3] = new TexturedRect(0, 0, 28, 28, guiTexture, 141,  76);
		buttons[4] = new TexturedRect(0, 0, 28, 28, guiTexture, 141, 104);
		buttons[5] = new TexturedRect(0, 0, 28, 28, guiTexture, 141, 132);
		
		setRows(recipeCache.getCraftTypes().size());
	}

	@Override
	public void renderGridRow(GuiRenderer renderer, int xOffset, int yOffset, int row)
	{
		Set<CraftType> types = recipeCache.getCraftTypes();
		
		if(row < types.size())
		{
			CraftType type = (CraftType)types.toArray()[row];
			background.render(renderer, xOffset, yOffset);
			renderer.drawItemStack(type.getStack(), xOffset + 8, yOffset + 8, false);
			
			for(int i = 0; i < 3; i++)
			{
				TexturedRect rect = buttons[i == setting(type)? i + 3 : i];
				
				rect.render(renderer, xOffset + i * 29 + (width - (3 * 29 + 24)) / 2 + 24, yOffset + 2);
			}
		}
	}

	@Override
	public void rowClicked(int row, int x, int y, boolean inBounds)
	{
		if(y > 1 && y < 30 && inBounds)
		{
			if(x >= (width - (3 * 29 + 24)) / 2 + 24 && x < (width - (3 * 29 + 24)) / 2 + 24 + 3 * 29)
			{
				int relX = (x - ((width - (3 * 29 + 24)) / 2 + 24));
				
				if(relX % 29 != 28)
				{
					set(row, relX / 29);
				}
			}
		}
		
		super.rowClicked(row, x, y, inBounds);
	}

	@Override
	public void onResize(int oldWidth, int oldHeight)
	{
		background.setSize(width, 32);
		
		super.onResize(oldWidth, oldHeight);
	}

	@Override
	public void onChange()
	{
		setRows(recipeCache.getCraftTypes().size());
	}
	
	@Override
	public void setColumns(int columns)
	{
	}

	private int setting(CraftType type)
	{
		if(!settings.containsKey(type))
		{
			settings.put(type, 0);
		}
		
		return settings.get(type);
	}
	
	private void set(int row, int setting)
	{
		Set<CraftType> types = recipeCache.getCraftTypes();
		
		if(row < types.size())
		{
			CraftType type = (CraftType)types.toArray()[row];

			settings.put(type, setting);
			
			settingChanged(type, setting);
		}
	}

	private void settingChanged(CraftType type, int setting)
	{
		if(setting == 2)
		{
			for(CraftType settingType: settings.keySet())
			{
				if(settingType != type && settings.get(settingType) == 2)
				{
					settings.put(settingType, 0);
				}
			}
		}
		
		updateFilter();
	}

	private void updateFilter()
	{
		Set<CraftType> set = new HashSet<CraftType>();
		
		for(CraftType type: settings.keySet())
		{
			if(setting(type) == 2)
			{
				set.add(type);
				recipeCache.setTypes(set);
				return;
			}
		}
		
		for(CraftType type: recipeCache.getCraftTypes())
		{
			if(setting(type) != 1)
			{
				set.add(type);
			}
		}
		
		recipeCache.setTypes(set);
	}
}
