package net.minecraft.src.CraftGuide.newAPI;

public interface IRecipeTemplate
{
	public IRecipeTemplate setSize(int width, int height);
	public IRecipeTemplate setBackground(String texture, int x, int y);
	public IRecipeTemplate setBackgroundSelected(String texture, int x, int y);
}
