package uristqwerty.gui.components;

import java.util.EnumMap;
import java.util.Map;

import uristqwerty.CraftGuide.client.ui.GuiRenderer;
import uristqwerty.gui.Rect;
import uristqwerty.gui.rendering.Renderable;
import uristqwerty.gui.texture.Texture;

public class Window extends GuiElement
{
	public enum Layer
	{
		MAIN,
		POPUP,
		TOOLTIP,
	}

	private GuiRenderer renderer;
	private boolean mousePressed;
	private Map<Layer, GuiElement> layers = new EnumMap<Layer, GuiElement>(Layer.class);

	public Window(int x, int y, int width, int height, GuiRenderer renderer)
	{
		super(x, y, width, height);

		this.renderer = renderer;
	}

	public void centerOn(int centerX, int centerY)
	{
		setPosition(centerX - (bounds.width() / 2), centerY - (bounds.height() / 2));
	}

	@Override
	public void render(Renderable renderable, int xOffset, int yOffset)
	{
		renderer.render(renderable, xOffset + bounds.x(), yOffset + bounds.y());
	}

	@Override
	public void render(Texture texture, int x, int y, int width, int height)
	{
		texture.renderRect(renderer, x, y, width, height, 0, 0);
	}

	public void updateMouse(int x, int y)
	{
		mouseMoved(x, y);
	}

	public void updateMouseState(int x, int y, boolean buttonState)
	{
		if(mousePressed != buttonState)
		{
			mousePressed = buttonState;

			if(buttonState)
			{
				mousePressed(x, y);
				GuiElement element = getElementAtPoint(x, y);

				if(element != null)
				{
					element.elementClicked(x - element.absoluteX(), y - element.absoluteY());
				}
			}
			else
			{
				mouseReleased(x, y);
			}
		}
	}

	@Override
	public void mousePressed(int x, int y)
	{
		getLayer(Layer.MAIN).mousePressed(x - bounds.x(), y - bounds.y());
		getLayer(Layer.POPUP).mousePressed(x - bounds.x(), y - bounds.y());
	}

	@Override
	public void draw()
	{
		drawBackground();
		getLayer(Layer.MAIN).draw();
		getLayer(Layer.POPUP).draw();
		getLayer(Layer.TOOLTIP).draw();
	}

	@Override
	public GuiElement getElementAtPoint(int x, int y)
	{
		GuiElement element = null;

		element = getLayer(Layer.POPUP).getElementAtPoint(x - bounds.x(), y - bounds.y());

		if(element == null)
		{
			element = getLayer(Layer.MAIN).getElementAtPoint(x - bounds.x(), y - bounds.y());
		}

		return element;
	}

	public void setMaxSize(int width, int height)
	{
		if(bounds.width() > width || bounds.height() > height)
		{
			setSize(Math.min(bounds.width(), width), Math.min(bounds.height(), height));
		}
	}

	@Override
	public GuiElement getLayer(Layer layer)
	{
		GuiElement element = layers.get(layer);

		if(element == null)
		{
			element = new GuiElement(new Rect(0, 0, bounds.width(), bounds.height()))
					.anchor(AnchorPoint.TOP_LEFT, AnchorPoint.BOTTOM_RIGHT)
					.setClickable(false);
			layers.put(layer, element);
			super.addElement(element);
		}

		return element;
	}

	@Override
	public GuiElement addElement(GuiElement element)
	{
		addElement(element, Layer.MAIN);
		return this;
	}

	private void addElement(GuiElement element, Layer layer)
	{
		getLayer(layer).addElement(element);
	}
}
