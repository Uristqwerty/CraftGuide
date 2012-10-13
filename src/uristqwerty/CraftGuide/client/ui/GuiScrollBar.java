package uristqwerty.CraftGuide.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import uristqwerty.gui.components.GuiElement;


public class GuiScrollBar extends GuiElement implements IButtonListener, ISliderListener
{
	private GuiSlider handle;
	private float min = 0, max = 1, value = 0;
	private static float scrollMultiplier = 1;
	private int pageSize;
	private Map<GuiElement, Object[]> buttons = new HashMap<GuiElement, Object[]>();

	public GuiScrollBar(int x, int y, int width, int height, GuiSlider handle/*, GuiValueButton[] scrollButtons*/)
	{
		super(x, y, width, height);
		
		this.handle = handle;
		addElement(handle);
		handle.addSliderListener(this);
	}
	
	public GuiScrollBar addButton(GuiButton button, int scrollValue, boolean scrollPages)
	{
		addElement(button);
		button.addButtonListener(this);
		buttons.put(button, new Object[]{scrollValue, scrollPages});
		
		return this;
	}
	
	public GuiElement setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
		
		return this;
	}
	
	public GuiScrollBar setScale(float min, float max)
	{
		float value = this.max > 0? (this.value - this.min) / (this.max - this.min) : 0;
		handle.setValue(0, value);
		
		this.min = min;
		this.max = max;
		this.value = (value * (max - min)) + min;
		
		return this;
	}
	
	public float getValue()
	{
		return value;
	}

	@Override
	public void onButtonEvent(GuiButton button, Event eventType)
	{
		if(eventType == Event.PRESS)
		{
			if(buttons.containsKey(button))
			{
				Object[] data = buttons.get(button);
				
				if((Boolean)data[1])
				{
					scrollPages((Integer)data[0]);
				}
				else
				{
					scrollLines((Integer)data[0]);
				}
			}
		}
	}
	
	@Override
	public void onKeyTyped(char eventChar, int eventKey)
	{
		super.onKeyTyped(eventChar, eventKey);
		
    	switch(eventKey)
    	{
    		case Keyboard.KEY_UP:
    			scrollLines(-1, true);
    			break;
    			
    		case Keyboard.KEY_DOWN:
    			scrollLines(1, true);
    			break;
    			
    		case Keyboard.KEY_LEFT:
    		case Keyboard.KEY_PRIOR:
    			scrollPages(-1);
    			break;
    			
    		case Keyboard.KEY_RIGHT:
    		case Keyboard.KEY_NEXT:
    			scrollPages(1);
    			break;
    			
    		case Keyboard.KEY_HOME:
    			scrollToStart();
    			break;
    			
    		case Keyboard.KEY_END:
    			scrollToEnd();
    			break;
    	}
	}
	
	@Override
	public void scrollWheelTurned(int change)
	{
		scrollLines(change, true);
		
		super.scrollWheelTurned(change);
	}

	public void scrollPages(int pages)
	{
		scrollLines(pages * pageSize, true);
	}

	public void scrollLines(int lines)
	{
		scrollLines(lines, false);
	}
	
	public void scrollLines(int lines, boolean align)
	{
		float newValue = value + lines * scrollMultiplier;
		
		if(align)
		{
			newValue = (int)newValue;
		}
		
		setValue(newValue);
	}
	
	public void scrollToStart()
	{
		setValue(min);
	}
	
	public void scrollToEnd()
	{
		setValue(max);
	}

	@Override
	public void onSliderMoved(GuiSlider slider)
	{
		setValue(slider.getPosY() * max + min);
	}

	private void setValue(float value)
	{
		if(value < min)
		{
			value = min;
		}
		
		if(value > max)
		{
			value = max;
		}
		
		this.value = value;
		handle.setValue(0, (value - min) / (max - min));
	}

	public float getMax()
	{
		return max;
	}

	public static void setScrollMultiplier(int i)
	{
		scrollMultiplier = i;
	}
}
