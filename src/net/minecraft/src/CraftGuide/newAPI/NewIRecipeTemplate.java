package net.minecraft.src.CraftGuide.newAPI;

public interface NewIRecipeTemplate
{
	public NewIRecipeTemplate setSize(int width, int height);
	public NewIRecipeTemplate setBackground(String texture, int x, int y);
	public NewIRecipeTemplate setBackgroundSelected(String texture, int x, int y);
}
