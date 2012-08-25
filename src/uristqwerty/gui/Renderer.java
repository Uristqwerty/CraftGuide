package uristqwerty.gui;

import org.lwjgl.opengl.GL11;

import uristqwerty.gui.texture.Texture;

public abstract class Renderer
{
	protected double red;
	protected double green;
	protected double blue;
	protected double alpha;
	
	public abstract void setTextureID(int textureID);

	public void drawTexturedRect(
			int x, int y, int width, int height,
			double u, double v, double u2, double v2)
	{
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

}
