package uristqwerty.CraftGuide.api;

import java.util.List;

import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class LiquidSlot implements Slot
{
	private int x;
	private int y;
	private int width = 16;
	private int height = 16;
	private SlotType slotType = SlotType.INPUT_SLOT;

	private static NamedTexture containerTexture = null;

	public LiquidSlot(int x, int y)
	{
		this.x = x;
		this.y = y;

		if(containerTexture == null)
		{
			containerTexture = Util.instance.getTexture("liquidFilterContainer");
		}
	}

	@Override
	public void draw(Renderer renderer, int recipeX, int recipeY, Object[] data, int dataIndex, boolean isMouseOver)
	{
		int x = recipeX + this.x;
		int y = recipeY + this.y;

		if(data[dataIndex] instanceof LiquidStack)
		{
			LiquidStack liquid = (LiquidStack)data[dataIndex];
			RenderEngine renderEngine = FMLClientHandler.instance().getClient().renderEngine;

			if(Item.itemsList[liquid.itemID] != null)
			{
				Item item = Item.itemsList[liquid.itemID];
				Icon icon = item.getIconFromDamage(liquid.itemMeta);

                if (item.func_94901_k() == 0)
                {
                	renderEngine.func_98187_b("/terrain.png");
                }
                else
                {
                	renderEngine.func_98187_b("/gui/items.png");
                }

                double u = icon.func_94214_a(3.0);
                double u2 = icon.func_94214_a(13.0);
                double v = icon.func_94207_b(1.0);
                double v2 = icon.func_94207_b(15.0);

                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glColor4d(1.0, 1.0, 1.0, 1.0);

        		GL11.glBegin(GL11.GL_QUADS);
        	        GL11.glTexCoord2d(u, v);
        	        GL11.glVertex2i(x + 3, y + 1);

        	        GL11.glTexCoord2d(u, v2);
        	        GL11.glVertex2i(x + 3, y + 15);

        	        GL11.glTexCoord2d(u2, v2);
        	        GL11.glVertex2i(x + 13, y + 15);

        	        GL11.glTexCoord2d(u2, v);
        	        GL11.glVertex2i(x + 13, y + 1);
        		GL11.glEnd();
			}
		}

		renderer.renderRect(x - 1, y - 1, 18, 18, containerTexture);
	}

	@Override
	public ItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
	{
		if(data[dataIndex] instanceof LiquidStack)
		{
			return new LiquidFilter((LiquidStack)data[dataIndex]);
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean isPointInBounds(int x, int y, Object[] data, int dataIndex)
	{
		return x >= this.x && x < this.x + width
			&& y >= this.y && y < this.y + height;
	}

	@Override
	public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
	{
		List<String> tooltip = null;

		if(data[dataIndex] instanceof LiquidStack)
		{
			LiquidStack stack = (LiquidStack)data[dataIndex];
			tooltip = Util.instance.getItemStackText(stack.asItemStack());
			tooltip.set(0, tooltip.get(0) + " (" + stack.amount + " milliBuckets)");
		}

		return tooltip;
	}

	//Check if the filter directly matches the contained LiquidStack, or
	// matches any liquid container containing the liquid.
	@Override
	public boolean matches(ItemFilter filter, Object[] data, int dataIndex, SlotType type)
	{
		if(!(data[dataIndex] instanceof LiquidStack) ||
				(type != slotType && (
					type != SlotType.ANY_SLOT ||
					slotType == SlotType.DISPLAY_SLOT ||
					slotType == SlotType.HIDDEN_SLOT)))
		{
			return false;
		}

		LiquidStack stack = (LiquidStack)data[dataIndex];

		if(filter.matches(stack))
		{
			return true;
		}
		else
		{
			for(LiquidContainerData liquidData: LiquidContainerRegistry.getRegisteredLiquidContainerData())
			{
				if(stack.isLiquidEqual(liquidData.stillLiquid) && filter.matches(liquidData.filled))
				{
					return true;
				}
			}

			return filter.matches(stack.asItemStack());
		}
	}

	public Slot setSlotType(SlotType type)
	{
		slotType = type;
		return this;
	}
}
