package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import net.minecraft.src.ItemStack;

/**
 * When a recipe is rendered, the ItemSlots provided to the template are
 * used to determine the layout of the recipe's items.
 */

public class ItemSlot implements ISlot
{
	public int x, y, width, height, index;
	public boolean drawQuantity;

	public ItemSlot(int x, int y, int width, int height, int index)
	{
		this(x, y, width, height, index, false);
	}
	
	public ItemSlot(int x, int y, int width, int height, int index, boolean drawQuantity)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.index = index;
		this.drawQuantity = drawQuantity;
	}

	@Override
	public void init(IRenderer renderer)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(IRenderer renderer, int x, int y, Object data, boolean isMouseOver)
	{
		ItemStack stack = item(data);
		
		if(stack != null)
		{
			renderer.renderItemStack(x, y, stack);
		}
	}

	@Override
	public List<String> getTooltip(Object data)
	{
		ItemStack stack = item(data);
		
		if(stack == null)
		{
			return null;
		}
		else
		{
			return Util.instance.getItemStackText(stack);
		}
	}
	
	public static ItemStack item(Object data)
	{
		if(data instanceof ItemStack)
		{
			return (ItemStack)data;
		}
		else if(data instanceof List && ((List)data).size() > 0)
		{
			return item(((List)data).get(0));
		}

		return null;
	}
}