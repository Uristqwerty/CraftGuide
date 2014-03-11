package uristqwerty.CraftGuide.recipes;

import ic2.api.info.Info;

import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2GeneratorFuel implements StackInfoSource
{
	@Override
	public String getInfo(ItemStack itemStack)
	{
		try
		{
			int fuel =  Info.itemFuel.getFuelValue(itemStack, false);

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
}
