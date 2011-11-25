package net.minecraft.src.CraftGuide.ui;


public class GuiScrollBar extends GuiElement implements IButtonListener, ISliderListener
{
	private GuiSlider handle;
	private float min = 0, max = 1, value = 0;
	private float scrollMultiplier = 1;

	public GuiScrollBar(int x, int y, int width, int height, GuiSlider handle, GuiValueButton[] scrollButtons)
	{
		super(x, y, width, height);
		
		this.handle = handle;
		addElement(handle);
		handle.addSliderListener(this);
		
		for(GuiButton button: scrollButtons)
		{
			addElement(button);
			button.addButtonListener(this);
		}
	}
	
	public void setScale(float min, float max)
	{
		float value = this.max > 0? (this.value - this.min) / (this.max - this.min) : 0;
		handle.setValue(0, value);
		
		this.min = min;
		this.max = max;
		this.value = (value * (max - min)) + min;
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
    		if(button instanceof GuiValueButton)
    		{
    			scroll(((GuiValueButton) button).getValue(), true);
    		}
		}
	}
	
	public void scroll(int lines)
	{
		scroll(lines, false);
	}
	
	public void scroll(int lines, boolean align)
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
	
	public void setScrollMultiplier(float multiplier)
	{
		scrollMultiplier = multiplier;
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
}
