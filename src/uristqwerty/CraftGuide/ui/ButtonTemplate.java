package uristqwerty.CraftGuide.ui;

import java.util.HashMap;
import java.util.Map;

import uristqwerty.CraftGuide.ui.GuiButton.ButtonState;
import uristqwerty.CraftGuide.ui.Rendering.GuiTexture;
import uristqwerty.CraftGuide.ui.Rendering.IRenderable;


public class ButtonTemplate
{
	private Map<ButtonState, IRenderable> stateImages = new HashMap<ButtonState, IRenderable>();
	
	public ButtonTemplate()
	{
	}
	
	public ButtonTemplate(GuiTexture texture, int u, int v, int width, int height, int dx, int dy)
	{
		this(texture, u, v, width, height, 0, 2, dx, dy);
	}
	
	public ButtonTemplate(GuiTexture texture, int u, int v, int width, int height, int spacing, int borderLength, int dx, int dy)
	{
		int yOffset = 0;
		int xOffset = 0;
		
		for(ButtonState state: ButtonState.values())
		{
			IRenderable image = new GuiBorderedRect(0, 0, 10, 10,
					texture, u + xOffset, v + yOffset, borderLength, borderLength,
					width - (borderLength + spacing) * 2, height - (borderLength + spacing) * 2, spacing);
			
			setStateImage(state, image);
			xOffset += dx;
			yOffset += dy;
		}
		
	}
	
	public IRenderable getStateImage(ButtonState state, int width, int height)
	{
		IRenderable image = stateImages.get(state);
		
		if(image != null)
		{
			if(image instanceof GuiElement)
			{
				((GuiElement)image).setSize(width, height);
			}
		}
		
		return image;
	}
	
	public ButtonTemplate setStateImage(ButtonState state, IRenderable image)
	{
		stateImages.put(state, image);
		
		return this;
	}
}
