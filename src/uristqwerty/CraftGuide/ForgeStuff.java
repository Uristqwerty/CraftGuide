package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.IRecipe;
import net.minecraft.src.ModLoader;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import uristqwerty.CraftGuide.RecipeGeneratorImplementation.RecipeGeneratorForgeExtension;

public class ForgeStuff implements RecipeGeneratorForgeExtension
{
	@Override
	public boolean matchesType(IRecipe recipe)
	{
		return recipe instanceof ShapedOreRecipe || recipe instanceof ShapelessOreRecipe;
	}

	@Override
	public Object[] getCraftingRecipe(RecipeGeneratorImplementation gen, IRecipe recipe, boolean allowSmallGrid)
	{
		if(recipe instanceof ShapedOreRecipe)
		{
			int width = (Integer)ModLoader.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "width");
			int height = (Integer)ModLoader.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "height");
			Object[] items = (Object[])ModLoader.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "input");

			if(allowSmallGrid && width < 3 && height < 3)
			{
				return gen.getSmallShapedRecipe(width, height, items, ((ShapedOreRecipe)recipe).getRecipeOutput());
			}
			else
			{
				return gen.getCraftingShapedRecipe(width, height, items, ((ShapedOreRecipe)recipe).getRecipeOutput());
			}
		}
		else if(recipe instanceof ShapelessOreRecipe)
		{
			List items = (List)ModLoader.getPrivateValue(ShapelessOreRecipe.class, (ShapelessOreRecipe)recipe, "input");
			return gen.getCraftingShapelessRecipe(items, ((ShapelessOreRecipe)recipe).getRecipeOutput());
		}
		else
		{
			return null;
		}
	}
}
