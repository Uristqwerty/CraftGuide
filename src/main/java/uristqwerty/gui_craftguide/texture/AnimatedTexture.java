package uristqwerty.gui_craftguide.texture;

import uristqwerty.gui_craftguide.editor.TextureMeta;
import uristqwerty.gui_craftguide.editor.TextureMeta.TextureInit;
import uristqwerty.gui_craftguide.editor.TextureMeta.TextureParameter;
import uristqwerty.gui_craftguide.rendering.RendererBase;

@TextureMeta(name = "animation")
public class AnimatedTexture implements Texture
{
	private static final double UNINITIALIZED_DURATION = -7.185673e5;

	@TextureParameter
	public AnimationFrame frames[];

	private double totalDuration = UNINITIALIZED_DURATION;

	public AnimatedTexture()
	{
	}

	@TextureInit
	public AnimatedTexture(AnimationFrame frames[])
	{
		this.frames = frames;
		initDuration();
	}

	private void initDuration()
	{
		totalDuration = 0;
		for(AnimationFrame frame: this.frames)
		{
			totalDuration += frame.duration;
		}
	}

	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{
		if(totalDuration == UNINITIALIZED_DURATION)
		{
			initDuration();
		}
		double t = renderer.getClock() % totalDuration;
		for(AnimationFrame frame: frames)
		{
			if(t < frame.duration)
			{
				frame.source.renderRect(renderer, x, y, width, height, u, v);
				break;
			}
			else
			{
				t -= frame.duration;
			}
		}
	}
}
