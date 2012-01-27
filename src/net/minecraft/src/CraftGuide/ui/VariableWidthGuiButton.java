package net.minecraft.src.CraftGuide.ui;

import net.minecraft.src.CraftGuide.ui.Rendering.GuiSubTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.ITexture;
import net.minecraft.src.CraftGuide.ui.Rendering.RenderMultiple;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class VariableWidthGuiButton extends GuiButton
{
	public VariableWidthGuiButton(int x, int y, int width, int height,
			GuiTexture texture, int u, int v, int texWidth, int texHeight, int dx, int dy)
	{
		super(x, y, width, height, genImages(texture, u, v, texWidth, texHeight, dx, dy, width));
	}

	private static IRenderable[] genImages(GuiTexture texture, int u, int v,
			int texWidth, int texHeight, int dx, int dy, int buttonWidth)
	{
		IRenderable images[] = {
			genImage(texture, u + dx * 0, v + dy * 0, texWidth, texHeight, buttonWidth),
			genImage(texture, u + dx * 1, v + dy * 1, texWidth, texHeight, buttonWidth),
			genImage(texture, u + dx * 2, v + dy * 2, texWidth, texHeight, buttonWidth),
			genImage(texture, u + dx * 3, v + dy * 3, texWidth, texHeight, buttonWidth)};
		
		return images;
	}

	private static IRenderable genImage(GuiTexture texture, int u, int v, int texWidth, int texHeight, int buttonWidth)
	{
		GuiSubTexture subTexture = new GuiSubTexture(texture, u + 4, v, texWidth - 8, texHeight);
		return
			new RenderMultiple( 
				new IRenderable[] {
					new TexturedRect(0, 0, 4, texHeight, texture, u, v),
					new TexturedRect(4, 0, buttonWidth - 8, texHeight, subTexture, 0, 0),
					new TexturedRect(buttonWidth - 4, 0, 4, texHeight, texture, u + texWidth - 4, v)
				});
	}
}
