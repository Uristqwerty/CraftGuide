package net.minecraft.src.CraftGuide.ui;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.Tessellator;
import net.minecraft.src.CraftGuide.GuiCraftGuide;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.ITexture;
import net.minecraft.src.CraftGuide.ui.Rendering.Overlay;

public class GuiRenderer
{
    private RenderItem itemRenderer = new RenderItem();
	private int currentTexture = -1;
	private Minecraft minecraft;
	private int u, v;
	private float textureWidth = 256, textureHeight = 256;
	private List<Overlay> overlays = new LinkedList<Overlay>();
	private int colour, alpha;
	private GuiCraftGuide guiCraftGuide;
	
	public void startFrame(Minecraft mc, GuiCraftGuide gui)
	{
		currentTexture = -1;
		minecraft = mc;
		guiCraftGuide = gui;
		u = 0;
		v = 0;
    	setColour(0xFFFFFF, 0xFF);
	}

	public void endFrame()
	{
		for(Overlay overlay: overlays)
		{
			overlay.renderOverlay(this);
		}
		
		overlays.clear();
	}
	
	public void setColour(int colour)
	{
		this.colour = colour;
	}
	
	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
	}
	
	public void setColour(int colour, int alpha)
	{
		setColour(colour);
		setAlpha(alpha);
	}

	public void setTexture(ITexture texture)
	{
		texture.setActive(this);
	}
	public void setTextureID(int textureID)
	{
		if(textureID != currentTexture && textureID != -1 && minecraft != null)
		{
			minecraft.renderEngine.bindTexture(textureID);
		}
	}

	public void setTextureCoords(int u, int v)
	{
		this.u = u;
		this.v = v;
	}

	public void drawTexturedRect(int x, int y, int width, int height)
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(colour, alpha);
        tessellator.addVertexWithUV(x		 , y + height, 0, (u) / textureWidth		, (v + height) / textureHeight);
        tessellator.addVertexWithUV(x + width, y + height, 0, (u + width) / textureWidth, (v + height) / textureHeight);
        tessellator.addVertexWithUV(x + width, y		 , 0, (u + width) / textureWidth, (v) / textureHeight);
        tessellator.addVertexWithUV(x		 , y		 , 0, (u) / textureWidth		, (v) / textureHeight);
        tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawRect(int x, int y, int width, int height)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(colour, alpha);
        tessellator.addVertex(x		   , y + height, 0);
        tessellator.addVertex(x + width, y + height, 0);
        tessellator.addVertex(x + width, y		   , 0);
        tessellator.addVertex(x		   , y		   , 0);
        tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void render(IRenderable renderable, int xOffset, int yOffset)
	{
		renderable.render(this, xOffset, yOffset);
	}

	public void overlay(Overlay overlay)
	{
		overlays.add(overlay);
	}

	public void drawShadowedText(int x, int y, String text)
	{
		minecraft.fontRenderer.drawStringWithShadow(text, x, y, 0xffffffff);
	}

	public void drawText(int x, int y, String text)
	{
		minecraft.fontRenderer.drawString(text, x, y, 0xffffffff);
	}

	public void drawText(int x, int y, String text, int color)
	{
		minecraft.fontRenderer.drawString(text, x, y, color);
	}

	public void drawRightAlignedText(int x, int y, String text, int color)
	{
		drawText(x - minecraft.fontRenderer.getStringWidth(text), y, text, color);
	}

	public void drawFloatingText(int x, int y, String text)
	{
    	setColour(0x000000, 0xC0);
		drawRect(x, y, minecraft.fontRenderer.getStringWidth(text) + 6, 13);
    	setColour(0xFFFFFF, 0xFF);
		drawShadowedText(x + 3, y + 3, text);
	}

	public void drawItemStack(ItemStack itemStack, int x, int y, boolean renderOverlay)
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        
		itemRenderer.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
		
		if(renderOverlay)
		{
			itemRenderer.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
		}
		
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
	}

	public void setClippingRegion(int x, int y, int width, int height)
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		x *= minecraft.displayHeight / (float)guiCraftGuide.height;
		y *= minecraft.displayWidth / (float)guiCraftGuide.width;
		height *= minecraft.displayHeight / (float)guiCraftGuide.height;
		width *= minecraft.displayWidth / (float)guiCraftGuide.width;
		
		GL11.glScissor(x, minecraft.displayHeight - y - height, width, height);
	}

	public void clearClippingRegion()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public int guiXFromMouseX(int x)
	{
        return (x * guiCraftGuide.width) / minecraft.displayWidth;
	}

	public int guiYFromMouseY(int y)
	{
        return guiCraftGuide.height - (y * guiCraftGuide.height) / minecraft.displayHeight - 1;
	}
}
