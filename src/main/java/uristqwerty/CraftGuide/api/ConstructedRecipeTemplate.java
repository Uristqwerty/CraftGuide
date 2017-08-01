package uristqwerty.CraftGuide.api;

import java.util.Collection;
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
		RecipeBuilder icon(String sourceTexture, String iconName);
		/** Texture coordinates in IIcon units: 0.0 to 16.0
		 * iconTint may be null for default value. Would by Optional<int> if
		 * a) last 1.7.10 release targetted Java8, and b) Java8 didn't require Optional<Integer> anyway */
		RecipeBuilder icon(String sourceTexture, String iconName, Integer iconTint_argb, float u, float v, float u2, float v2);
		RecipeBuilder iconWithText(String sourceTexture, String iconName, String text);
		/** Texture coordinates in IIcon units: 0.0 to 16.0
		 * iconTint may be null for default value. Would by Optional<int> if
		 * a) last 1.7.10 release targetted Java8, and b) Java8 didn't require Optional<Integer> anyway */
		RecipeBuilder iconWithText(String sourceTexture, String iconName, Integer iconTint_argb, float u, float v, float u2, float v2, String text);
		<T> RecipeBuilder subUnit(T[] items, SubunitBuilder<T> builder);
		<T> RecipeBuilder subUnit(Collection<T> items, SubunitBuilder<T> builder);

		void addRecipe(RecipeGenerator generator);
	}

	public static interface SubunitBuilder<T>
	{
		public void build(T item, RecipeBuilder builder);
	}
}
