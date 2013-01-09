package uristqwerty.CraftGuide.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class BWRRecipeEncoder
{
	private static NBTTagCompound data = new NBTTagCompound();
	private static byte[] packetData;

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

			if(stack.getItemDamage() != -1)
			{
				itemCompound.setInteger("damage", stack.getItemDamage());
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
