package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2GeneratorFuel implements StackInfoSource
{
	@Override
	public String getInfo(ItemStack itemStack)
	{
		try
		{
			int fuel = (Integer)Class.forName("ic2.common.TileEntityIronFurnace").getMethod("getFuelValueFor", ItemStack.class).invoke(null, itemStack);

			if(fuel > 0)
			{
				int generation = (Integer)Class.forName("ic2.common.IC2").getField("energyGeneratorBase").get(null);

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
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchMethodException e)
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
