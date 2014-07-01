package uristqwerty.gui_craftguide.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import uristqwerty.gui_craftguide.rendering.Renderable;
import uristqwerty.gui_craftguide.rendering.RendererBase;

public class MultilineText implements Renderable
{
	protected int x, y;
	protected String[] text;
	protected int color;
	protected int lineSpacing;

	public MultilineText(int x, int y, String[] text, int lineSpacing, int color)
	{
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		this.lineSpacing = lineSpacing;
	}

	public MultilineText(int x, int y, String[] text, int lineSpacing)
	{
		this(x, y, text, lineSpacing, 0xff000000);
	}

	public MultilineText(int x, int y, String[] text)
	{
		this(x, y, text, 10, 0xff000000);
	}

	@Override
	public void render(RendererBase renderer, int x, int y)
	{
		renderer.setColor(color);

		int lineY = 0;
		for(String line: text)
		{
			renderer.drawText(line, x + this.x, y + this.y + lineY);
			lineY += lineSpacing;
		}
		renderer.setColor(0xffffffff);
	}

	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void setText(String[] text)
	{
		this.text = text;
	}

	public int textHeight()
	{
		return 8;
	}

	public int textWidth()
	{
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		int width = 0;

		for(String line: text)
		{
			width = Math.max(width, fontRenderer.getStringWidth(line));
		}

		return width;
	}
}
