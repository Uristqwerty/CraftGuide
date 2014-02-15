package uristqwerty.CraftGuide.recipes;

import ic2.api.info.Info;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2GeneratorFuel implements StackInfoSource
{
	@Override
	public String getInfo(ItemStack itemStack)
	{
		try
		{
			int fuel;
			if(Item.itemsList[itemStack.itemID] == null)
			{
				fuel = 0;
			}
			else if(itemStack.hasTagCompound())
			{
				fuel = Info.itemFuel.getFuelValue(itemStack, false);
			}
			else
			{
				fuel = getCachedBurnTime(itemStack);
			}

			if(fuel > 0)
			{
				int generation = (Integer)Class.forName("ic2.core.IC2").getField("energyGeneratorBase").get(null);

				return "\u00a77" + ((fuel / 4) * generation) + " EU in an IndustrialCraft generator";
			}
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private static Map<Long, Integer> burnCache = new HashMap();

	private static int getCachedBurnTime(ItemStack stack)
	{
		long lookup = (stack.itemID << 32) | (stack.getHasSubtypes()? CommonUtilities.getItemDamage(stack) & 0xffffffff : 0);

		Integer value = burnCache.get(lookup);

		if(value == null)
		{
			value = Info.itemFuel.getFuelValue(stack, false);
			burnCache.put(lookup, value);
		}

		return value;
	}
}
