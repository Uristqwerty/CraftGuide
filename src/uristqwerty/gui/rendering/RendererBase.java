package uristqwerty.gui.rendering;

import org.lwjgl.opengl.GL11;

import uristqwerty.gui.texture.Texture;

/**
 * Defines the core functionality required to render GUI components.
 */
public abstract class RendererBase
{
	protected double red;
	protected double green;
	protected double blue;
	protected double alpha;
	
	public static RendererBase instance;
	
	public abstract void setTextureID(int textureID);
	public abstract void drawText(String text, int x, int y);
	public abstract void drawTextWithShadow(String text, int x, int y);
	
	public void drawRect(int x, int y, int width, int height)
	{
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(770, 771);
	    
	    GL11.glColor4d(red, green, blue, alpha);
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex2i(x, y);
	        GL11.glVertex2i(x, y + height);
	        GL11.glVertex2i(x + width, y + height);
	        GL11.glVertex2i(x + width, y);
	    GL11.glEnd();
	    
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void drawTexturedRect(
			int x, int y, int width, int height,
			double u, double v, double u2, double v2)
	{
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(770, 771);
	    
	    GL11.glColor4d(red, green, blue, alpha);
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
	    
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawTexturedRect(Texture texture,
			int x, int y, int width, int height, int u, int v)
	{
		texture.renderRect(this, x, y, width, height, u, v);
	}
	
	public void setColorRgb(int colour)
	{
		setColor((colour >> 16) & 0xff, (colour >> 8) & 0xff, colour & 0xff);
	}
	
	public void setColor(int color)
	{
		setColor((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff, (color >> 24) & 0xff);
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
}
