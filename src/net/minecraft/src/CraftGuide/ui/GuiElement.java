package net.minecraft.src.CraftGuide.ui;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.CraftGuide.ui.GuiElement;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;

public class GuiElement
{
	protected int x, y, width, height;
	private GuiElement parent = null;
	private List<GuiElement> children = new LinkedList<GuiElement>();
	
	public GuiElement(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void addElement(GuiElement element)
	{
		element.parent = this;
		children.add(element);
	}
	
	public void draw()
	{
		for(GuiElement element: children)
		{
			element.draw();
		}
	}
	
	public void mouseMoved(int x, int y)
	{
		for(GuiElement element: children)
		{
			element.mouseMoved(x - this.x, y - this.y);
		}
	}
	
	public void mousePressed(int x, int y)
	{
		for(GuiElement element: children)
		{
			element.mousePressed(x - this.x, y - this.y);
		}
	}
	
	public void mouseReleased(int x, int y)
	{
		for(GuiElement element: children)
		{
			element.mouseReleased(x - this.x, y - this.y);
		}
	}

	public void render(IRenderable renderable)
	{
		render(renderable, 0, 0);
	}
	
	public void render(IRenderable renderable, int xOffset, int yOffset)
	{
		if(parent != null && renderable != null)
		{
			parent.render(renderable, xOffset + x, yOffset + y);
		}
	}
	
	public boolean isMouseOver(int mouseX, int mouseY)
	{
		return mouseX >= x
			&& mouseX <  x + width
			&& mouseY >= y
			&& mouseY <  y + height;
	}
}
