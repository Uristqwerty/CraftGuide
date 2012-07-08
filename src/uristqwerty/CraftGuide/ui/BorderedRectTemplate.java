package uristqwerty.CraftGuide.ui;

import uristqwerty.CraftGuide.ui.Rendering.GuiTexture;

public class BorderedRectTemplate
{
	public GuiTexture texture;
	public int texX, texY;
	public int borderLeftWidth, borderRightWidth;
	public int borderTopHeight, borderBottomHeight;
	public int interiorWidth, interiorHeight;
	public int spacing;
	
	public BorderedRectTemplate(
			GuiTexture texture, int texX, int texY,
			int borderSize, int interiorSize)
	{
		this(texture, texX, texY, borderSize, interiorSize, 1);
	}

	public BorderedRectTemplate(GuiTexture texture, int texX, int texY,
			int borderSize, int interiorSize, int spacing)
	{
		this(texture, texX, texY,
			borderSize, borderSize, interiorSize, interiorSize,
			spacing);
	}

	public BorderedRectTemplate(GuiTexture texture, int texX, int texY,
			int borderWidth, int borderHeight,
			int interiorWidth, int interiorHeight,
			int spacing)
	{
		this(texture, texX, texY,
				borderWidth, borderWidth,
				borderHeight, borderHeight,
				interiorWidth, interiorHeight,
				spacing);
	}

	public BorderedRectTemplate(GuiTexture texture, int texX, int texY,
			int borderLeftWidth, int borderRightWidth, int borderTopHeight,
			int borderBottomHeight, int interiorWidth, int interiorHeight,
			int spacing)
	{
		this.texture = texture;
		this.texX = texX;
		this.texY = texY;
		this.borderLeftWidth = borderLeftWidth;
		this.borderRightWidth = borderRightWidth;
		this.borderTopHeight = borderTopHeight;
		this.borderBottomHeight = borderBottomHeight;
		this.interiorWidth = interiorWidth;
		this.interiorHeight = interiorHeight;
		this.spacing = spacing;
	}
}
