package uristqwerty.gui_craftguide.texture;

import uristqwerty.gui_craftguide.editor.TextureMeta.Unit;
import uristqwerty.gui_craftguide.editor.TextureMeta.WithUnits;

public class AnimationFrame
{
	public Texture source;

	@WithUnits({
		@Unit(multiplier = 1, names = {"second", "seconds", "s"}),
		@Unit(multiplier = 1/1000.0, names = {"millisecond", "milliseconds", "ms"}),
		@Unit(multiplier = 1/20.0, names = {"tick", "ticks", "t"}),
	})
	public double duration;
}
