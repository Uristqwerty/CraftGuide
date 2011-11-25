package net.minecraft.src.CraftGuide.ui.Rendering;

import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class RenderItemStack implements IRenderable
{
	private static IRenderable overlayAny = new TexturedRect(
		-1, -1, 18, 18, GuiTexture.getInstance("/gui/CraftGuide.png"), 238, 238);
	
	private ItemStack item;
	private boolean drawQuantity;
	
	public RenderItemStack(ItemStack item, boolean drawQuantity)
	{
		this.item = item;
		this.drawQuantity = drawQuantity;
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}
	
	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		if(item != null)
		{
			renderer.drawItemStack(item, xOffset, yOffset, drawQuantity);
			
			if(item.getItemDamage() == -1)
			{
				overlayAny.render(renderer, xOffset, yOffset);
			}
		}
	}

}
