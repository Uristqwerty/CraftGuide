package uristqwerty.CraftGuide;

import java.io.File;
import java.util.Collection;

import net.minecraft.client.Minecraft;

public class ThemeManager
{
	ThemeFile currentTheme;
	ThemeFile defaultTheme;
	Collection<ThemeFile> themes;
	
	public void reload()
	{
		currentTheme = new ThemeFile();
		defaultTheme = new ThemeFile(mod_CraftGuide.class.getResourceAsStream("default_theme.txt"));
		
		File themeDir = new File(Minecraft.getMinecraftDir(), "CraftGuide themes");
	}
}
