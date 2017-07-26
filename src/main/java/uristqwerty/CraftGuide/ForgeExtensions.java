package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;

public abstract class ForgeExtensions
{
	private static ForgeExtensions forgeExt = new ForgeExtensionsNullImpl();

	public static String getOreDictionaryName(List<?> list)
	{
		return forgeExt.getOreDictionaryNameImpl(list);
	}

	public static boolean matchesType(IRecipe recipe)
	{
		return forgeExt.matchesTypeImpl(recipe);
	}

	public static boolean isShapelessRecipe(IRecipe recipe)
	{
		return forgeExt.isShapelessRecipeImpl(recipe);
	}

	public static Object[] getCraftingRecipe(RecipeGeneratorImplementation recipeGeneratorImplementation, IRecipe recipe, boolean allowSmallGrid)
	{
		return forgeExt.getCraftingRecipeImpl(recipeGeneratorImplementation, recipe, allowSmallGrid);
	}

	public static List<String> emptyOreDictEntryText(List<?> oreDictionaryList)
	{
		return forgeExt.emptyOreDictEntryTextImpl(oreDictionaryList);
	}


	public static void setImplementation(ForgeExtensions imlpementation)
	{
		forgeExt = imlpementation;
	}

	protected abstract boolean matchesTypeImpl(IRecipe recipe);
	protected abstract boolean isShapelessRecipeImpl(IRecipe recipe);
	protected abstract Object[] getCraftingRecipeImpl(RecipeGeneratorImplementation recipeGeneratorImplementation, IRecipe recipe, boolean allowSmallGrid);
	protected abstract List<String> emptyOreDictEntryTextImpl(List<?> oreDictionaryList);
	protected abstract String getOreDictionaryNameImpl(List<?> list);

	private static class ForgeExtensionsNullImpl extends ForgeExtensions
	{
		@Override
		protected boolean matchesTypeImpl(IRecipe recipe)
		{
			return false;
		}

		@Override
		protected boolean isShapelessRecipeImpl(IRecipe recipe)
		{
			return false;
		}

		@Override
		protected Object[] getCraftingRecipeImpl(
				RecipeGeneratorImplementation recipeGeneratorImplementation,
				IRecipe recipe, boolean allowSmallGrid)
		{
			return null;
		}

		@Override
		protected List<String> emptyOreDictEntryTextImpl(List<?> oreDictionaryList)
		{
			return null;
		}

		@Override
		protected String getOreDictionaryNameImpl(List<?> list)
		{
			return null;
		}

	}
}