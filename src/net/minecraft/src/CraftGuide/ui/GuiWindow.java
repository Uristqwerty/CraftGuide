package net.minecraft.src.CraftGuide.ui;

import net.minecraft.src.CraftGuide.ui.GuiElement;
import net.minecraft.src.CraftGuide.ui.GuiRenderer;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;

public class GuiWindow extends GuiElement
{
	private GuiRenderer renderer;
	private boolean mousePressed;
	
	public GuiWindow(int x, int y, int width, int height, GuiRenderer renderer)
	{
		super(x, y, width, height);
		
		this.renderer = renderer;
	}

	public void centerOn(int centerX, int centerY)
	{
		x = centerX - (width / 2);
		y = centerY - (height / 2);
	}

	@Override
	public void render(IRenderable renderable, int xOffset, int yOffset)
	{
		renderer.render(renderable, xOffset + x, yOffset + y);
	}

	public void updateMouse(int x, int y)
	{
		mouseMoved(x, y);
	}

	public void updateMouseState(int x, int y, boolean buttonState)
	{
		if(mousePressed != buttonState)
		{
			mousePressed = buttonState;
			
			if(buttonState)
			{
				mousePressed(x, y);
			}
			else
			{
				mouseReleased(x, y);
			}
		}
	}
}
