package uristqwerty.CraftGuide.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderEngine;
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
			if(liquid.itemID < Block.blocksList.length && Block.blocksList[liquid.itemID] != null)
			{
				Block block = Block.blocksList[liquid.itemID];
				int index = block.blockIndexInTexture;
				int texture = renderEngine.getTexture(block.getTextureFile());
	            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
				rect(x + 3, y + 1, 10, 14, (index & 0xf) * 16 + 3, (index & 0xf0) + 1);
			}
			else if(Item.itemsList[liquid.itemID] != null)
			{
				Item item = Item.itemsList[liquid.itemID];
				int index = item.getIconFromDamage(liquid.itemMeta);
				int texture = renderEngine.getTexture(item.getTextureFile());
	            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
				rect(x + 3, y + 1, 10, 14, (index & 0xf) * 16 + 3, (index & 0xf0) + 1);
			}
		}

		renderer.renderRect(x - 1, y - 1, 18, 18, containerTexture);
	}

	@Override
	public List<String> getTooltip()
	{
		return tooltip ;
	}

	//Assumption: Tessellator is more likely to be unnessecary overhead than beneficial
	//  when all that is being rendered is a single quad...
	private static void rect(int x, int y, int width, int height, int texX, int texY)
	{
		double u = texX / 256.0;
		double v = texY / 256.0;
		double u2 = (texX + width) / 256.0;
		double v2 = (texY + height) / 256.0;

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);

		GL11.glBegin(GL11.GL_QUADS);
	        GL11.glTexCoord2d(u, v);
	        GL11.glVertex2i(x, y);

	        GL11.glTexCoord2d(u, v2);
	        GL11.glVertex2i(x, y + height);

	        GL11.glTexCoord2d(u2, v2);
	        GL11.glVertex2i(x + width, y + height);

	        GL11.glTexCoord2d(u2, v);
	        GL11.glVertex2i(x + width, y);
		GL11.glEnd();
	}
}
