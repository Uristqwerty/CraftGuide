package uristqwerty.CraftGuide.client.ui;

public interface IButtonListener
{
	enum Event
	{
		PRESS,
		RELEASE,
		RELEASE_DISABLED,
	}

	void onButtonEvent(GuiButton button, Event eventType);
}
