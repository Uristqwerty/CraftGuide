package uristqwerty.CraftGuide.itemtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import uristqwerty.CraftGuide.CommonUtilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemType implements Comparable<ItemType>
{
	private static final ItemTypeKey errKey = new ItemTypeKey(Integer.MAX_VALUE, Integer.MIN_VALUE);
	private static final ItemType errType_nullItem = new ItemType(errKey, "Error\nOne or more recipe contains an ItemStack with a null item.");
	private static final ItemType errType_unknownType = new ItemType(errKey, "Error\nItemType requested for unknown type of item.");

	private static final class ItemTypeKey implements Comparable<ItemTypeKey>
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

		@Override
		public int compareTo(ItemTypeKey other)
		{
			if(this.id != other.id)
				return this.id > other.id? 1 : -1;
			else
				return this.damage == other.damage? 0 :
					this.damage > other.damage? 1 : -1;
		}
	}

	private static Map<ItemTypeKey, ItemType> simpleItemStacks = new HashMap<ItemTypeKey, ItemType>();
	private static Map<ItemTypeKey, List<ItemType>> nbtItemStacks = new HashMap<ItemTypeKey, List<ItemType>>();
	private static Map<String, ItemType> oreDictionaryEntries = new HashMap<String, ItemType>();
	private static Map<ArrayList<ItemStack>, ItemType> generalArrayLists = new IdentityHashMap<ArrayList<ItemStack>, ItemType>();

	private final ItemTypeKey key;
	private final Item item;
	private final Object stack;

	private final int originalHashcode;

	private ItemType(Item item, int itemDamage)
	{
		this.item = item;
		key = new ItemTypeKey(Item.getIdFromItem(item), itemDamage);
		stack = new ItemStack(item, 1, itemDamage);
		originalHashcode = 0;
	}

	private ItemType(ItemTypeKey key)
	{
		this.item = Item.getItemById(key.id);
		this.key = key;
		this.stack = new ItemStack(item, 1, key.damage);
		this.originalHashcode = 0;
	}

	private ItemType(ArrayList<ItemStack> items)
	{
		ItemStack itemStack = items.get(0);
		this.item = itemStack.getItem();
		this.key = new ItemTypeKey(Item.getIdFromItem(item), CommonUtilities.getItemDamage(itemStack));
		this.stack = items;
		this.originalHashcode = items.hashCode();
	}

	private ItemType(ItemTypeKey key, NBTTagCompound nbt, int nbtHash)
	{
		this.item = Item.getItemById(key.id);
		this.key = key;
		ItemStack stack = new ItemStack(item, 1, key.damage);
		stack.setTagCompound((NBTTagCompound)nbt.copy());
		this.stack = stack;
		this.originalHashcode = nbtHash;
	}

	private ItemType(ItemTypeKey key, String msg) {
		this.item = null;
		this.key = key;
		this.stack = msg;
		this.originalHashcode = msg.hashCode();
	}

	public static ItemType getInstance(Object stack)
	{
		if(stack instanceof ItemStack)
		{
			return getInstance((ItemStack)stack);
		}
		else if(stack instanceof ArrayList<?> && ((ArrayList<?>)stack).size() > 0)
		{
			for(Object o: (ArrayList<?>)stack)
			{
				if(o != null && !(o instanceof ItemStack))
					return errType_unknownType;
			}

			return getInstance((ArrayList<ItemStack>)stack);
		}
		else
		{
			return errType_unknownType;
		}
	}

	private static ItemType getInstance(ArrayList<ItemStack> stack)
	{
		ItemType type = generalArrayLists.get(stack);

		if(type == null)
		{
			type = new ItemType(stack);
			generalArrayLists.put(stack, type);
		}

		return type;
	}

	private static ItemType getInstance(ItemStack stack)
	{
		if(stack.getItem() == null)
			return errType_nullItem;

		ItemTypeKey key = new ItemTypeKey(stack);
		ItemType type;

		if(!stack.hasTagCompound())
		{
			type = simpleItemStacks.get(key);

			if(type == null)
			{
				type = new ItemType(key);
				simpleItemStacks.put(key, type);
			}
		}
		else
		{
			List<ItemType> items = nbtItemStacks.get(key);

			if(items == null)
			{
				items = new ArrayList<ItemType>();
				nbtItemStacks.put(key,  items);
			}

			NBTTagCompound nbt = stack.getTagCompound();
			int nbtHash = nbt.hashCode();

			type = findMatchingNBT(items, nbt, nbtHash);

			if(type == null)
			{
				nbt = (NBTTagCompound)nbt.copy();
				type = new ItemType(key, nbt, nbtHash);
				items.add(type);
			}
		}

		return type;
	}

	private static ItemType findMatchingNBT(List<ItemType> items, NBTTagCompound nbt)
	{
		return findMatchingNBT(items, nbt, nbt.hashCode());
	}

	private static ItemType findMatchingNBT(List<ItemType> items, NBTTagCompound nbt, int nbtHash)
	{
		for(ItemType type: items)
		{
			ItemStack stack = (ItemStack)type.stack;

			if(type.originalHashcode == nbtHash && nbt.equals(stack.getTagCompound()))
			{
				return type;
			}
		}

		return null;
	}

	public static boolean hasInstance(ItemStack stack)
	{
		ItemTypeKey key = new ItemTypeKey(stack);

		if(!stack.hasTagCompound())
		{
			return simpleItemStacks.containsKey(key);
		}
		else
		{
			List<ItemType> items = nbtItemStacks.get(key);

			if(items == null)
			{
				return false;
			}

			return findMatchingNBT(items, stack.getTagCompound()) != null;
		}
	}

	@Override
	public int compareTo(ItemType other)
	{
		int keySort = this.key.compareTo(other.key);

		// First, sort by item and metadata
		if(keySort != 0)
			return keySort;

		// ArrayLists sort before ItemStacks
		if((this.stack instanceof ArrayList) != (other.stack instanceof ArrayList))
		{
			return (this.stack instanceof ArrayList)? -1 : 1;
		}

		// Sort error strings last
		if((this.stack instanceof String) != (other.stack instanceof String))
		{
			return (other.stack instanceof ArrayList)? -1 : 1;
		}

		if(this.stack instanceof ItemStack)
		{
			ItemStack thisStack = (ItemStack)this.stack;
			ItemStack otherStack = (ItemStack)other.stack;

			// Stacks with NBT data sort before stacks without
			if(thisStack.hasTagCompound() != otherStack.hasTagCompound())
			{
				return thisStack.hasTagCompound()? -1 : 1;
			}

			if(thisStack.hasTagCompound())
			{
				// Need something here, as SortedSet requires compareTo to be consistent with equals.
				return compareInts(this.hashCode(), other.hashCode());
			}
		}
		else if(this.stack instanceof String)
		{
			String thisStack = (String)this.stack;
			String otherStack = (String)other.stack;

			return thisStack.compareTo(otherStack);
		}
		else
		{
			// Need something here, as SortedSet requires compareTo to be consistent with equals.
			return compareInts(this.hashCode(), other.hashCode());
		}

		return 0;
	}

	private int compareInts(int a, int b) {
		return Integer.signum(a - b);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || !(obj instanceof ItemType))
			return super.equals(obj);

		ItemType other = (ItemType)obj;

		if(stack instanceof ItemStack && other.stack instanceof ItemStack)
		{
			if(!this.key.equals(other.key))
			{
				return false;
			}

			ItemStack thisStack = (ItemStack)this.stack;
			ItemStack otherStack = (ItemStack)other.stack;

			if(thisStack.hasTagCompound() != otherStack.hasTagCompound())
			{
				return false;
			}

			if(!thisStack.hasTagCompound())
			{
				return true;
			}
			else
			{
				return thisStack.getTagCompound().equals(otherStack.getTagCompound());
			}
		}
		else if(stack instanceof ArrayList && other.stack instanceof ArrayList)
		{
			return stack.equals(other.stack);
		}
		else if(stack instanceof String && other.stack instanceof String)
		{
			return stack.equals(other.stack);
		}
		else
		{
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return key.hashCode() ^ originalHashcode;
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
			return ((ArrayList<ItemStack>)stack).get(0);
		}
		else
		{
			return null;
		}
	}
}