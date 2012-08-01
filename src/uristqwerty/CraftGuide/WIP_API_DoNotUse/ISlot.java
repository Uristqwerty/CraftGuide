package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

public interface ISlot
{
	public void init(IRenderer renderer);
	public void draw(IRenderer renderer, int x, int y, Object data, boolean isMouseOver);
	public List<String> getTooltip(Object data);
}
