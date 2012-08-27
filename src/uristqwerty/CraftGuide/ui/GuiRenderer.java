package uristqwerty.CraftGuide.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.Gui;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRenderer;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.NamedTexture;
import uristqwerty.CraftGuide.ui.Rendering.GuiTexture;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.ITexture;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.Renderer;
import uristqwerty.gui.minecraft.Image;
import uristqwerty.gui.texture.Texture;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.Tessellator;

public class GuiRenderer extends Renderer implements IRenderer
{
    private RenderItem itemRenderer = new RenderItem();
	private Minecraft minecraft;
	private int u, v;
	private float textureWidth = 256, textureHeight = 256;
	private List<Overlay> overlays = new LinkedList<Overlay>();
	private Gui gui;

	private static Map<ItemStack, ItemStack> itemStackFixes = new HashMap<ItemStack, ItemStack>();
	
	private boolean subtexEnabled = false;
	private int subtex_x, subtex_y, subtex_width, subtex_height;
	
	private IRenderable itemError = new TexturedRect(-1, -1, 18, 18, Image.getImage("/gui/CraftGuide.png"), 238, 200);
	
	public void startFrame(Minecraft mc, Gui gui)
	{
		//System.out.println("startFrame(...)");
		GuiTexture.refreshTextures(mc.renderEngine);
		minecraft = mc;
		this.gui = gui;
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
	
	public void setColour(int colour, int alpha)
	{
		setColour(colour);
		setAlpha(alpha);
	}
	
	public void setColour(int colour)
	{
		setColor((colour >> 16) & 0xff, (colour >> 8) & 0xff, colour & 0xff);
	}

	public void setColor(int red, int green, int blue, int alpha)
	{
		setColor(red, green, blue);
		setAlpha(alpha);
	}

	public void setColor(int red, int green, int blue)
	{
		this.red = red / 255.0;
		this.green = green / 255.0;
		this.blue = blue / 255.0;
	}
	
	public void setAlpha(int alpha)
	{
		this.alpha = alpha / 255.0;
	}

	public void setTexture(ITexture texture)
	{
		texture.setActive(this);
	}
	
	@Override
	public void setTextureID(int textureID)
	{
		clearSubTexture();
		
		if(textureID != -1 && minecraft != null)
		{
			minecraft.renderEngine.bindTexture(textureID);
		}
	}

	public void setSubTexture(int x, int y, int width, int height)
	{
		subtexEnabled = true;
		subtex_x = x;
		subtex_y = y;
		subtex_width = width;
		subtex_height = height;
	}
	
	public void clearSubTexture()
	{
		subtexEnabled = false;
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
        tessellator.setColorRGBA_F((float)red, (float)green, (float)blue, (float)alpha);
        
        if(subtexEnabled)
        {
        	drawSubTexRect(tessellator, x, y, width, height);
        }
        else
        {
        	drawTexturedRect(tessellator, x, y, width, height, 0, 0);
        }
        
        tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawSubTexRect(Tessellator tessellator, int x, int y, int width, int height)
	{
		if(subtex_width < 1 || subtex_height < 1)
		{
			return;
		}
		
		int texX = u % subtex_width;
		int texY = v % subtex_height;
		int rectX = x;
		int rectY = y;
		
		while(rectX < x + width)
		{
			int subWidth = subtex_width - texX;
			
			if(rectX + subWidth > x + width)
			{
				subWidth = x + width - rectX;
			}
			
			while(rectY < y + height)
			{
				int subHeight = subtex_height - texY;
				
				if(rectY + subHeight > y + height)
				{
					subHeight = y + height - rectY;
				}
				
				drawTexturedRect(tessellator, rectX, rectY, subWidth, subHeight, texX + subtex_x, texY + subtex_y);
				
				rectY += subtex_height - texY;
				texY = 0;
			}
			
			texY = v % subtex_height;
			rectY = y;
			
			rectX += subtex_width - texX;
			texX = 0;
		}
	}

	private void drawTexturedRect(Tessellator tessellator, int x, int y, int width, int height, int u, int v)
	{
		u += this.u;
		v += this.v;
		
        tessellator.addVertexWithUV(x		 , y + height, 0, (u) / textureWidth		, (v + height) / textureHeight);
        tessellator.addVertexWithUV(x + width, y + height, 0, (u + width) / textureWidth, (v + height) / textureHeight);
        tessellator.addVertexWithUV(x + width, y		 , 0, (u + width) / textureWidth, (v) / textureHeight);
        tessellator.addVertexWithUV(x		 , y		 , 0, (u) / textureWidth		, (v) / textureHeight);
	}

	public void drawRect(int x, int y, int width, int height)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F((float)red, (float)green, (float)blue, (float)alpha);
        tessellator.addVertex(x		   , y + height, 0);
        tessellator.addVertex(x + width, y + height, 0);
        tessellator.addVertex(x + width, y		   , 0);
        tessellator.addVertex(x		   , y		   , 0);
        tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawGradient(int x, int y, int width, int height, int topColour, int bottomColour)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorRGBA_I(bottomColour & 0xffffff, bottomColour >> 24 & 0xff);
        tessellator.addVertex(x		   , y + height, 0);
        tessellator.addVertex(x + width, y + height, 0);
        
        tessellator.setColorRGBA_I(topColour & 0xffffff, topColour >> 24 & 0xff);
        tessellator.addVertex(x + width, y		   , 0);
        tessellator.addVertex(x		   , y		   , 0);
        
        tessellator.draw();
        
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
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
		
		if(x > xMax)
		{
			x = xMax;
		}
		
		if(x < 3)
		{
			x = 3;
		}
		
		if(y < 4)
		{
			y = 4;
		}
		
    	setColour(0x100010, 0xf0);
		drawRect(x - 3,				y - 4,				textWidth + 6,	1);
		drawRect(x - 3,				y + textHeight + 3,	textWidth + 6,	1);
		drawRect(x - 3,				y - 3,				textWidth + 6,	textHeight + 6);
		drawRect(x - 4,				y - 3,				1,				textHeight + 6);
		drawRect(x + textWidth + 3,	y - 3,				1,				textHeight + 6);
		
		setColour(0x5000ff, 0x50);
		drawRect(x - 3, y - 3, textWidth + 6, 1);
		
		setColour(0x28007f, 0x50);
		drawRect(x - 3, y + textHeight + 2,	textWidth + 6, 1);
		
		drawGradient(x - 3,				y - 2, 1, textHeight + 4, 0x505000ff, 0x5028007f);
		drawGradient(x + textWidth + 2, y - 2, 1, textHeight + 4, 0x505000ff, 0x5028007f);
		
    	setColour(0xFFFFFF, 0xFF);
		
		int textY = y;
		boolean first = true;
		for(String s: text)
		{
        	drawShadowedText(x, textY, s);
        	
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
        
        try
        {
			itemRenderer.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
			
			if(renderOverlay)
			{
				itemRenderer.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
			}
			
			error = false;
        }
        catch(Exception e)
        {
        	if(itemStack != null && itemStack.getItemDamage() == -1)
        	{
        		try
                {
        			itemStack = fixedItemStack(itemStack);
        			
        			itemRenderer.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
        			
        			if(renderOverlay)
        			{
        				itemRenderer.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
        			}
        			
        			error = false;
                }
                catch(Exception e2)
                {
            		if(!hasLogged(itemStack))
            		{
            			CraftGuideLog.log("Failed to render ItemStack {" + (
            					itemStack == null? "null" : (
            						"itemID = " + itemStack.itemID +
            						", itemDamage = " + itemStack.getItemDamage() +
            						", stackSize = " + itemStack.stackSize)) +
            					"} (Further stack traces from this particular ItemStack instance will not be logged)");
            			CraftGuideLog.log(e);
            			CraftGuideLog.log("Additionally, while trying to render a copy with a data value of 0, rather than -1, the following stack trace occurred:");
            			CraftGuideLog.log(e2);
            		}
                }
        	}
        	else
        	{
        		if(!hasLogged(itemStack))
        		{
        			CraftGuideLog.log("Failed to render ItemStack {" + (
        					itemStack == null? "null" : (
        						"itemID = " + itemStack.itemID +
        						", itemDamage = " + itemStack.getItemDamage() +
        						", stackSize = " + itemStack.stackSize)) +
        					"} (Further stack traces from this particular ItemStack instance will not be logged)");
        			CraftGuideLog.log(e);
        		}
        	}
        }
        catch(LinkageError e)
        {
        }

        itemRenderer.zLevel = 0.0F;
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        
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
			stack = new ItemStack(itemStack.itemID, itemStack.stackSize, 0);
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
}
