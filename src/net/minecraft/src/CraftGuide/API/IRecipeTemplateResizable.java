package net.minecraft.src.CraftGuide.API;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 *
 * An extension of IRecipeTemplate, to allow arbitrary recipe sizes, while maintaining backwards compatibility with previous API versions.
 *
 * @see IRecipeTemplate
 */

@Deprecated
public interface IRecipeTemplateResizable extends IRecipeTemplate
{
	/**
	 * @param width
	 * @param height
	 * @return itself
	 */
	public IRecipeTemplateResizable setSize(int width, int height);
}
