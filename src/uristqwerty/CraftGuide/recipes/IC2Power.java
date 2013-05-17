package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.StackInfoSource;

public class IC2Power implements StackInfoSource
{
	private static boolean init = false;
	private static int suBatteryID;
	private static Class electricItem;
	private static Method getMaxCharge;

	@Override
	public String getInfo(ItemStack itemStack)
	{
		if(!init)
		{
			init();
			init = true;
		}

		if(itemStack.itemID == suBatteryID)
		{
			return "\u00a77Powers IC2 machines for 1000 EU";
		}
		else if(itemStack.itemID == Item.redstone.itemID)
		{
			return "\u00a77Powers IC2 machines for 500 EU";
		}
		else if(electricItem.isInstance(itemStack.getItem()))
		{
			try
			{
				return "\u00a77Can store " + getMaxCharge.invoke(itemStack.getItem(), itemStack) + " EU";
			}
			catch(IllegalArgumentException e)
			{
				CraftGuideLog.log(e);
			}
			catch(IllegalAccessException e)
			{
				CraftGuideLog.log(e);
			}
			catch(InvocationTargetException e)
			{
				CraftGuideLog.log(e);
			}
		}

		return null;
	}

	private void init()
	{
		try
		{
			suBatteryID = ((ItemStack)Class.forName("ic2.core.Ic2Items").getField("suBattery").get(null)).itemID;

			try
			{
				electricItem = Class.forName("ic2.api.item.IElectricItem");
			}
			catch(ClassNotFoundException e)
			{
				electricItem = Class.forName("ic2.api.IElectricItem");
			}

			getMaxCharge = electricItem.getMethod("getMaxCharge", ItemStack.class);
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e);
		}
		catch(SecurityException e)
		{
			CraftGuideLog.log(e);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e);
		}
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e);
		}
	}
}
