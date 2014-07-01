package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.LiquidSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import buildcraft.api.recipes.BuildcraftRecipes;
import buildcraft.api.recipes.IAssemblyRecipeManager.IAssemblyRecipe;
import buildcraft.api.recipes.IIntegrationRecipeManager.IIntegrationRecipe;
import buildcraft.api.recipes.IRefineryRecipeManager.IRefineryRecipe;

public class BuildCraftRecipes extends CraftGuideAPIObject implements RecipeProvider
{
	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			Class silicon = Class.forName("buildcraft.BuildCraftSilicon");
			addAssemblyRecipes(generator,
					new ItemStack((Block)silicon.getField("assemblyTableBlock").get(null), 1, 0),
					new ItemStack((Block)silicon.getField("laserBlock").get(null)));

			addIntegrationRecipes(generator,
					new ItemStack((Block)silicon.getField("assemblyTableBlock").get(null), 1, 2),
					new ItemStack((Block)silicon.getField("laserBlock").get(null)));

			Class factory = Class.forName("buildcraft.BuildCraftFactory");
			addRefineryRecipes(generator,
					new ItemStack((Block)factory.getField("refineryBlock").get(null)));
		}
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(SecurityException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	private void addAssemblyRecipes(RecipeGenerator generator, ItemStack table, ItemStack laser)
	{
		int maxInput = 1;

		for(IAssemblyRecipe recipe: BuildcraftRecipes.assemblyTable.getRecipes())
		{
			maxInput = Math.max(maxInput, recipe.getInputs().length);
		}

		int rows = (maxInput + 2) / 3;
		Slot[] recipeSlots = new Slot[rows * 3 + 3];

		int offset = rows == 1? 18 : rows == 2? 9 : 0;

		for(int i = 0; i < rows; i++)
		{
			recipeSlots[i * 3 + 0] = new ItemSlot( 3, 3 + i * 18 + offset, 16, 16).drawOwnBackground();
			recipeSlots[i * 3 + 1] = new ItemSlot(21, 3 + i * 18 + offset, 16, 16).drawOwnBackground();
			recipeSlots[i * 3 + 2] = new ItemSlot(39, 3 + i * 18 + offset, 16, 16).drawOwnBackground();
		}

		offset = rows <= 3? 3 : 3 + (rows - 3) * 9;

		recipeSlots[rows * 3 + 0] = new ItemSlot(59, offset + 18, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		recipeSlots[rows * 3 + 1] = new ExtraSlot(59, offset +  0, 16, 16, table).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
		recipeSlots[rows * 3 + 2] = new ExtraSlot(59, offset + 36, 16, 16, laser).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, table);
		template.setSize(79, rows > 3? 58 + (rows - 3) * 18 : 58);

		for(IAssemblyRecipe recipe: BuildcraftRecipes.assemblyTable.getRecipes())
		{
			Object[] recipeContents = new Object[rows * 3 + 3];
			Object[] input = recipe.getInputs();
			for(int i = 0; i < Math.min(rows * 3, input.length); i++)
			{
				recipeContents[i] = convert(input[i]);
			}

			recipeContents[rows * 3 + 0] = recipe.getOutput();
			recipeContents[rows * 3 + 1] = table;
			recipeContents[rows * 3 + 2] = laser;

			generator.addRecipe(template, recipeContents);
		}
	}

	private void addIntegrationRecipes(RecipeGenerator generator, ItemStack table, ItemStack laser)
	{
		try
		{
			IIntegrationRecipe.class.getMethod("getComponents");
			addIntegrationRecipes_bc6(generator, table, laser);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e, "Please use BuildCraft 6.0.7+. Previous versions are no longer supported.", true);
		}
		catch (SecurityException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch (IllegalArgumentException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch (IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch (InvocationTargetException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	private void addIntegrationRecipes_bc6(RecipeGenerator generator, ItemStack table, ItemStack laser) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Method getComponents = IIntegrationRecipe.class.getMethod("getComponents");
		Method getOutput = IIntegrationRecipe.class.getMethod("getOutputForInputs", ItemStack.class, ItemStack.class, ItemStack[].class);

		int maxComponents = 0;

		for(IIntegrationRecipe recipe: BuildcraftRecipes.integrationTable.getRecipes())
		{
			ItemStack[] components = (ItemStack[])getComponents.invoke(recipe);
			maxComponents = Math.max(maxComponents, components.length);
		}

		Slot[] recipeSlots = layoutIntegrationTableSlots(table, laser, maxComponents);

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, table);

		if(slotRows(maxComponents + 2) > 3)
		{
			template.setSize(79, 58 + (slotRows(maxComponents + 2) - 3) * 18);
		}

		for(IIntegrationRecipe recipe: BuildcraftRecipes.integrationTable.getRecipes())
		{
			ItemStack a[] = recipe.getExampleInputsA();
			ArrayList<ItemStack> inputA = new ArrayList<ItemStack>(a.length);
			for(ItemStack i: a)
				inputA.add(i);

			ItemStack b[] = recipe.getExampleInputsB();
			ArrayList<ItemStack> inputB = new ArrayList<ItemStack>(b.length);
			for(ItemStack i: b)
				inputB.add(i);

			if(inputA.size() == 0 && inputB.size() == 0 && recipe.getClass().getSimpleName().equals("AdvancedFacadeRecipe"))
			{
				addFacades(inputA, false);
				addFacades(inputB, true);
			}

			ItemStack[] components = (ItemStack[]) getComponents.invoke(recipe);

			Object[] recipeContents = new Object[5 + maxComponents];

			recipeContents[0] = inputA;
			recipeContents[1] = inputB;

			for(int i = 0; i < Math.min(maxComponents, components.length); i++)
			{
				recipeContents[i + 2] = components[i];
			}

			recipeContents[maxComponents + 2] = getOutput.invoke(recipe, inputA.size() > 0? inputA.get(0) : null, inputB.size() > 0? inputB.get(0) : null, components);
			recipeContents[maxComponents + 3] = table;
			recipeContents[maxComponents + 4] = laser;

			generator.addRecipe(template, recipeContents);
		}
	}

	private void addFacades(ArrayList<ItemStack> list, boolean swapFirst)
	{
		try
		{
			Class facade = Class.forName("buildcraft.transport.ItemFacade");
			Field allFacades = facade.getField("allFacades");
			list.addAll((List<ItemStack>)allFacades.get(null));

			if(swapFirst)
			{
				ItemStack t = list.get(0);
				list.set(0, list.get(1));
				list.set(1, t);
			}
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch (ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(SecurityException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}

	private Slot[] layoutIntegrationTableSlots(ItemStack table, ItemStack laser, int maxComponents)
	{
		Slot recipeSlots[] = new Slot[5 + maxComponents];

		int inputSlots = 2 + maxComponents;
		int hOffset = slotColumns(inputSlots) == 1? 12 : 3;

		for(int i = 0; i < maxComponents + 2; i++)
		{
			recipeSlots[i] = new ItemSlot(slotX(i, inputSlots) + hOffset, slotY(i, inputSlots) + 3, 16, 16).drawOwnBackground();
		}

		int vOffset = slotRows(inputSlots) <= 3? 3 : (slotRows(inputSlots) - 3) * 9 + 3;
		hOffset += slotColumns(inputSlots) * 18 + 1;

		if(slotColumns(inputSlots) < 3)
		{
			recipeSlots[maxComponents + 2] = new ItemSlot(hOffset + 19, vOffset + 18, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			recipeSlots[maxComponents + 3] = new ExtraSlot(hOffset + 0, vOffset + 9, 16, 16, table).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
			recipeSlots[maxComponents + 4] = new ExtraSlot(hOffset + 0, vOffset + 27, 16, 16, laser).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
		}
		else
		{
			recipeSlots[maxComponents + 2] = new ItemSlot(hOffset, vOffset + 18, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			recipeSlots[maxComponents + 3] = new ExtraSlot(hOffset, vOffset + 0, 16, 16, table).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
			recipeSlots[maxComponents + 4] = new ExtraSlot(hOffset, vOffset + 36, 16, 16, laser).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
		}

		return recipeSlots;
	}

	private int slotX(int i, int numSlots)
	{
		switch(numSlots)
		{
		case 0: case 1: case 2:
			return 0;

		case 3: case 5:
			return i == numSlots - 1? 9 : i % 2 == 0? 0 : 18;

		case 4: case 6:
			return i % 2 == 0? 0 : 18;

		case 7:
			return i < 2? (i == 0? 6 : 24) : i < 4? (18 * (i - 2)) : (i == 5? 6 : 24);

		case 8:
			return i < 6? (18 * ((i % 3) - 2)) : (i == 6? 6 : 24);

		default:
			return 18 * ((i % 3) - 2);
		}
	}

	private int slotY(int i, int numSlots)
	{
		switch(numSlots)
		{
		case 0: case 1:
			return 18;

		case 2:
			return i * 18 + 9;

		case 3: case 4:
			return (i / 2) * 18 + 9;

		case 5: case 6:
			return (i / 2) * 18;

		case 7:
			return i < 2? 0 : i < 5? 18 : 36;

		default:
			return (i / 3) * 18;
		}
	}

	private int slotColumns(int numSlots)
	{
		return numSlots <= 0? 0 : numSlots <= 2? 1 : numSlots <= 6? 2 : 3;
	}

	private int slotRows(int numSlots)
	{
		return numSlots <= 0? 0 : numSlots == 1? 1 : numSlots <= 4? 2 : numSlots <= 9? 3 : (numSlots + 2)/3;
	}

	private Object convert(Object object)
	{
		if(object instanceof String)
		{
			return OreDictionary.getOres((String)object);
		}
		else if(object instanceof Item)
		{
			return new ItemStack((Item)object);
		}
		else if(object instanceof Block)
		{
			return new ItemStack((Block)object, 1, CraftGuide.DAMAGE_WILDCARD);
		}
		else
		{
			return object;
		}
	}

	private void addRefineryRecipes(RecipeGenerator generator, ItemStack refinery)
	{
		Slot[] recipeSlotsOneInput = new Slot[] {
				new LiquidSlot(12, 21),
				new LiquidSlot(50, 21).setSlotType(SlotType.OUTPUT_SLOT),
				new ExtraSlot(31, 21, 16, 16, refinery).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
		};

		Slot[] recipeSlotsTwoInputs = new Slot[] {
				new LiquidSlot(12, 12),
				new LiquidSlot(12, 30),
				new LiquidSlot(50, 21).setSlotType(SlotType.OUTPUT_SLOT),
				new ExtraSlot(31, 21, 16, 16, refinery).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
		};

		RecipeTemplate templateOneInput = generator.createRecipeTemplate(recipeSlotsOneInput, refinery);
		RecipeTemplate templateTwoInputs = generator.createRecipeTemplate(recipeSlotsTwoInputs, refinery);

		for(IRefineryRecipe recipe: BuildcraftRecipes.refinery.getRecipes())
		{
			boolean twoInputs = recipe.getIngredient2() != null;
			Object[] recipeContents = new Object[twoInputs? 4 : 3];
			recipeContents[0] = recipe.getIngredient1();

			if(twoInputs)
			{
				recipeContents[1] = recipe.getIngredient2();
			}

			recipeContents[twoInputs? 2 : 1] = recipe.getResult();
			recipeContents[twoInputs? 3 : 2] = refinery;

			generator.addRecipe(twoInputs? templateTwoInputs : templateOneInput, recipeContents);
		}
	}
}
