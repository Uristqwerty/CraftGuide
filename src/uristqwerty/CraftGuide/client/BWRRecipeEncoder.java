package uristqwerty.CraftGuide.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuide;

public class BWRRecipeEncoder
{
	private static NBTTagCompound data = new NBTTagCompound();
	private static byte[] packetData;

	public static void addMillstoneRecipe(ItemStack[] input, ItemStack[] output)
	{
		addRecipe("millstone", input, output);
	}
	public static void addCauldronRecipe(ItemStack[] input, ItemStack[] output)
	{
		addRecipe("cauldron", input, output);
	}
	public static void addStokedCauldronRecipe(ItemStack[] input, ItemStack[] output)
	{
		addRecipe("cauldronStoked", input, output);
	}
	public static void addCrucibleRecipe(ItemStack[] input, ItemStack[] output)
	{
		addRecipe("crucible", input, output);
	}
	public static void addStokedCrucibleRecipe(ItemStack[] input, ItemStack[] output)
	{
		addRecipe("crucibleStoked", input, output);
	}

	public static void addMillstoneRecipe(Object[] input, Object[] output)
	{
		addRecipe("millstone", input, output);
	}
	public static void addCauldronRecipe(Object[] input, Object[] output)
	{
		addRecipe("cauldron", input, output);
	}
	public static void addStokedCauldronRecipe(Object[] input, Object[] output)
	{
		addRecipe("cauldronStoked", input, output);
	}
	public static void addCrucibleRecipe(Object[] input, Object[] output)
	{
		addRecipe("crucible", input, output);
	}
	public static void addStokedCrucibleRecipe(Object[] input, Object[] output)
	{
		addRecipe("crucibleStoked", input, output);
	}

	/**
	 * Currently, input and output arrays can contain ItemStacks and Object[]s (which are interpreted recursively)
	 *
	 * This is mostly because some recipes may, currently or in the future,
	 *  include an ItemStack[], representing an input that allows any one of
	 *  a set of items (a bit more flexible than just a data value of -1).
	 *
	 * This is mostly found in Forge ore dictionary recipes, which BTW obviously
	 *  doesn't have, so is unlikely to be used.
	 */
	public static void addRecipe(String key, Object[] input, Object[] output)
	{
		NBTTagList list = data.getTagList(key);

		NBTTagList inputList = new NBTTagList();
		NBTTagList outputList = new NBTTagList();

		for(Object inputItem: input)
		{
			inputList.appendTag(itemTag(inputItem));
		}

		for(Object outputItem: output)
		{
			outputList.appendTag(itemTag(outputItem));
		}

		NBTTagCompound recipe = new NBTTagCompound();
		recipe.setTag("input", inputList);
		recipe.setTag("output", outputList);
		list.appendTag(recipe);
		data.setTag(key, list);
		packetData = null;
	}

	public static byte[] getPacketData()
	{
		if(packetData == null)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			try
			{
				stream.write(0);
				CompressedStreamTools.writeCompressed(data, stream);
				packetData = stream.toByteArray();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		return packetData;
	}

	private static NBTBase itemTag(Object item)
	{
		if(item instanceof ItemStack)
		{
			ItemStack stack = (ItemStack)item;
			NBTTagCompound itemCompound = new NBTTagCompound();

			itemCompound.setInteger("id", stack.itemID);

			if(stack.stackSize != 1)
			{
				itemCompound.setInteger("count", stack.stackSize);
			}

			if(CommonUtilities.getItemDamage(stack) != CraftGuide.DAMAGE_WILDCARD)
			{
				itemCompound.setInteger("damage", CommonUtilities.getItemDamage(stack));
			}

			if(stack.hasTagCompound())
			{
				itemCompound.setCompoundTag("tagData", (NBTTagCompound)stack.getTagCompound().copy());
			}

			return itemCompound;
		}
		else if(item instanceof Object[])
		{
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagList contents = new NBTTagList();

			for(Object object: (Object[])item)
			{
				contents.appendTag(itemTag(object));
			}

			compound.setBoolean("isArray", true);
			compound.setTag("contents", contents);
			return compound;
		}

		return null;
	}
}
