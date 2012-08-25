package uristqwerty.gui.minecraft;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.ModLoader;

import uristqwerty.gui.Renderer;
import uristqwerty.gui.texture.Texture;

public class Image implements Texture
{
	private int texID;
	private static Map<String, Image> cache = new HashMap<String, Image>();
	
	public static Image getImage(String filename)
	{
		Image image = cache.get(filename);
		
		if(image == null)
		{
			image = new Image(ModLoader.getMinecraftInstance().renderEngine.getTexture(filename));
			cache.put(filename, image);
		}
		
		return image;
	}
	
	private Image(int textureID)
	{
		texID = textureID;
	}
	
	@Override
	public void renderRect(Renderer renderer, int x, int y, int width, int height, int textureX, int textureY)
	{
		double u = (textureX % 256) / 256.0;
		double v = (textureY % 256) / 256.0;
		double u2 = ((textureX % 256) + width) / 256.0;
		double v2 = ((textureY % 256) + height) / 256.0;
		
		renderer.setTextureID(texID);
		renderer.drawTexturedRect(x, y, width, height, u, v, u2, v2);
	}
}
