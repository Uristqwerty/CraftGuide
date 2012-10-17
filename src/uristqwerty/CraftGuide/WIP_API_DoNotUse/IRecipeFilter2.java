package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;
import java.util.Map;

import net.minecraft.src.ItemStack;

/**
 * A more advanced version of {@link IRecipeFilter} which is passed every
 * type of recipe at once. This allows for more advanced comparisons, such
 * as taking into consideration the type of recipe (smelting, brewing, something
 * added by a mod), or comparing if a recipe exists in multiple crafting types.
 * <br><br>
 * It should also be possible to move recipes between crafting types, or re-order
 * the recipes within a single crafting type.
 * 
 * @see IRecipeFilter
 */
public interface IRecipeFilter2
{
	Map<ItemStack, List<ICraftGuideRecipe>> removeRecipes(Map<ItemStack, List<ICraftGuideRecipe>> allRecipes);
}
