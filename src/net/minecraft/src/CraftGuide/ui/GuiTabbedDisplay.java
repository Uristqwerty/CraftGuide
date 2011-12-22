package net.minecraft.src.CraftGuide.ui;

import java.util.HashMap;
import java.util.Map;

public class GuiTabbedDisplay extends GuiElement implements IButtonListener
{
	private GuiElement currentTab = null;
	private GuiElement changeTab = null;
	private Map<GuiButton, GuiElement> tabMap = new HashMap<GuiButton, GuiElement>();
	
	public GuiTabbedDisplay(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}
	
	public GuiTabbedDisplay(int x, int y, int width, int height, Object[][] tabs)
	{
		this(x, y, width, height);
		
		for(Object[] tab: tabs)
		{
			if(tab[0] instanceof GuiElement && tab[1] instanceof GuiButton)
			{
				addTab((GuiElement)tab[0], (GuiButton)tab[1]);
			}
		}
	}
	
	public GuiTabbedDisplay(int x, int y, int width, int height, Object[] tabs)
	{
		this(x, y, width, height);
		
		for(int i = 0; i < (tabs.length & ~1); i += 2)
		{
			if(tabs[i] instanceof GuiElement && tabs[i + 1] instanceof GuiButton)
			{
				addTab((GuiElement)tabs[i], (GuiButton)tabs[i + 1]);
			}
		}
	}

	public void addTab(GuiElement tab, GuiButton button)
	{
		addElement(button);
		button.addButtonListener(this);
		
		tabMap.put(button, tab);
		
		if(currentTab == null)
		{
			setTab(tab);
		}
	}
	
	@Override
	public void mousePressed(int x, int y)
	{
		super.mousePressed(x, y);
		
		if(changeTab != null)
		{
			setTab(changeTab);
		}
	}

	@Override
	public void onButtonEvent(GuiButton button, Event eventType)
	{
		if(eventType == Event.PRESS)
		{
			changeTab = tabMap.get(button);
		}
	}

	private void setTab(GuiElement tab)
	{
		if(currentTab != null)
		{
			removeElement(currentTab);
		}
		
		if(tab != null)
		{
			addElement(tab);
		}
		
		currentTab = tab;
		changeTab = null;
	}

	@Override
	public void onResize(int oldWidth, int oldHeight)
	{
		for(GuiElement element: tabMap.values())
		{
			if(element != currentTab)
			{
				element.onParentResize(oldWidth, oldHeight, width, height);
			}
		}
		
		super.onResize(oldWidth, oldHeight);
	}
}
