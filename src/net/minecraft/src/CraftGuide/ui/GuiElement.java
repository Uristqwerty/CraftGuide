package net.minecraft.src.CraftGuide.ui;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.CraftGuide.ui.GuiElement;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;

public class GuiElement
{
	protected int x, y, width, height;
	private GuiElement parent = null;
	private List<GuiElement> children = new LinkedList<GuiElement>();
	
	public enum AnchorPoint
	{
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
	}
	
	private AnchorPoint anchorTL = AnchorPoint.TOP_LEFT;
	private AnchorPoint anchorBR = AnchorPoint.TOP_LEFT;
	
	public GuiElement(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public GuiElement addElement(GuiElement element)
	{
		element.parent = this;
		children.add(element);
		
		return this;
	}

	public void removeElement(GuiElement element)
	{
		element.parent = null;
		children.remove(element);
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
	
	public GuiElement setSize(int width, int height)
	{
		int oldWidth = this.width;
		int oldHeight = this.height;
		this.width = width;
		this.height = height;
		
		onResize(oldWidth, oldHeight);
		
		for(GuiElement element: children)
		{
			element.onParentResize(oldWidth, oldHeight, width, height);
		}
		
		return this;
	}

	public void onParentResize(int oldWidth, int oldHeight, int newWidth, int newHeight)
	{
		int x1 = x;
		int y1 = y;
		int x2 = x + this.width;
		int y2 = y + this.height;
		
		if(anchorTL == AnchorPoint.TOP_RIGHT || anchorTL == AnchorPoint.BOTTOM_RIGHT)
		{
			x1 += newWidth - oldWidth;
		}
		
		if(anchorTL == AnchorPoint.BOTTOM_LEFT || anchorTL == AnchorPoint.BOTTOM_RIGHT)
		{
			y1 += newHeight - oldHeight;
		}
		
		if(anchorBR == AnchorPoint.TOP_RIGHT || anchorBR == AnchorPoint.BOTTOM_RIGHT)
		{
			x2 += newWidth - oldWidth;
		}
		
		if(anchorBR == AnchorPoint.BOTTOM_LEFT || anchorBR == AnchorPoint.BOTTOM_RIGHT)
		{
			y2 += newHeight - oldHeight;
		}
		
		if(x1 != x || y1 != y)
		{
			setPosition(x1, y1);
		}
		
		if(x2 - x1 != width || y2 - y1 != height)
		{
			setSize(x2 - x1, y2 - y1);
		}
	}
	
	public void onResize(int oldWidth, int oldHeight)
	{
	}
	
	public GuiElement setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		onMove();
		
		for(GuiElement element: children)
		{
			element.onParentMove();
		}
		
		return this;
	}

	public void onParentMove()
	{
	}
	
	public void onMove()
	{
	}
	
	public GuiElement anchor(AnchorPoint topLeft, AnchorPoint bottomRight)
	{
		anchorTL = topLeft;
		anchorBR = bottomRight;
		
		return this;
	}

	public GuiElement anchor(AnchorPoint point)
	{
		return anchor(point, point);
	}
	
	public int absoluteX()
	{
		if(parent == null)
		{
			return x;
		}
		else
		{
			return parent.absoluteX() + x;
		}
	}
	
	public int absoluteY()
	{
		if(parent == null)
		{
			return y;
		}
		else
		{
			return parent.absoluteY() + y;
		}
	}

	public void onKeyTyped(char eventChar, int eventKey)
	{
		for(GuiElement element: children)
		{
			element.onKeyTyped(eventChar, eventKey);
		}
	}
	
	public void scrollWheelTurned(int change)
	{
		for(GuiElement element: children)
		{
			element.scrollWheelTurned(change);
		}
	}

	public void onGuiClosed()
	{
		for(GuiElement element: children)
		{
			element.onGuiClosed();
		}
	}
}
