package uristqwerty.CraftGuide.ui;

import java.lang.Math;

import uristqwerty.CraftGuide.CraftGuide;


public class GuiResizeHandle extends GuiElement
{
	private AnchorPoint corner;
	private boolean dragging = false;
	private int targetX, targetY, targetOffsetX, targetOffsetY;

	private int minWidth, minHeight;
	private GuiElement target;

	public GuiResizeHandle(int x, int y, int width, int height, GuiElement element)
	{
		this(x, y, width, height, element, AnchorPoint.BOTTOM_RIGHT);
	}
	
	public GuiResizeHandle(int x, int y, int width, int height, GuiElement element, AnchorPoint corner)
	{
		this(x, y, width, height, element, corner, element.width, element.height);
	}
	
	public GuiResizeHandle(int x, int y, int width, int height,
		GuiElement element, int minimumWidth, int minimumHeight)
	{
		this(x, y, width, height, element, AnchorPoint.BOTTOM_RIGHT, minimumWidth, minimumHeight);
	}
	
	public GuiResizeHandle(int x, int y, int width, int height,
		GuiElement element, AnchorPoint corner, int minimumWidth, int minimumHeight)
	{
		super(x, y, width, height);
		
		this.corner = corner;
		target = element;
		minWidth = minimumWidth;
		minHeight = minimumHeight;
		
		anchor(corner, corner);
	}

	@Override
	public void mouseMoved(int x, int y)
	{
		super.mouseMoved(x, y);

		if(dragging)
		{
			targetX = absoluteX() + x - this.x - targetOffsetX;
			targetY = absoluteY() + y - this.y - targetOffsetY;
		}
	}

	@Override
	public void mousePressed(int x, int y)
	{
		super.mousePressed(x, y);
		
		if(isMouseOver(x, y))
		{
			dragging = true;
			targetX = absoluteX();
			targetY = absoluteY();
			targetOffsetX = x - this.x;
			targetOffsetY = y - this.y;
		}
	}

	@Override
	public void mouseReleased(int x, int y)
	{
		super.mouseReleased(x, y);
		
		dragging = false;
	}
	
	@Override
	public void draw()
	{
		if(dragging)
		{
			int xDif = targetX - absoluteX();
			int yDif = targetY - absoluteY();
			
			if(CraftGuide.resizeRate > 0)
			{
				int xDir = (int)Math.signum(xDif);
				int yDir = (int)Math.signum(yDif);
				
				xDif = Math.min(Math.abs(xDif), CraftGuide.resizeRate) * xDir;
				yDif = Math.min(Math.abs(yDif), CraftGuide.resizeRate) * yDir;
			}
			
			if(xDif != 0 || yDif != 0)
			{
				if(corner == AnchorPoint.TOP_LEFT || corner == AnchorPoint.TOP_RIGHT)
				{
					yDif = -yDif;
				}
				
				if(corner == AnchorPoint.TOP_LEFT || corner == AnchorPoint.BOTTOM_LEFT)
				{
					xDif = -xDif;
				}
				
				target.setSize(
					Math.max(target.width  + xDif, minWidth),
					Math.max(target.height + yDif, minHeight));
			}
		}
		
		super.draw();
	}
}
