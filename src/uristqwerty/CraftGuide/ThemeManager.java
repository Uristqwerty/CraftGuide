package uristqwerty.CraftGuide;

import java.io.File;
import java.util.Collection;

import net.minecraft.client.Minecraft;

@SuppressWarnings("unused")
public class ThemeManager
{
	private Theme currentTheme;
	private Collection<ThemeFile> themes;
	
	public void reload()
	{
		currentTheme = new Theme();
		
		File themeDir = CraftGuide.themeDirectory();
		
		if(!themeDir.isDirectory())
		{
			return;
		}
	}
}
