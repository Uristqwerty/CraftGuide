package uristqwerty.CraftGuide.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;

import org.lwjgl.opengl.GL11;

import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.client.ui.Rendering.Overlay;
import uristqwerty.gui_craftguide.minecraft.Gui;
import uristqwerty.gui_craftguide.rendering.Renderable;
import uristqwerty.gui_craftguide.rendering.RendererBase;
import uristqwerty.gui_craftguide.rendering.TexturedRect;
import uristqwerty.gui_craftguide.texture.DynamicTexture;
import uristqwerty.gui_craftguide.texture.Texture;

public class GuiRenderer extends RendererBase implements uristqwerty.CraftGuide.api.Renderer
{
    private RenderItem itemRenderer = new RenderItem();
	private Minecraft minecraft;
	private List<Overlay> overlays = new LinkedList<Overlay>();
	private Gui gui;

	private static Map<ItemStack, ItemStack> itemStackFixes = new HashMap<ItemStack, ItemStack>();

	private Renderable itemError = new TexturedRect(-1, -1, 18, 18, DynamicTexture.instance("item_error"), 238, 200);

	public void startFrame(Minecraft mc, Gui gui)
	{
		minecraft = mc;
		this.gui = gui;
    	resetValues();
	}

	public void endFrame()
	{
		for(Overlay overlay: overlays)
		{
			overlay.renderOverlay(this);
		}

		overlays.clear();

		GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
	}

	public void setColor(int colour, int alpha)
	{
		setColorRgb(colour);
		setAlpha(alpha);
	}

	@Override
	public void setTextureID(int textureID)
	{
		if(textureID != -1 && minecraft != null)
		{
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            minecraft.renderEngine.resetBoundTexture();
			//minecraft.renderEngine.bindTexture(textureID);
		}
	}

	public void drawGradient(int x, int y, int width, int height, int topColor, int bottomColor)
	{
		renderVerticalGradient(x, y, width, height, topColor, bottomColor);
	}

	public void render(Renderable renderable, int xOffset, int yOffset)
	{
		renderable.render(this, xOffset, yOffset);
	}

	public void overlay(Overlay overlay)
	{
		overlays.add(overlay);
	}

	@Override
	public void drawText(String text, int x, int y)
	{
		minecraft.fontRenderer.drawString(text, x, y, currentColor());
	}

	@Override
	public void drawTextWithShadow(String text, int x, int y)
	{
		minecraft.fontRenderer.drawStringWithShadow(text, x, y, currentColor());
	}

	private int currentColor()
	{
		return ((((int)(alpha * 255)) & 0xff) << 24) | ((((int)(red * 255)) & 0xff) << 16) | ((((int)(green * 255)) & 0xff) << 8) | (((int)(blue * 255)) & 0xff);
	}

	public void drawFloatingText(int x, int y, String text)
	{
		List<String> list = new ArrayList<String>(1);
		list.add(text);
		drawFloatingText(x, y, list);
	}

	public void drawFloatingText(int x, int y, List<String> text)
	{
		int textWidth = 0;
		int textHeight = (text.size() > 1)? text.size() * 10: 8;

		for(String s: text)
		{
			int w;

			if(s.charAt(0) == '\u00a7')
			{
				w = minecraft.fontRenderer.getStringWidth(s.substring(2));
			}
			else
			{
				w = minecraft.fontRenderer.getStringWidth(s);
			}

			if(w > textWidth)
			{
				textWidth = w;
			}
		}

		int xMax = gui.width - textWidth - 4;
		int yMax = gui.height - textHeight - 4;

		if(x > xMax)
		{
			x = xMax;
		}

		if(x < 3)
		{
			x = 3;
		}

		if(y > yMax)
		{
			y = yMax;
		}

		if(y < 4)
		{
			y = 4;
		}

    	setColor(0xf0100010);
		drawRect(x - 3,				y - 4,				textWidth + 6,	1);
		drawRect(x - 3,				y + textHeight + 3,	textWidth + 6,	1);
		drawRect(x - 3,				y - 3,				textWidth + 6,	textHeight + 6);
		drawRect(x - 4,				y - 3,				1,				textHeight + 6);
		drawRect(x + textWidth + 3,	y - 3,				1,				textHeight + 6);

		setColor(0x505000ff);
		drawRect(x - 3, y - 3, textWidth + 6, 1);

		setColor(0x5028007f);
		drawRect(x - 3, y + textHeight + 2,	textWidth + 6, 1);

		drawGradient(x - 3,				y - 2, 1, textHeight + 4, 0x505000ff, 0x5028007f);
		drawGradient(x + textWidth + 2, y - 2, 1, textHeight + 4, 0x505000ff, 0x5028007f);

    	setColor(0xffffffff);

		int textY = y;
		boolean first = true;
		for(String s: text)
		{
        	drawTextWithShadow(s, x, textY);

			if(first)
			{
                textY += 2;
                first = false;
			}

            textY += 10;
		}
	}

	public void drawItemStack(ItemStack itemStack, int x, int y)
	{
		drawItemStack(itemStack, x, y, true);
	}

	public void drawItemStack(ItemStack itemStack, int x, int y, boolean renderOverlay)
	{
    	if(itemStack == null)
    	{
    		return;
    	}

		boolean error = true;

		GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        itemRenderer.zLevel = 100.0F;

        int initialMatrixStackDepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);

        try
        {
        	if(CommonUtilities.getItemDamage(itemStack) == CraftGuide.DAMAGE_WILDCARD)
        	{
        		itemStack = fixedItemStack(itemStack);
        	}

			itemRenderer.renderItemAndEffectIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);

			if(renderOverlay)
			{
				itemRenderer.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
			}

			error = false;
        }
        catch(Exception e)
        {
    		if(!hasLogged(itemStack))
    		{
    			CraftGuideLog.log("Failed to render ItemStack {" + (
    					itemStack == null? "null" : (
    						"itemID = " + itemStack.itemID +
    						", itemDamage = " + CommonUtilities.getItemDamage(itemStack) +
    						", stackSize = " + itemStack.stackSize)) +
    					"} (Further stack traces from this particular ItemStack instance will not be logged)");
    			CraftGuideLog.log(e);
    		}
        }
        catch(Error e)
        {
        	CraftGuideLog.log(e);
        	throw e;
        }
        finally
        {
            int finalMatrixStackDepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);

            // If something went wrong, and an exception was thrown after at least one matrix push,
            //  fix it here, so that rendering errors do not affect later parts of the UI.
            while(finalMatrixStackDepth > initialMatrixStackDepth)
            {
            	GL11.glPopMatrix();
            	finalMatrixStackDepth--;
            }

        	CraftGuide.side.stopTessellating();

            itemRenderer.zLevel = 0.0F;
            GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.disableStandardItemLighting();
    		GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }

        if(error)
        {
    		drawItemStackError(x, y);
        }
	}

	private HashSet<ItemStack> loggedStacks = new HashSet<ItemStack>();

	private boolean hasLogged(ItemStack stack)
	{
		return !loggedStacks.add(stack);
	}

	public static ItemStack fixedItemStack(ItemStack itemStack)
	{
		ItemStack stack = itemStackFixes.get(itemStack);

		if(stack == null)
		{
			stack = itemStack.copy();
			stack.setItemDamage(0);
			itemStackFixes.put(itemStack, stack);
		}

		return stack;
	}

	private void drawItemStackError(int x, int y)
	{
		itemError.render(this, x, y);
	}

	public void setClippingRegion(int x, int y, int width, int height)
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		x *= minecraft.displayHeight / (float)gui.height;
		y *= minecraft.displayWidth / (float)gui.width;
		height *= minecraft.displayHeight / (float)gui.height;
		width *= minecraft.displayWidth / (float)gui.width;

		GL11.glScissor(x, minecraft.displayHeight - y - height, width, height);
	}

	public void clearClippingRegion()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public int guiXFromMouseX(int x)
	{
        return (x * gui.width) / minecraft.displayWidth;
	}

	public int guiYFromMouseY(int y)
	{
        return gui.height - (y * gui.height) / minecraft.displayHeight - 1;
	}

	@Override
	public void renderItemStack(int x, int y, ItemStack stack)
	{
		drawItemStack(stack, x, y);
	}

	@Override
	public void renderRect(int x, int y, int width, int height, NamedTexture texture)
	{
		if(texture != null)
		{
			setColor(255, 255, 255, 255);
			drawTexturedRect((Texture)texture, x, y, width, height, 0, 0);
		}
	}

	@Override
	public void renderRect(int x, int y, int width, int height, int red, int green, int blue, int alpha)
	{
		setColor(red, green, blue, alpha);
		drawRect(x, y, width, height);
		setColor(0xff, 0xff, 0xff, 0xff);
	}

	@Override
	public void renderRect(int x, int y, int width, int height, int color_rgb, int alpha)
	{
		renderRect(x, y, width, height,
				(color_rgb >> 16) & 0xff,
				(color_rgb >>  8) & 0xff,
				(color_rgb >>  0) & 0xff,
				alpha);
	}

	@Override
	public void renderRect(int x, int y, int width, int height, int color_argb)
	{
		renderRect(x, y, width, height, color_argb & 0x00ffffff, (color_argb >> 24) & 0xff);
	}

	@Override
	public void renderVerticalGradient(int x, int y, int width, int height, int topColor_argb, int bottomColor_argb)
	{
		renderGradient(x, y, width, height, topColor_argb, topColor_argb, bottomColor_argb, bottomColor_argb);
	}

	@Override
	public void renderHorizontalGradient(int x, int y, int width, int height, int leftColor_argb, int rightColor_argb)
	{
		renderGradient(x, y, width, height, leftColor_argb, rightColor_argb, leftColor_argb, rightColor_argb);
	}

	@Override
	public void renderGradient(int x, int y, int width, int height, int topLeftColor_argb, int topRightColor_argb,
			int bottomLeftColor_argb, int bottomRightColor_argb)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(770, 771);

	    GL11.glBegin(GL11.GL_QUADS);
	    	glColor1i(topLeftColor_argb);
	        GL11.glVertex2i(x, y);

	    	glColor1i(bottomLeftColor_argb);
	        GL11.glVertex2i(x, y + height);

	    	glColor1i(bottomRightColor_argb);
	        GL11.glVertex2i(x + width, y + height);

	    	glColor1i(topRightColor_argb);
	        GL11.glVertex2i(x + width, y);
	    GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void glColor1i(int color)
	{
	    setGlColor(
				((color >> 16) & 0xff) / 255.0,
				((color >>  8) & 0xff) / 255.0,
				((color >>  0) & 0xff) / 255.0,
				((color >> 24) & 0xff) / 255.0);
	}

	public List<String> getItemNameandInformation(ItemStack stack)
	{
		if(stack.getItem() != null)
		{
			try
			{
				return getTooltip(stack);
			}
			catch(Exception e)
			{
				try
				{
					stack = fixedItemStack(stack);
					return getTooltip(stack);
				}
				catch(Exception e2)
				{
					CraftGuideLog.log(e2);
				}
			}
		}

		List<String> list = new ArrayList<String>();
		list.add("Err: Item #" + stack.itemID + ", damage " + CommonUtilities.getItemDamage(stack));
		return list;
	}

	/*
	 * Profiling shows that the main cost of ItemStack.getTooltip is, when
	 *  advanced tooltips is enabled, parsing the format strings.
	 *
	 * This wouldn't be worth avoiding, except for the fact that it may be
	 *  called a lot during a text search, especially searching the item
	 *  list (since it re-searches after each character).
	 */
	private List<String> getTooltip(ItemStack stack)
	{
		List<String> list = stack.getTooltip(minecraft.thePlayer, false);

		if(minecraft.gameSettings.advancedItemTooltips)
		{
			String name = list.get(0);
			StringBuilder builder = new StringBuilder();

			if(name.length() > 0)
			{
				builder.append(name);
				builder.append(" (#");
			}

			if(stack.itemID < 1000)
			{
				if(stack.itemID >= 100)
				{
					builder.append("0");
				}
				else if(stack.itemID >= 10)
				{
					builder.append("00");
				}
				else
				{
					builder.append("000");
				}
			}

			builder.append(stack.itemID);

			if(stack.getHasSubtypes())
			{
				builder.append("/");
				builder.append(CommonUtilities.getItemDamage(stack));
			}

			if(name.length() > 0)
			{
				builder.append(")");
			}

			list.set(0, builder.toString());
		}

		return list;
	}
}
