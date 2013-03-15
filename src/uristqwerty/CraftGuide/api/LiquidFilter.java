package uristqwerty.CraftGuide.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class LiquidFilter implements ItemFilter
{
	private static NamedTexture containerTexture = null;
	private LiquidStack liquid;
	private String liquidName;
	private List<String> tooltip = new ArrayList<String>();

	public LiquidFilter(LiquidStack liquid)
	{
		if(containerTexture == null)
		{
			containerTexture = Util.instance.getTexture("liquidFilterContainer");
		}

		setLiquid(liquid);
	}

	public void setLiquid(LiquidStack liquid)
	{
		String name = liquid.asItemStack().getDisplayName();
		this.liquid = liquid;
		liquidName = name.toLowerCase();
		tooltip.clear();
		tooltip.add(name);
	}

	@Override
	public boolean matches(Object item)
	{
		if(item instanceof ItemStack)
		{
			return liquid.isLiquidEqual((ItemStack)item) || LiquidContainerRegistry.containsLiquid((ItemStack)item, liquid);
		}
		else if(item instanceof LiquidStack)
		{
			return liquid.isLiquidEqual((LiquidStack)item);
		}
		else if(item instanceof String)
		{
			return liquidName.contains(((String)item).toLowerCase());
		}
		else if(item instanceof List)
		{
			for(Object object: ((List)item))
			{
				if(matches(object))
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void draw(Renderer renderer, int x, int y)
	{
		if(liquid != null)
		{
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
	public List<String> getTooltip()
	{
		return tooltip ;
	}
}
