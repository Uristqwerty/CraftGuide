package uristqwerty.CraftGuide.ui.Rendering;

import uristqwerty.CraftGuide.ui.GuiRenderer;

public class Overlay implements IRenderable
{
	private IRenderable renderable;
	private int x, y;
	
	public Overlay(IRenderable renderable)
	{
		this.renderable = renderable;
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.overlay(this);
		x = xOffset;
		y = yOffset;
	}
	
	public void renderOverlay(GuiRenderer renderer)
	{
		renderable.render(renderer, x, y);
	}
}
