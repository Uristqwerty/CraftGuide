package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;

public class ExtendedWorkbench extends CraftGuideAPIObject implements RecipeProvider
{
	private static ItemStack workbenchStack = new ItemStack(Blocks.crafting_table, 1, 1);

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			Class<?> craftingManagerClass = Class.forName("naruto1310.extendedWorkbench.crafting.ExtendedCraftingManager");
			Object craftingManager = craftingManagerClass.getMethod("getInstance").invoke(null);
			addRecipes(generator, (List<IRecipe>)craftingManagerClass.getMethod("getRecipeList").invoke(craftingManager));
		}
		catch(IllegalArgumentException | SecurityException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	private void addRecipes(RecipeGenerator generator, List<IRecipe> recipes) throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Class<?> extendedShapedRecipe = Class.forName("naruto1310.extendedWorkbench.crafting.ExtendedShapedRecipes");
		Field shapedWidth = extendedShapedRecipe.getDeclaredField("recipeWidth");
		Field shapedHeight = extendedShapedRecipe.getDeclaredField("recipeHeight");
		Field shapedItems = extendedShapedRecipe.getDeclaredField("recipeItems");
		shapedWidth.setAccessible(true);
		shapedHeight.setAccessible(true);
		shapedItems.setAccessible(true);

		Class<?> extendedShapelessRecipe = Class.forName("naruto1310.extendedWorkbench.crafting.ExtendedShapelessRecipes");
		Field shapelessItems = extendedShapelessRecipe.getDeclaredField("recipeItems");
		shapelessItems.setAccessible(true);

		ConstructedRecipeTemplate templateShaped = generator.buildTemplate(workbenchStack)
				.shapedItemGrid(3, 6).nextColumn(1)
				.outputItem().finishTemplate();

		ConstructedRecipeTemplate templateShapeless = generator.buildTemplate(workbenchStack)
				.shapelessItemGrid(3, 6).nextColumn(1)
				.outputItem().finishTemplate();

		for(IRecipe recipe: recipes)
		{
			if(extendedShapedRecipe.isInstance(recipe))
			{
				ItemStack[] items = (ItemStack[])shapedItems.get(recipe);
				int height = Math.min(6, shapedHeight.getInt(recipe));
				int width = Math.min(3, shapedWidth.getInt(recipe));

				templateShaped.buildRecipe()
					.shapedItemGrid(width, height, items)
					.item(recipe.getRecipeOutput())
					.addRecipe(generator);
			}
			else if(extendedShapelessRecipe.isInstance(recipe))
			{
				templateShapeless.buildRecipe()
					.shapelessItemGrid((List<ItemStack>)shapelessItems.get(recipe))
					.item(recipe.getRecipeOutput())
					.addRecipe(generator);
			}
		}
	}
}
