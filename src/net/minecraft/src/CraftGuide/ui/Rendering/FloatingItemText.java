package net.minecraft.src.CraftGuide.ui.Rendering;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class FloatingItemText implements IRenderable
{
	private List<String> text = new ArrayList<String>(1);;
	
	public FloatingItemText(List<String> text)
	{
		this.text = text;
	}
	
	public FloatingItemText(String text)
	{
		this.text.add(text);
	}
	
	public void setText(String text)
	{
		this.text.clear();
		this.text.add(text);
	}

	public void setText(List<String> text)
	{
		this.text = text;
	}

	@Override
	public void render(GuiRenderer renderer, int xOffset, int yOffset)
	{
		renderer.drawFloatingText(renderer.guiXFromMouseX(Mouse.getX()) + 12, renderer.guiYFromMouseY(Mouse.getY()) - 13, text);
	}
}
