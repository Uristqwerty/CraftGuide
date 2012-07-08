package uristqwerty.CraftGuide.ui.Rendering;

import java.util.HashMap;
import java.util.Map;

import uristqwerty.CraftGuide.ui.GuiRenderer;

import net.minecraft.src.RenderEngine;

public class GuiTexture implements ITexture
{
	private int textureID = -1;
	private String path;
	private static Map<String, GuiTexture> textureList = new HashMap<String, GuiTexture>();
	
	private GuiTexture(String name)
	{
		path = name;
	}

	public String getPath()
	{
		return path;
	}
	
	public void updateTextureID(int id)
	{
		textureID = id;
	}

	public int getTextureID()
	{
		return textureID;
	}

	public static void refreshTextures(RenderEngine renderEngine)
	{
		for(GuiTexture texture: textureList.values())
		{
			texture.updateTextureID(renderEngine.getTexture(texture.getPath()));
		}
	}

	public static GuiTexture getInstance(String name)
	{
		if(!textureList.containsKey(name))
		{
			textureList.put(name, new GuiTexture(name));
		}
		return textureList.get(name);
	}

	@Override
	public void setActive(GuiRenderer renderer)
	{
		renderer.setTextureID(textureID);
	}
}
