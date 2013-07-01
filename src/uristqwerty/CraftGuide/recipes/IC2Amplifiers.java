package uristqwerty.CraftGuide.recipes;

import ic2.api.recipe.Recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2Amplifiers implements StackInfoSource
{
	private static Map<Long, Integer> amplifierCache = new HashMap<Long, Integer>();

	@Override
	public String getInfo(ItemStack itemStack)
	{
		int value = getCachedAmplifierValue(itemStack);

		if(value != 0)
		{
			return "\u00a77Massfab amplifier value: " + value;
		}
		else
		{
			return null;
		}
	}

	private static int getCachedAmplifierValue(ItemStack stack)
	{
		long lookup = (stack.itemID << 32) | (stack.getHasSubtypes()? CommonUtilities.getItemDamage(stack) : 0);

		Integer value = amplifierCache.get(lookup);

		if(value == null)
		{
			value = Recipes.matterAmplifier.getOutputFor(stack, false);

			if(value == null)
			{
				value = 0;
			}

			amplifierCache.put(lookup, value);
		}

		return value;
	}
}
