package net.minecraft.src.CraftGuide.ui.Rendering;

import net.minecraft.src.CraftGuide.ui.GuiRenderer;

public class GuiSubTexture implements ITexture
{
	private GuiTexture texture;
	
	public GuiSubTexture(GuiTexture texture)
	{
		this.texture = texture;
	}

	@Override
	public void setActive(GuiRenderer renderer)
	{
		texture.setActive(renderer);
	}
}
