package uristqwerty.CraftGuide.client.ui;

import uristqwerty.gui.components.GuiElement;
import uristqwerty.gui.rendering.Renderable;
import uristqwerty.gui.rendering.RendererBase;
import uristqwerty.gui.rendering.TexturedRect;
import uristqwerty.gui.texture.SubTexture;
import uristqwerty.gui.texture.Texture;

public class GuiBorderedRect extends GuiElement implements Renderable
{
	private TexturedRect[] parts = new TexturedRect[9];

	public GuiBorderedRect(int x, int y, int width, int height,
			Texture texture, int texX, int texY,
			int borderLength, int interiorLength)
	{
		this(x, y, width, height,
				texture, texX, texY,
				borderLength, interiorLength, 1);
	}

	public GuiBorderedRect(int x, int y, int width, int height,
			Texture texture, int texX, int texY,
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
			Texture texture, int texX, int texY,
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
			Texture texture, int texX, int texY,
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
				new SubTexture(texture,
						texCentreX, texY,
						interiorWidth, borderTopHeight),
				0, 0);

		parts[2] = new TexturedRect(borderLeftWidth + centreWidth, 0,
				borderRightWidth, borderTopHeight,
				texture, texRightX, texY);

		parts[3] = new TexturedRect(0, borderTopHeight,
				borderLeftWidth, centreHeight,
				new SubTexture(texture,
						texX, texCentreY,
						borderLeftWidth, interiorHeight),
				0, 0);

		parts[4] = new TexturedRect(borderLeftWidth, borderTopHeight,
				centreWidth, centreHeight,
				new SubTexture(texture,
						texCentreX, texCentreY,
						interiorWidth, interiorHeight),
				0, 0);

		parts[5] = new TexturedRect(borderLeftWidth + centreWidth, borderTopHeight,
				borderRightWidth, centreHeight,
				new SubTexture(texture,
						texRightX, texCentreY,
						borderRightWidth, interiorHeight),
				0, 0);

		parts[6] = new TexturedRect(0, borderTopHeight + centreHeight,
				borderLeftWidth, borderBottomHeight,
				texture, texX, texBottomY);

		parts[7] = new TexturedRect(borderLeftWidth, borderTopHeight + centreHeight,
				centreWidth, borderBottomHeight,
				new SubTexture(texture,
						texCentreX, texBottomY,
						interiorWidth, borderBottomHeight),
				0, 0);

		parts[8] = new TexturedRect(borderLeftWidth + centreWidth, borderTopHeight + centreHeight,
				borderRightWidth, borderBottomHeight,
				texture, texRightX, texBottomY);
	}

	public GuiBorderedRect(int x, int y, int width, int height, BorderedRectTemplate template)
	{
		this(x, y, width, height,
			template.texture, template.texX, template.texY,
			template.borderLeftWidth, template.borderRightWidth,
			template.borderTopHeight, template.borderBottomHeight,
			template.interiorWidth, template.interiorHeight,
			template.spacing);
	}

	@Override
	public void draw()
	{
		for(Renderable rect: parts)
		{
			render(rect);
		}

		super.draw();
	}

	@Override
	public void render(RendererBase renderer, int x, int y)
	{
		for(Renderable rect: parts)
		{
			rect.render(renderer, x + bounds.x(), y + bounds.y());
		}
	}

	@Override
	public void onResize(int oldWidth, int oldHeight)
	{
		int widthChange = bounds.width() - oldWidth;
		int heightChange = bounds.height() - oldHeight;

		parts[1].resizeBy(widthChange, 0);
		parts[3].resizeBy(0, heightChange);
		parts[4].resizeBy(widthChange, heightChange);
		parts[5].resizeBy(0, heightChange);
		parts[7].resizeBy(widthChange, 0);

		parts[2].moveBy(widthChange, 0);
		parts[5].moveBy(widthChange, 0);
		parts[6].moveBy(0, heightChange);
		parts[7].moveBy(0, heightChange);
		parts[8].moveBy(widthChange, heightChange);
	}
}
