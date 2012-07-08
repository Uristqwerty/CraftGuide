package uristqwerty.CraftGuide.ui;

import java.util.LinkedList;
import java.util.List;

import uristqwerty.CraftGuide.ui.Rendering.FloatingItemText;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;
import uristqwerty.CraftGuide.ui.Rendering.ITexture;
import uristqwerty.CraftGuide.ui.Rendering.Overlay;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;


public class GuiButton extends GuiElement
{
	protected enum ButtonState
	{
		UP,
		UP_OVER,
		DOWN,
		DOWN_OVER,
	}
	
	private List<IButtonListener> buttonListeners = new LinkedList<IButtonListener>();
	ButtonTemplate template = new ButtonTemplate();
	
	private ButtonState currentState = ButtonState.UP;

	private static FloatingItemText toolTip = new FloatingItemText("");
	private static Overlay toolTipRender = new Overlay(toolTip);
	private String toolTipText = "";

	public GuiButton(int x, int y, int width, int height, ITexture texture, int u, int v)
	{
		this(x, y, width, height, texture, u, v, width, 0);
	}
	
	public GuiButton(int x, int y, int width, int height, ITexture texture, int u, int v, int dx, int dy)
	{
		super(x, y, width, height);
		
		int yOffset = 0;
		int xOffset = 0;
		for(ButtonState state: ButtonState.values())
		{
			IRenderable image = new TexturedRect(0, 0, width, height, texture, u + xOffset, v + yOffset);
			
			template.setStateImage(state, image);
			xOffset += dx;
			yOffset += dy;
		}
	}

	protected GuiButton(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}
	
	public GuiButton(int x, int y, int width, int height, ButtonTemplate template)
	{
		super(x, y, width, height);
		
		this.template = template;
	}
	
	public GuiButton(int x, int y, int width, int height, ButtonTemplate template, String text)
	{
		this(x, y, width, height, template);
		
		addElement(
			new GuiCentredText(0, 0, width, height, text)
				.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT));
	}

	@Override
	public void draw()
	{
		render(template.getStateImage(currentState, width, height));
		
		if(isOver() && !toolTipText.isEmpty())
		{
			toolTip.setText(toolTipText);
			render(toolTipRender);
		}
		
		super.draw();
	}
	
	@Override
	public void mouseMoved(int x, int y)
	{
		updateState(isMouseOver(x, y), isHeld());
	}

	@Override
	public void mousePressed(int x, int y)
	{
		if(isMouseOver(x, y))
		{
			if(!isHeld())
			{
				sendButtonEvent(IButtonListener.Event.PRESS);
			}
			updateState(true, true);
		}
	}

	@Override
	public void mouseReleased(int x, int y)
	{
		if(isHeld())
		{
			sendButtonEvent(IButtonListener.Event.RELEASE);
		}
		
		updateState(isMouseOver(x, y), false);
	}
	
	private void updateState(boolean over, boolean held)
	{
		currentState = 
			held?
				over?
					ButtonState.DOWN_OVER
				:
					ButtonState.DOWN
			:
				over?
					ButtonState.UP_OVER
				:
					ButtonState.UP;
	}
	
	protected boolean isHeld()
	{
		return currentState == ButtonState.DOWN || currentState == ButtonState.DOWN_OVER;
	}
	
	protected boolean isOver()
	{
		return currentState == ButtonState.UP_OVER || currentState == ButtonState.DOWN_OVER;
	}
	
	public GuiButton addButtonListener(IButtonListener listener)
	{
		buttonListeners.add(listener);
		
		return this;
	}
	
	private void sendButtonEvent(IButtonListener.Event eventType)
	{
		for(IButtonListener listener: buttonListeners)
		{
			listener.onButtonEvent(this, eventType);
		}
	}
	
	public GuiButton setToolTip(String text)
	{
		toolTipText = text;
		
		return this;
	}
}
