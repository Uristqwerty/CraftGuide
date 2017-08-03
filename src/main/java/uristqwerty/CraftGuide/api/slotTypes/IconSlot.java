package uristqwerty.CraftGuide.api.slotTypes;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.Renderer;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class IconSlot implements Slot
{
	private static final double RAW_COORD_SCALE = 1/16.0;
	private int x, y, width, height;

	public IconSlot(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Renderer renderer, int recipeX, int recipeY, Object[] dataArray, int dataIndex, boolean isMouseOver)
	{
		Object[] data = (Object[])dataArray[dataIndex];
		Object source = data[0];
		ResourceLocation texture = null;
		TextureAtlasSprite icon = null;
		if(source instanceof ResourceLocation)
		{
			texture = (ResourceLocation)source;
		}
		else if(source instanceof Object[])
		{
			texture = (ResourceLocation)((Object[])source)[0];
			icon = (TextureAtlasSprite)((Object[])source)[1];
		}

		int x = recipeX + this.x;
		int y = recipeY + this.y;
		if(texture != null)
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
			double u = (Float)data[1], v = (Float)data[2],
					u2 = (Float)data[3], v2 = (Float)data[4];
			int color = (Integer)data[5];
			if(icon != null)
			{
				u = icon.getInterpolatedU(u);
				u2 = icon.getInterpolatedU(u2);
				v = icon.getInterpolatedV(v);
				v2 = icon.getInterpolatedV(v2);
			}
			else
			{
				u *= RAW_COORD_SCALE;
				u2 *= RAW_COORD_SCALE;
				v *= RAW_COORD_SCALE;
				v2 *= RAW_COORD_SCALE;
			}
			int x2 = x + width;
			int y2 = y + height;

			GlStateManager.disableDepth();
			GlStateManager.enableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			setColor(color);

			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(u, v);
				GL11.glVertex2i(x, y);

				GL11.glTexCoord2d(u, v2);
				GL11.glVertex2i(x, y2);

				GL11.glTexCoord2d(u2, v2);
				GL11.glVertex2i(x2, y2);

				GL11.glTexCoord2d(u2, v);
				GL11.glVertex2i(x2, y);
			GL11.glEnd();
		}

		if(data.length > 6)
		{
			final String text = data[6].toString();
			int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
			int textX = Math.max(x, x + width + 1 - textWidth);
			renderer.renderText(textX, y + height - 7, text, 0xffffffff, true);
		}
	}

	private void setColor(int color)
	{
		float a = ((color >> 24) & 0xff)/ 255.0f,
				r = ((color >> 16) & 0xff)/ 255.0f,
				g = ((color >> 8) & 0xff)/ 255.0f,
				b = ((color >> 0) & 0xff)/ 255.0f;
		GlStateManager.color(r, g, b, a);
	}

	@Override
	public ItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
	{
		return null;
	}

	@Override
	public boolean isPointInBounds(int x, int y, Object[] data, int dataIndex)
	{
		return false;
	}

	@Override
	public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
	{
		return null;
	}

	@Override
	public boolean matches(ItemFilter filter, Object[] data, int dataIndex, SlotType type)
	{
		return false;
	}
}
