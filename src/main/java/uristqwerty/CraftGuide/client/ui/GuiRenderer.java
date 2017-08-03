package uristqwerty.CraftGuide.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

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
	private double frameStartTime;
	private List<Overlay> overlays = new LinkedList<>();
	private Gui gui;
	private RenderItem itemRenderer = null;

	private static Map<ItemStack, ItemStack> itemStackFixes = new HashMap<>();

	private Renderable itemError = new TexturedRect(-1, -1, 18, 18, DynamicTexture.instance("item_error"), 238, 200);

	public void startFrame(Gui gui)
	{
		this.gui = gui;
		resetValues();
		frameStartTime = Minecraft.getSystemTime() / 1000.0;
		if(itemRenderer == null)
		{
			try
			{
				itemRenderer = (RenderItem)CommonUtilities.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(),
						"renderItem", "field_175621_X", "ab");
			}
			catch(SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
			{
				CraftGuideLog.log(e);
				throw new RuntimeException("Could not get Minecraft's itemRenderer. Probably have the wrong field name.");
			}
		}
	}

	public void endFrame()
	{
		for(Overlay overlay: overlays)
		{
			overlay.renderOverlay(this);
		}

		overlays.clear();

		GlStateManager.color(1, 1, 1, 1);
	}

	public void setColor(int colour, int alpha)
	{
		setColorRgb(colour);
		setAlpha(alpha);
	}

	@Override
	public void setTextureID(int textureID)
	{
		if(textureID != -1)
		{
			GlStateManager.bindTexture(textureID);
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

	/** Adapt from cg.api.Renderer conventions to uq.gui.Renderer conventions */
	@Override
	public void renderText(int x, int y, String text, int textColor, boolean drawShadow)
	{
		int prevColor = currentColor();
		setColor(textColor);
		if(drawShadow)
			drawTextWithShadow(text, x, y);
		else
			drawText(text, x, y);
		setColor(prevColor);
	}

	@Override
	public void drawText(String text, int x, int y)
	{
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, currentColor());
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}

	@Override
	public void drawTextWithShadow(String text, int x, int y)
	{
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, currentColor());
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}

	private int currentColor()
	{
		return ((((int)(alpha * 255)) & 0xff) << 24) | ((((int)(red * 255)) & 0xff) << 16) | ((((int)(green * 255)) & 0xff) << 8) | (((int)(blue * 255)) & 0xff);
	}

	public void drawFloatingText(int x, int y, String text)
	{
		List<String> list = new ArrayList<>(1);
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
				w = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s.substring(2));
			}
			else
			{
				w = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s);
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

		int initialMatrixStackDepth = prepareGlForItemRender(x, y);

		try
		{
			itemStack = renderItem(itemStack, renderOverlay);

			error = false;
		}
		catch(Exception e)
		{
			logItemRenderException(itemStack, e);
		}
		catch(Error e)
		{
			CraftGuideLog.log(e);
			throw e;
		}
		finally
		{
			fixGlState(initialMatrixStackDepth);
		}

		if(error)
		{
			drawItemStackError(x, y);
		}
	}

	private void logItemRenderException(ItemStack itemStack, Exception e)
	{
		if(!hasLogged(itemStack))
		{
			CraftGuideLog.log("Failed to render ItemStack {" + (
					itemStack == null? "null" : (
						"itemID = " + Item.itemRegistry.getNameForObject(itemStack.getItem()) +
						", itemDamage = " + CommonUtilities.getItemDamage(itemStack) +
						", stackSize = " + itemStack.stackSize)) +
					"} (Further stack traces from this particular ItemStack instance will not be logged)");
			CraftGuideLog.log(e);
		}
	}

	private ItemStack renderItem(ItemStack itemStack, boolean renderOverlay)
	{
		if(CommonUtilities.getItemDamage(itemStack) == CraftGuide.DAMAGE_WILDCARD)
		{
			itemStack = fixedItemStack(itemStack);
		}

		itemRenderer.renderItemAndEffectIntoGUI(itemStack, 0, 0);

		if(renderOverlay)
		{
			itemRenderer.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, itemStack, 0, 0, null);
		}

		return itemStack;
	}

	private int prepareGlForItemRender(int x, int y)
	{
		GlStateManager.enableDepth();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0.0F);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableRescaleNormal();
		itemRenderer.zLevel = 100.0F;

		int initialMatrixStackDepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
		return initialMatrixStackDepth;
	}

	private void fixGlState(int initialMatrixStackDepth)
	{
		int finalMatrixStackDepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);

		// If something went wrong, and an exception was thrown after at least one matrix push,
		//  fix it here, so that rendering errors do not affect later parts of the UI.
		while(finalMatrixStackDepth > initialMatrixStackDepth)
		{
			GlStateManager.popMatrix();
			finalMatrixStackDepth--;
		}

		CraftGuide.side.stopTessellating();

		itemRenderer.zLevel = 0.0F;
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableRescaleNormal();
		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
	}

	private HashSet<ItemStack> loggedStacks = new HashSet<>();

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

		x *= Minecraft.getMinecraft().displayHeight / (float)gui.height;
		y *= Minecraft.getMinecraft().displayWidth / (float)gui.width;
		height *= Minecraft.getMinecraft().displayHeight / (float)gui.height;
		width *= Minecraft.getMinecraft().displayWidth / (float)gui.width;

		GL11.glScissor(x, Minecraft.getMinecraft().displayHeight - y - height, width, height);
	}

	public void clearClippingRegion()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public int guiXFromMouseX(int x)
	{
		return (x * gui.width) / Minecraft.getMinecraft().displayWidth;
	}

	public int guiYFromMouseY(int y)
	{
		return gui.height - (y * gui.height) / Minecraft.getMinecraft().displayHeight - 1;
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
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.disableLighting();

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

		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	private void glColor1i(int color)
	{
		setGlColor(
				((color >> 16) & 0xff) / 255.0f,
				((color >>  8) & 0xff) / 255.0f,
				((color >>  0) & 0xff) / 255.0f,
				((color >> 24) & 0xff) / 255.0f);
	}

	private WeakHashMap<ItemStack, Void> invalidStacks = new WeakHashMap<>();

	public List<String> getItemNameandInformation(ItemStack stack)
	{
		if(!invalidStacks.containsKey(stack))
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
		}

		invalidStacks.put(stack, null);
		List<String> list = new ArrayList<>();
		list.add(EnumChatFormatting.YELLOW + "Err: Item " + Item.itemRegistry.getNameForObject(stack.getItem()) + ", damage " + CommonUtilities.getItemDamage(stack));
		return list;
	}

	private List<String> getTooltip(ItemStack stack)
	{
		return stack.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
	}

	@Override
	public double getClock()
	{
		return frameStartTime;
	}
}
