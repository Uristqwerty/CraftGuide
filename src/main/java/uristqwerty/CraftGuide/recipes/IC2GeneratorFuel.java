package uristqwerty.CraftGuide.recipes;

import ic2.api.info.Info;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2GeneratorFuel implements StackInfoSource
{
	private float mult = 1;

	public IC2GeneratorFuel()
	{
		mult = getGeneratorMult();
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

	private float getGeneratorMult()
	{
		return ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/generator");
	}
}
