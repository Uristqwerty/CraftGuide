package uristqwerty.gui.texture;

import uristqwerty.gui.Renderer;

public class SubTexture implements Texture
{
	private final Texture source;
	private final int u, v, width, height;
	
	public SubTexture(Texture source, int u, int v, int width, int height)
	{
		this.source = source;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void renderRect(Renderer renderer, int x, int y, int width, int height, int u, int v)
	{
		if(this.width < 1 || this.height < 1)
		{
			return;
		}
		
		u = ((u % this.width) + this.width) % this.width; //Properly handle negatives

		if(u + width <= this.width)
		{
			renderRectColumn(renderer, x, y, width, height, this.u + u, v);
		}
		else
		{
			if(u != 0)
			{
				renderRectColumn(renderer, x, y, this.width - (u % this.width), height, this.u + (u % this.width), v);
			}

			int segment_start;
			for(segment_start = u; segment_start + this.width < width; segment_start += this.width)
			{
				renderRectColumn(renderer, x + segment_start, y, this.width, height, this.u, v);
			}

			renderRectColumn(renderer, x + segment_start, y, width - segment_start, height, this.u, v);
		}
	}
	
	private void renderRectColumn(Renderer renderer, int x, int y, int width, int height, int u, int v)
	{
		int v1 = (((v % this.height) + this.height) % this.height) + this.v;
		int v2 = v1 + height;
		
		if(v2 < this.v + this.height)
		{
			source.renderRect(renderer, x, y, width, height, u, v1);
		}
		else
		{
			if(v != 0)
			{
				source.renderRect(renderer, x, y, width, this.height - (v % this.height), u, this.v + (v % this.height));
			}

			int segment_start;
			for(segment_start = v; segment_start + this.height < height; segment_start += this.height)
			{
				source.renderRect(renderer, x, y + segment_start, width, this.height, u, this.v);
			}

			source.renderRect(renderer, x, y + segment_start, width, height - segment_start, u, this.v);
		}
	}
}
