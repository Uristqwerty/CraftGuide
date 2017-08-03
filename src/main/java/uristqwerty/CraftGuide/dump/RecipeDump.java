package uristqwerty.CraftGuide.dump;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.api.CraftGuideRecipe;
import uristqwerty.CraftGuide.itemtype.ItemType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

public abstract class RecipeDump
{
	private IdentityHashMap<ArrayList<ItemStack>, String> oredictLookup = new IdentityHashMap<>();

	public RecipeDump()
	{
		buildOredictLookup();
	}

	private void buildOredictLookup()
	{
		try
		{
			Class<?> oreDict = Class.forName("net.minecraftforge.oredict.OreDictionary");
			Field idMap = oreDict.getDeclaredField("oreIDs");
			Field listMap = oreDict.getDeclaredField("oreStacks");
			idMap.setAccessible(true);
			listMap.setAccessible(true);

			for(Entry<String, Integer> entry: ((HashMap<String, Integer>)idMap.get(null)).entrySet())
			{
				ArrayList<ItemStack> array = ((HashMap<Integer, ArrayList<ItemStack>>)listMap.get(null)).get(entry.getValue());

				if(array != null)
				{
					oredictLookup.put(array, entry.getKey());
				}
			}
		}
		catch(ClassNotFoundException e)
		{
		}
		catch(SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	public void dumpCraftingRecipes(OutputStream output)
	{
		try
		{
			startWriting(output);

			List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
			Set<Class<?>> classes = new HashSet<>();

			for(IRecipe recipe: recipes)
			{
				classes.add(recipe.getClass());
			}

			startArray();
			for(Class<?> c: classes)
			{
				dumpRecipes(c, recipes);
			}
			endArray();

			stopWriting();
		}
		catch(IOException | IllegalArgumentException | IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	public void exportDisplayedRecipes(OutputStream output)
	{
		try
		{
			startWriting(output);

			Map<ItemType, List<CraftGuideRecipe>> recipes = GuiCraftGuide.getInstance().getRecipeCache().getAllRecipes();

			startArray();
			for(Entry<ItemType, List<CraftGuideRecipe>> recipeSet: recipes.entrySet())
			{
				startObject("recipe_group");
				writeItemStackValue(recipeSet.getKey().getDisplayStack(), "group type");

				startArrayValue("recipes");
				for(CraftGuideRecipe recipe: recipeSet.getValue())
				{
					Object[] values = recipe.getItems();

					if(values != null)
					{
						startArray();
						dumpArrayValues(values, values.getClass().getComponentType());
						endArray();
					}
				}
				endArrayValue();
				endObject();
			}
			endArray();

			stopWriting();
		}
		catch(IOException | IllegalArgumentException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	private void dumpRecipes(Class<?> recipeClass, List<IRecipe> recipes) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		List<Field> recipeFields = new ArrayList<>();

		startObject("recipeclass");
		startArrayValue("structure");
		Class<?> c = recipeClass;
		while(c != Object.class && c != null)
		{
			startObject("class_structure");
			writeStringValue("class_name", "type", c.getName());

			Field[] fields = c.getDeclaredFields();
			startArrayValue("fields");
			for(Field field: fields)
			{
				field.setAccessible(true);
				recipeFields.add(field);

				startObject("field");
				writeStringValue("field_name", "name", field.getName());
				writeStringValue("field_type", "type", field.getType().getName());

				int modifiers = field.getModifiers();
				writeStringValue("string", "accessability",
						Modifier.isPrivate(modifiers)? "private" :
							Modifier.isProtected(modifiers)? "protected" :
								Modifier.isPublic(modifiers)? "public" : "");
				endObject();
			}
			endArrayValue();

			Method[] methods = c.getDeclaredMethods();
			startArrayValue("methods");
			for(Method method: methods)
			{
				startObject("method");
				writeStringValue("method_name", "name", method.getName());
				writeStringValue("method_type", "type", method.getReturnType().getName());

				int modifiers = method.getModifiers();
				writeStringValue("string", "accessability",
						Modifier.isPrivate(modifiers)? "private" :
							Modifier.isProtected(modifiers)? "protected" :
								Modifier.isPublic(modifiers)? "public" : "");
				endObject();
			}
			endArrayValue();
			endObject();
			c = c.getSuperclass();
		}
		endArrayValue();


		startArrayValue("recipes");
		for(IRecipe recipe: recipes)
		{
			if(recipe.getClass().equals(recipeClass))
			{
				startObject("recipe");
				writeItemStackValue(recipe.getRecipeOutput(), "getRecipeOutput()");
				for(Field field: recipeFields)
				{
					dumpField(field, recipe);
				}
				endObject();
			}
		}
		endArrayValue();
		endObject();
	}

	private void writeItemStackValue(ItemStack stack, String name) throws IOException
	{
		if(stack == null)
		{
			writeStringValue("itemstack", name, "null");
		}
		else
		{
			startObjectValue(name, "itemstack");
			writeItemStack(stack);
			endObjectValue();
		}
	}

	private void writeItemStack(ItemStack stack) throws IOException
	{
		writeStringValue("item_name", "item", stack.getItem().getUnlocalizedName());
		writeStringValue("int", "damage", Integer.toString(stack.getItemDamage()));
		writeStringValue("int", "size", Integer.toString(stack.stackSize));

		if(stack.hasTagCompound())
		{
			startObjectValue("nbt", "NBT");
			writeNBT(stack.getTagCompound());
			endObject();
		}
	}

	private void dumpField(Field field, IRecipe recipe) throws IllegalArgumentException, IOException, IllegalAccessException
	{
		if(field.getType().isArray())
		{
			dumpFieldArray(field, recipe);
		}
		else if(field.getType().isPrimitive())
		{
			dumpFieldPrimitive(field, recipe);
		}
		else
		{
			dumpFieldObject(field, recipe);
		}
	}

	private void dumpFieldArray(Field field, IRecipe recipe) throws IllegalArgumentException, IllegalAccessException, IOException
	{
		Object values = field.get(recipe);
		if(values == null)
		{
			writeStringValue("array", field.getName(), "null");
		}
		else
		{
			startArrayValue(field.getName());
			dumpArrayValues(values, field.getType().getComponentType());
			endArrayValue();
		}
	}

	private void dumpArrayValues(Object values, Class<?> type) throws IOException
	{
		if(type.isPrimitive())
		{
			for(int i = 0; i < Array.getLength(values); i++)
			{
				Object value = Array.get(values, i);
				writeString("primitive", value.toString());
			}
		}
		else if(type.isArray())
		{
			for(int i = 0; i < Array.getLength(values); i++)
			{
				Object value = Array.get(values, i);
				if(value == null)
				{
					writeString("object", "null");
				}
				else
				{
					startArray();
					dumpArrayValues(value, type.getComponentType());
					endArray();
				}
			}
		}
		else
		{
			for(int i = 0; i < Array.getLength(values); i++)
			{
				Object value = Array.get(values, i);
				if(value == null)
				{
					writeString("object", "null");
				}
				else
				{
					startObject("object");
					dumpObjectValue(value);
					endObject();
				}
			}
		}
	}

	private void dumpFieldPrimitive(Field field, IRecipe recipe) throws IllegalArgumentException, IOException, IllegalAccessException
	{
		writeStringValue("primitive_field", field.getName(), field.get(recipe).toString());
	}

	private void dumpFieldObject(Field field, IRecipe recipe) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		Object value = field.get(recipe);
		if(value == null)
		{
			writeStringValue("object", field.getName(), "null");
		}
		else
		{
			startObjectValue(field.getName(), field.getType().getName());
			dumpObjectValue(value);
			endObjectValue();
		}
	}

	private void dumpObjectValue(Object value) throws IOException
	{
		Class<?> type = value.getClass();
		if(type.equals(ItemStack.class))
		{
			writeItemStack((ItemStack)value);
		}
		else if(type.equals(String.class))
		{
			writeStringValue("string", "string literal", (String)value);
		}
		else if(List.class.isAssignableFrom(type))
		{
			String oreDict = null;
			if(ArrayList.class.isAssignableFrom(type) && (oreDict = oredictLookup.get(value)) != null)
			{
				writeStringValue("oredict_name", "Ore Dictionary entry", oreDict);
			}
			else
			{
				startArrayValue("List contents");
				for(Object o: (List<?>)value)
				{
					if(o == null)
					{
						writeString("object", "null");
					}
					else
					{
						startObject("object");
						dumpObjectValue(o);
						endObject();
					}
				}
				endArrayValue();
			}
		}
		else
		{
			writeStringValue("class", "Object type", type.getName());
			writeStringValue("string", "toString()", value.toString());
		}
	}

	private void writeNBT(NBTTagCompound tagCompound)
	{
		// TODO Auto-generated method stub

	}

	abstract void startWriting(OutputStream output) throws IOException;
	abstract void stopWriting() throws IOException;

	abstract void startArray() throws IOException;
	abstract void endArray() throws IOException;

	abstract void startObject(String type) throws IOException;
	abstract void endObject() throws IOException;

	abstract void writeString(String type, String value) throws IOException;
	abstract void writeStringValue(String type, String name, String value) throws IOException;

	abstract void startObjectValue(String name, String type) throws IOException;
	abstract void endObjectValue() throws IOException;
	abstract void startArrayValue(String name) throws IOException;
	abstract void endArrayValue() throws IOException;
}
