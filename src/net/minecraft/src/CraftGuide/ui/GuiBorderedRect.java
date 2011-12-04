package net.minecraft.src.CraftGuide.ui;

import net.minecraft.src.CraftGuide.ui.Rendering.GuiSubTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.GuiTexture;
import net.minecraft.src.CraftGuide.ui.Rendering.IRenderable;
import net.minecraft.src.CraftGuide.ui.Rendering.ITexture;
import net.minecraft.src.CraftGuide.ui.Rendering.TexturedRect;

public class GuiBorderedRect extends GuiElement
{
	private IRenderable[] parts = new IRenderable[9];

	public GuiBorderedRect(int x, int y, int width, int height,
			GuiTexture texture, int texX, int texY,
			int borderLength, int interiorLength)
	{
		this(x, y, width, height,
				texture, texX, texY,
				borderLength, interiorLength, 1);
	}
	
	public GuiBorderedRect(int x, int y, int width, int height,
			GuiTexture texture, int texX, int texY,
			int borderLength, int interiorLength,
			int spacing)
	{
		this(x, y, width, height,
				texture, texX, texY,
				borderLength, borderLength,
				interiorLength, interiorLength,
				spacing);
	}
	
	public GuiBorderedRect(int x, int y, int width, int height,
			GuiTexture texture, int texX, int texY,
			int borderWidth, int borderHeight,
			int interiorWidth, int interiorHeight,
			int spacing)
	{
		this(x, y, width, height,
				texture, texX, texY,
				borderWidth, borderWidth,
				borderHeight, borderHeight,
				interiorWidth, interiorHeight,
				spacing);
	}
	
	public GuiBorderedRect(int x, int y, int width, int height,
			GuiTexture texture, int texX, int texY,
			int borderLeftWidth, int borderRightWidth,
			int borderTopHeight, int borderBottomHeight,
			int interiorWidth, int interiorHeight,
			int spacing)
	{
		super(x, y, width, height);
		
		int centreWidth = width - borderLeftWidth - borderRightWidth;
		int centreHeight = height - borderTopHeight - borderBottomHeight;
		int texCentreX = texX + borderLeftWidth + spacing;
		int texRightX = texCentreX + interiorWidth + spacing;
		int texCentreY = texY + borderTopHeight + spacing;
		int texBottomY = texCentreY + interiorHeight + spacing;
		
		parts[0] = new TexturedRect(0, 0,
				borderLeftWidth, borderTopHeight,
				texture, texX, texY);
		
		parts[1] = new TexturedRect(borderLeftWidth, 0,
				centreWidth, borderTopHeight,
				new GuiSubTexture(texture,
						texCentreX, texY,
						interiorWidth, borderTopHeight),
				0, 0);
		
		parts[2] = new TexturedRect(borderLeftWidth + centreWidth, 0,
				borderRightWidth, borderTopHeight,
				texture, texRightX, texY);
		
		parts[3] = new TexturedRect(0, borderTopHeight,
				borderLeftWidth, centreHeight,
				new GuiSubTexture(texture,
						texX, texCentreY,
						borderLeftWidth, interiorHeight),
				0, 0);
		
		parts[4] = new TexturedRect(borderLeftWidth, borderTopHeight,
				centreWidth, centreHeight,
				new GuiSubTexture(texture,
						texCentreX, texCentreY,
						interiorWidth, interiorHeight),
				0, 0);
		
		parts[5] = new TexturedRect(borderLeftWidth + centreWidth, borderTopHeight,
				borderRightWidth, centreHeight,
				new GuiSubTexture(texture,
						texRightX, texCentreY,
						borderRightWidth, interiorHeight),
				0, 0);
		
		parts[6] = new TexturedRect(0, borderTopHeight + centreHeight,
				borderLeftWidth, borderBottomHeight,
				texture, texX, texBottomY);
		
		parts[7] = new TexturedRect(borderLeftWidth, borderTopHeight + centreHeight,
				centreWidth, borderBottomHeight,
				new GuiSubTexture(texture,
						texCentreX, texBottomY,
						interiorWidth, borderBottomHeight),
				0, 0);
		
		parts[8] = new TexturedRect(borderLeftWidth + centreWidth, borderTopHeight + centreHeight,
				borderRightWidth, borderBottomHeight,
				texture, texRightX, texBottomY);
	}

	@Override
	public void draw()
	{
		for(IRenderable rect: parts)
		{
			render(rect);
		}
		
		super.draw();
	}
}
