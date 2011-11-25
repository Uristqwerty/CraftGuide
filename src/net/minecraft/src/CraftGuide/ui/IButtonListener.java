package net.minecraft.src.CraftGuide.ui;

public interface IButtonListener
{
	enum Event
	{
		PRESS,
		RELEASE
	}
	
	void onButtonEvent(GuiButton button, Event eventType);
}
