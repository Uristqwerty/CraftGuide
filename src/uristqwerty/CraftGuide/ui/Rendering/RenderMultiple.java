package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;

public class RenderMultiple implements IRenderable
{
	IRenderable render[];
	int x, y;
	
	public RenderMultiple(IRenderable render[])
	{
		this(0, 0, render);
	}
	
	public RenderMultiple(int x, int y, IRenderable render[])
	{
		this.x = x;
		this.y = y;
		this.render = render;
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		for(IRenderable renderable: render)
		{
			renderable.render(renderer, xOffset + x, yOffset + y);
		}
	}
}
