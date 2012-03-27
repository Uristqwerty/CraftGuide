package net.minecraft.src.CraftGuide.API;

/**
 * An extension of IRecipeTemplate, to allow arbitrary recipe sizes, while maintaining backwards compatibility with previous API versions.
 * 
 * @see IRecipeTemplate
 */

public interface IRecipeTemplateResizable extends IRecipeTemplate
{
	/**
	 * @param width
	 * @param height
	 * @return itself
	 */
	public IRecipeTemplateResizable setSize(int width, int height);
}
