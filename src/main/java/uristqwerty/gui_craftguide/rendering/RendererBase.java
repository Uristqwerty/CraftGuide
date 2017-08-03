package uristqwerty.gui_craftguide.rendering;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import uristqwerty.gui_craftguide.Color;
import uristqwerty.gui_craftguide.texture.Texture;

/**
 * Defines the core functionality required to render GUI components.
 */
public abstract class RendererBase
{
	protected float red;
	protected float green;
	protected float blue;
	protected float alpha;

	protected float redModifier;
	protected float greenModifier;
	protected float blueModifier;
	protected float alphaModifier;

	public static RendererBase instance;

	/** Returns the time that the current frame started rendering, measured in
	 * seconds since an arbitrary reference point */
	public abstract double getClock();

	public abstract void setTextureID(int textureID);
	public abstract void drawText(String text, int x, int y);
	public abstract void drawTextWithShadow(String text, int x, int y);

	public void resetValues()
	{
		red = 1.0f;
		green = 1.0f;
		blue = 1.0f;
		alpha = 1.0f;
		redModifier = 1.0f;
		greenModifier = 1.0f;
		blueModifier = 1.0f;
		alphaModifier = 1.0f;
	}

	public void drawRect(int x, int y, int width, int height)
	{
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.disableLighting();

		setGlColor(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(x, y);
			GL11.glVertex2i(x, y + height);
			GL11.glVertex2i(x + width, y + height);
			GL11.glVertex2i(x + width, y);
		GL11.glEnd();

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
	}

	public void drawTexturedRect(
			int x, int y, int width, int height,
			double u, double v, double u2, double v2)
	{
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.disableLighting();

		setGlColor(red, green, blue, alpha);
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

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
	}

	public void drawTexturedRect(Texture texture,
			int x, int y, int width, int height, int u, int v)
	{
		texture.renderRect(this, x, y, width, height, u, v);
	}

	public void setColorModifier(float red, float green, float blue, float alpha)
	{
		redModifier = red;
		greenModifier = green;
		blueModifier = blue;
		alphaModifier = alpha;
	}

	public void setColorModifierv(float v[])
	{
		redModifier = v[0];
		greenModifier = v[1];
		blueModifier = v[2];
		alphaModifier = v[3];
	}

	public void setColorModifierv(Color color)
	{
		redModifier = color.red / 255.0f;
		greenModifier = color.green / 255.0f;
		blueModifier = color.blue / 255.0f;
		alphaModifier = color.alpha / 255.0f;
	}

	public void getColorModifierv(float v[])
	{
		v[0] = red;
		v[1] = green;
		v[2] = blue;
		v[3] = alpha;
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
		this.red = red / 255.0f;
		this.green = green / 255.0f;
		this.blue = blue / 255.0f;
	}

	public void setAlpha(int alpha)
	{
		this.alpha = alpha / 255.0f;
	}

	public void setGlColor(float red, float green, float blue, float alpha)
	{
		GlStateManager.color(red * redModifier, green * greenModifier, blue * blueModifier, alpha * alphaModifier);
	}
}
