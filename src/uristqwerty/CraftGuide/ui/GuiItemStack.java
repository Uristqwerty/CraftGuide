package uristqwerty.CraftGuide.ui;

import java.util.List;

import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.RenderItemStack;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.minecraft.Image;
import net.minecraft.src.ItemStack;

public class GuiItemStack extends GuiElement
{
	private RenderItemStack renderable;
	private List multi;
	
	private static IRenderable multiSymbol = new TexturedRect(-1, -1, 18, 18, Image.getImage("/gui/CraftGuide.png"), 238, 181);
	
	public GuiItemStack(int x, int y, boolean drawQuantity)
	{
		super(x, y, 16, 16);
		
		renderable = new RenderItemStack(null, drawQuantity);
	}
	
	public void setItem(Object item)
	{
		multi = null;
		
		if(item == null)
		{
			renderable.setItem(null);
		}
		else if(item instanceof ItemStack)
		{
			renderable.setItem((ItemStack)item);
		}
		else if(item instanceof List && ((List)item).size() > 0)
		{
			renderable.setItem((ItemStack)((List)item).get(0));
			multi = (List)item;
		}
	}

	@Override
	public void draw()
	{
		render(renderable);

		if(multi != null)
		{
			render(multiSymbol);
		}
		
		super.draw();
	}
}
