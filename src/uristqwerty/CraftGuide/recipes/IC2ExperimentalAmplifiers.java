package uristqwerty.CraftGuide.recipes;

import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;

import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2ExperimentalAmplifiers implements StackInfoSource
{
	@Override
	public String getInfo(ItemStack itemStack)
	{
		int value = getValue(Recipes.matterAmplifier.getOutputFor(itemStack, false));

		if(value != 0)
		{
			return "\u00a77Massfab amplifier value: " + value;
		}
		else
		{
			return null;
		}
	}

	private static Integer getValue(RecipeOutput output)
	{
		if(output != null && output.metadata != null)
		{
			return output.metadata.getInteger("amplification");
		}

		return 0;
	}
}
