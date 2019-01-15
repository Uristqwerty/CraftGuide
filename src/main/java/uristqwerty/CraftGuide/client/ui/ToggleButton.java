package uristqwerty.CraftGuide.client.ui;


public class ToggleButton extends GuiButton
{
	private boolean disabledChecked = false;

	public ToggleButton(int x, int y, int width, int height, ButtonTemplate template)
	{
		super(x, y, width, height, template);
	}

	@Override
	public void mouseMoved(int x, int y)
	{
		updateState(containsPoint(x, y), isHeld());
	}

	@Override
	public void mousePressed(int x, int y)
	{
		if(containsPoint(x, y) && !isDisabled())
		{
			if(!isHeld())
			{
				sendButtonEvent(IButtonListener.Event.PRESS);
			}
			else
			{
				sendButtonEvent(IButtonListener.Event.RELEASE);
			}

			updateState(true, !isHeld());
		}
	}

	@Override
	public void mouseReleased(int x, int y)
	{
	}

	public ToggleButton setState(ButtonState state)
	{
		currentState = state;
		return this;
	}

	@Override
	public void setDisabled(boolean disabled)
	{
		if(disabled != isDisabled())
		{
			if(disabled)
			{
				disabledChecked = isHeld();
				super.setDisabled(disabled);
			}
			else
			{
				super.setDisabled(disabled);
				updateState(isOver(), disabledChecked);
			}
		}
	}
}
