package uristqwerty.CraftGuide.api;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;

public interface ConstructedRecipeTemplate
{
	public RecipeBuilder buildRecipe();

	public static interface RecipeBuilder
	{
		RecipeBuilder shapelessItemGrid(Object[] items);
		RecipeBuilder shapelessItemGrid(List<?> items);
		RecipeBuilder shapedItemGrid(int width, int height, Object[] items);
		RecipeBuilder shapedItemGrid(int width, int height, List<?> items);
		RecipeBuilder item(Object item);
		RecipeBuilder chanceItem(Object item, double probability);
		RecipeBuilder liquid(FluidStack liquid);
		RecipeBuilder liquid(PseudoFluidStack liquid);
		RecipeBuilder text(String text);
		/* iconName rather than a direct IIcon reference because I assume it would be more friendly for
		 * a hypothetical server-side module that can serialize recipe data to send to clients */
		RecipeBuilder icon(String iconName);
		RecipeBuilder icon(String iconName, float u, float v, float u2, float v2);
		RecipeBuilder iconWithData(String iconName, int data);
		RecipeBuilder iconWithData(String iconName, float u, float v, float u2, float v2, int data);

		void addRecipe(RecipeGenerator generator);
	}
}
