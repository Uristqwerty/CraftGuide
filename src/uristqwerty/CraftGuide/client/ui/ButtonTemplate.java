package uristqwerty.CraftGuide.client.ui;

import java.util.HashMap;
import java.util.Map;

import uristqwerty.CraftGuide.client.ui.GuiButton.ButtonState;
import uristqwerty.gui.components.GuiElement;
import uristqwerty.gui.rendering.Renderable;
import uristqwerty.gui.texture.Texture;

public class ButtonTemplate
{
	private Map<ButtonState, Renderable> stateImages = new HashMap<ButtonState, Renderable>();
	
	public ButtonTemplate()
	{
	}
	
	public ButtonTemplate(Texture texture, int u, int v, int width, int height, int dx, int dy)
	{
		this(texture, u, v, width, height, 0, 2, dx, dy);
	}
	
	public ButtonTemplate(Texture texture, int u, int v, int width, int height, int spacing, int borderLength, int dx, int dy)
	{
		int yOffset = 0;
		int xOffset = 0;
		
		for(ButtonState state: ButtonState.values())
		{
			Renderable image = new GuiBorderedRect(0, 0, 10, 10,
					texture, u + xOffset, v + yOffset, borderLength, borderLength,
					width - (borderLength + spacing) * 2, height - (borderLength + spacing) * 2, spacing);
			
			setStateImage(state, image);
			xOffset += dx;
			yOffset += dy;
		}
		
	}
	
	public Renderable getStateImage(ButtonState state, int width, int height)
	{
		Renderable image = stateImages.get(state);
		
		if(image != null)
		{
			if(image instanceof GuiElement)
			{
				((GuiElement)image).setSize(width, height);
			}
		}
		
		return image;
	}
	
	public ButtonTemplate setStateImage(ButtonState state, Renderable image)
	{
		stateImages.put(state, image);
		
		return this;
	}
}
