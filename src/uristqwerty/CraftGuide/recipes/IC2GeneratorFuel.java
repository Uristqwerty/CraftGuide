package uristqwerty.CraftGuide.recipes;

import ic2.core.block.machine.tileentity.TileEntityIronFurnace;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2GeneratorFuel implements StackInfoSource
{
	@Override
	public String getInfo(ItemStack itemStack)
	{
		try
		{
			int fuel;
			if(itemStack.hasTagCompound())
			{
				fuel = TileEntityIronFurnace.getFuelValueFor(itemStack);
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
		long lookup = (stack.itemID << 32) | (stack.getHasSubtypes()? stack.getItemDamage() : 0);

		Integer value = burnCache.get(lookup);

		if(value == null)
		{
			value = TileEntityIronFurnace.getFuelValueFor(stack);
			burnCache.put(lookup, value);
		}

		return value;
	}
}
