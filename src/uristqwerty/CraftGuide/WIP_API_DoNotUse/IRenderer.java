package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import net.minecraft.src.ItemStack;

public interface IRenderer
{
	public void renderItemStack(int x, int y, ItemStack stack);
	public void renderRect(int x, int y, int width, int height, NamedTexture texture);
	public void renderRect(int x, int y, int width, int height, int red, int green, int blue, int alpha);
}
