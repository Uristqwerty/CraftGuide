package uristqwerty.CraftGuide.itemtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uristqwerty.CraftGuide.CommonUtilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemType implements Comparable<ItemType>
{
	private static final class ItemTypeKey
	{
		public final int id, damage;

		public ItemTypeKey(ItemStack stack) {
			this(stack.getItem(), stack.getItemDamage());
		}

		public ItemTypeKey(Item item, int damage)
		{
			this(Item.getIdFromItem(item), damage);
		}

		public ItemTypeKey(int id, int damage)
		{
			this.id = id;
			this.damage = damage;
		}

		// Automatically-generated .hashCode(), with prime increased from 31
		@Override
		public int hashCode()
		{
			final int prime = 104147;
			int result = 1;
			result = prime * result + damage;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(this.getClass() != obj.getClass())
				return false;

			ItemTypeKey other = (ItemTypeKey) obj;
			return this.id == other.id &&
					this.damage == other.damage;
		}
	}

	private static Map<ItemTypeKey, ItemType> cache = new HashMap<ItemTypeKey, ItemType>();
	private static Map<ArrayList, ItemType> arrayListCache = new HashMap<ArrayList, ItemType>();
	private int itemID, damage;
	private Item item;
	private Object stack;

	private ItemType(Item item, int itemDamage)
	{
		this.item = item;
		itemID = Item.getIdFromItem(item);
		damage = itemDamage;
		stack = new ItemStack(item, 1, damage);
	}

	private ItemType(ArrayList<ItemStack> items)
	{
		ItemStack itemStack = items.get(0);
		item = itemStack.getItem();
		itemID = Item.getIdFromItem(item);
		damage = CommonUtilities.getItemDamage(itemStack);
		stack = items;
	}

	public static ItemType getInstance(Object stack)
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

	private static ItemType getInstance(ArrayList stack)
	{
		ItemType type = arrayListCache.get(stack);

		if(type == null)
		{
			type = new ItemType(stack);
			arrayListCache.put(stack, type);
		}

		return type;
	}

	private static ItemType getInstance(ItemStack stack)
	{
		ItemTypeKey key = new ItemTypeKey(stack);
		ItemType type = cache.get(key);

		if(type == null)
		{
			type = new ItemType(stack.getItem(), CommonUtilities.getItemDamage(stack));
			cache.put(key, type);
		}

		return type;
	}

	public static boolean hasInstance(ItemStack stack)
	{
		ItemTypeKey key = new ItemTypeKey(stack);
		return cache.containsKey(key);
	}

	@Override
	public int compareTo(ItemType other)
	{
		if(this.itemID != other.itemID)
		{
			return this.itemID > other.itemID? 1 : -1;
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
		if(obj != null && obj instanceof ItemType)
		{
			ItemType type = (ItemType)obj;

			if(stack instanceof ItemStack && type.stack instanceof ItemStack)
			{
				return type.itemID == this.itemID && type.damage == this.damage;
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
		return damage * 3571 + itemID;
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