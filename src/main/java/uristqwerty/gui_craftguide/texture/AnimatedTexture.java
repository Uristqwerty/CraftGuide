package uristqwerty.gui_craftguide.texture;

import uristqwerty.gui_craftguide.editor.TextureMeta;
import uristqwerty.gui_craftguide.editor.TextureMeta.TextureInit;
import uristqwerty.gui_craftguide.editor.TextureMeta.TextureParameter;
import uristqwerty.gui_craftguide.rendering.RendererBase;

@TextureMeta(name = "animatedtexture")
public class AnimatedTexture implements Texture
{
	@TextureParameter
	public Texture[] frames;

	@TextureParameter
	public float frameDuration;

	@TextureInit
	public AnimatedTexture(Texture[] frames)
	{
		this.frames = frames;
	}

	@Override
	public void renderRect(RendererBase renderer, int x, int y, int width, int height, int u, int v)
	{

	}
}
