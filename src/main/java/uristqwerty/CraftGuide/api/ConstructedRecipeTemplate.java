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

		void addRecipe(RecipeGenerator generator);
	}
}
