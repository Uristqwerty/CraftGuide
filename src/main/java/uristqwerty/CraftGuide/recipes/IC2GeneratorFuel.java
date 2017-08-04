package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.InvocationTargetException;

import ic2.api.info.Info;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2GeneratorFuel implements StackInfoSource
{
	private float mult = 1;

	public IC2GeneratorFuel()
	{
		try
		{
			mult = getGeneratorMult();
		}
		catch(IllegalAccessException | ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	@Override
	public String getInfo(ItemStack itemStack)
	{
		int fuel =  Info.itemInfo.getFuelValue(itemStack, false);

		if(fuel > 0)
		{
			return "\u00a77" + ((fuel / 4) * mult) + " EU in an IndustrialCraft generator";
		}

		return null;
	}

	private float getGeneratorMult() throws IllegalAccessException, ClassNotFoundException
	{
		CraftGuide.unimplemented(); // TODO: fix reflection, drop backcompat
		try
		{
			return (Integer)Class.forName("ic2.core.IC2").getField("energyGeneratorBase").get(null);
		}
		catch(NoSuchFieldException e)
		{
			try
			{
				Class<?> configClass = Class.forName("ic2.core.util.Config");

				try
				{
					Class<?> configUtilClass = Class.forName("ic2.core.util.ConfigUtil");
					Object config = Class.forName("ic2.core.init.MainConfig").getMethod("get").invoke(null);
					return (Float)configUtilClass.getMethod("getFloat", configClass, String.class).invoke(null, config, "balance/energy/generator/generator");
				}
				catch(ClassNotFoundException _)
				{
					Object config = Class.forName("ic2.core.IC2").getField("config").get(null);
					return 10*(Float)configClass.getMethod("getFloat", String.class).invoke(config, "balance/energy/generator/generator");
				}
			}
			catch(NoSuchFieldException e1)
			{
				try
				{
					Object config = Class.forName("ic2.core.init.MainConfig").getMethod("get").invoke(null);
					return (Float)Class.forName("ic2.core.util.Config").getMethod("getFloat", String.class).invoke(config, "balance/energy/generator/generator");
				}
				catch(IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException e2)
				{
					CraftGuideLog.log(e2, "", true);
				}
			}
			catch(InvocationTargetException | NoSuchMethodException | ClassNotFoundException e1)
			{
				CraftGuideLog.log(e1, "", true);
			}
		}

		return 0;
	}
}
