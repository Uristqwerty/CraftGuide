package uristqwerty.CraftGuide;

import java.io.File;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public interface CraftGuideLoaderSide
{
	public boolean isModLoaded(String name);
	public File getConfigDir();
	public void addRecipe(ItemStack itemStack, Object[] objects);
	public void addName(Item item, String name);
	public void logConsole(String text);
	public void logConsole(String text, Throwable e);
	public void initClientNetworkChannels();
}
