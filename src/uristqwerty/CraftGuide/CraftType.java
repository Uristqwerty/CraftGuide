package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.ItemStack;

public class CraftType implements Comparable<CraftType>
{
	private static Map<Integer, Map<Integer, CraftType>> cache = new HashMap<Integer, Map<Integer, CraftType>>();
	private static Map<ArrayList, CraftType> arrayListCache = new HashMap<ArrayList, CraftType>();
	private int item, damage;
	private Object stack;

	private CraftType(int itemID, int itemDamage)
	{
		item = itemID;
		damage = itemDamage;
		stack = new ItemStack(item, 1, damage);
	}

	private CraftType(ArrayList<ItemStack> items)
	{
		ItemStack itemStack = items.get(0);
		item = itemStack.itemID;
		damage = CommonUtilities.getItemDamage(itemStack);
		stack = items;
	}

	public static CraftType getInstance(Object stack)
	{
		if(stack instanceof ItemStack)
		{
			return getInstance((ItemStack)stack);
		}
		else if(stack instanceof ArrayList && ((ArrayList)stack).size() > 0)
		{
			return getInstance((ArrayList)stack);
		}
		else
		{
			return null;
		}
	}

	private static CraftType getInstance(ArrayList stack)
	{
		CraftType type = arrayListCache.get(stack);

		if(type == null)
		{
			type = new CraftType(stack);
			arrayListCache.put(stack, type);
		}

		return type;
	}

	private static CraftType getInstance(ItemStack stack)
	{
		Map<Integer, CraftType> map = cache.get(stack.itemID);

		if(map == null)
		{
			map = new HashMap<Integer, CraftType>();
			cache.put(stack.itemID, map);
		}

		CraftType type = map.get(CommonUtilities.getItemDamage(stack));

		if(type == null)
		{
			type = new CraftType(stack.itemID, CommonUtilities.getItemDamage(stack));
			map.put(CommonUtilities.getItemDamage(stack), type);
		}

		return type;
	}

	public static boolean hasInstance(ItemStack stack)
	{
		if(!cache.containsKey(stack.itemID))
		{
			return false;
		}

		return cache.get(stack.itemID).containsKey(CommonUtilities.getItemDamage(stack));
	}

	@Override
	public int compareTo(CraftType other)
	{
		if(this.item != other.item)
		{
			return this.item > other.item? 1 : -1;
		}
		else if(this.damage != other.damage)
		{
			return this.damage > other.damage? 1 : -1;
		}
		else if((this.stack instanceof ArrayList) != (other.stack instanceof ArrayList))
		{
			return (this.stack instanceof ArrayList)? -1 : 1;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof CraftType)
		{
			CraftType type = (CraftType)obj;

			if(stack instanceof ItemStack && type.stack instanceof ItemStack)
			{
				return type.item == this.item && type.damage == this.damage;
			}
			else if(stack instanceof ArrayList && type.stack instanceof ArrayList)
			{
				return stack.equals(type.stack);
			}
			else
			{
				return false;
			}
		}

		return super.equals(obj);
	}

	@Override
	public int hashCode()
	{
		return damage * 3571 + item;
	}

	public Object getStack()
	{
		return stack;
	}

	public ItemStack getDisplayStack()
	{
		if(stack instanceof ItemStack)
		{
			return (ItemStack)stack;
		}
		else if(stack instanceof ArrayList)
		{
			return (ItemStack)((ArrayList)stack).get(0);
		}
		else
		{
			return null;
		}
	}
}