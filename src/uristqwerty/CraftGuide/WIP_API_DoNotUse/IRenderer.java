package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.src.ItemStack;

public interface IRenderer
{
	public ITexture getTexture(String identifier);

	public void renderItemStack(int x, int y, ItemStack stack);
	public void renderRect(int x, int y, int width, int height, ITexture texture);
	public void renderRect(int x, int y, int width, int height, byte red, byte green, byte blue, byte alpha);
}
