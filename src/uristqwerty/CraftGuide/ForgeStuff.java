package uristqwerty.CraftGuide;

import java.util.List;

import net.minecraft.src.IRecipe;
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
	public boolean isShapelessRecipe(IRecipe recipe)
	{
		return recipe instanceof ShapedOreRecipe;
	}

	@Override
	public Object[] getCraftingRecipe(RecipeGeneratorImplementation gen, IRecipe recipe, boolean allowSmallGrid)
	{
		try
		{
			if(recipe instanceof ShapedOreRecipe)
			{
				int width = (Integer)CommonUtilities.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "width");
				int height = (Integer)CommonUtilities.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "height");
				Object[] items = (Object[])CommonUtilities.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "input");

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
				List items = (List)CommonUtilities.getPrivateValue(ShapelessOreRecipe.class, (ShapelessOreRecipe)recipe, "input");
				return gen.getCraftingShapelessRecipe(items, ((ShapelessOreRecipe)recipe).getRecipeOutput());
			}
		}
		catch(SecurityException e)
		{
			CraftGuideLog.log(e);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e);
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e);
		}

		return null;
	}
}
