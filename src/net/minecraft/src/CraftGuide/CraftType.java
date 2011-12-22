package net.minecraft.src.CraftGuide;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.ItemStack;

public class CraftType implements Comparable
{
	private static Map<Integer, Map<Integer, CraftType>> cache = new HashMap<Integer, Map<Integer, CraftType>>();
	private int item, damage;
	private ItemStack stack;
	
	private CraftType(int itemID, int itemDamage)
	{
		item = itemID;
		damage = itemDamage;
		stack = new ItemStack(item, 1, damage);
	}

	public static CraftType getInstance(ItemStack stack)
	{
		Map<Integer, CraftType> map = cache.get(stack.itemID);
		
		if(map == null)
		{
			map = new HashMap<Integer, CraftType>();
			cache.put(stack.itemID, map);
		}
		
		CraftType type = map.get(stack.getItemDamage());
		
		if(type == null)
		{
			type = new CraftType(stack.itemID, stack.getItemDamage());
			map.put(stack.getItemDamage(), type);
		}
		
		return type;
	}

	public static boolean hasInstance(ItemStack stack)
	{
		if(!cache.containsKey(stack.itemID))
		{
			return false;
		}
		
		return cache.get(stack.itemID).containsKey(stack.getItemDamage());
	}

	@Override
	public int compareTo(Object o)
	{
		CraftType other = (CraftType)o;
		
		if(this.item != other.item)
		{
			return this.item > other.item? 1 : -1;
		}
		else if(this.damage != other.damage)
		{
			return this.damage > other.damage? 1 : -1;
		}
		
		return 0;
	}

	public ItemStack getStack()
	{
		return stack;
	}
}