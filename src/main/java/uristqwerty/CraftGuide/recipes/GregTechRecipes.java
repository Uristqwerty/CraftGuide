package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.ChanceSlot;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.EUSlot;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.recipes.IC2ExperimentalRecipes.AdditionalMachines;

public class GregTechRecipes extends CraftGuideAPIObject implements RecipeProvider, AdditionalMachines
{
	public GregTechRecipes()
	{
		super();
		IC2ExperimentalRecipes.additionalMachines.add(this);
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			Class<? extends Enum<?>> itemList = (Class<? extends Enum<?>>)Class.forName("gregtech.api.enums.ItemList");
			Class<?> recipeClass;

			try
			{
				recipeClass = Class.forName("gregtech.api.util.GT_Recipe$GT_Recipe_Map");
			}
			catch(ClassNotFoundException e)
			{
				recipeClass = Class.forName("gregtech.api.util.GT_Recipe");
			}

			generateRecipes(
					generator, getMachines(itemList, "Centrifuge"),
					(ArrayList<?>)recipeClass.getField("sCentrifugeRecipes").get(null),
					5, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Electrolyzer"),
					(ArrayList<?>)recipeClass.getField("sElectrolyzerRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "ChemicalReactor"),
					(ArrayList<?>)recipeClass.getField("sChemicalRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Wiremill"),
					(ArrayList<?>)recipeClass.getField("sWiremillRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "AlloySmelter"),
					(ArrayList<?>)recipeClass.getField("sAlloySmelterRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Bender"),
					(ArrayList<?>)recipeClass.getField("sBenderRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Assembler"),
					(ArrayList<?>)recipeClass.getField("sAssemblerRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Canner"),
					(ArrayList<?>)recipeClass.getField("sCannerRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Lathe"),
					(ArrayList<?>)recipeClass.getField("sLatheRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Cutter"),
					(ArrayList<?>)recipeClass.getField("sCutterRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Extruder"),
					(ArrayList<?>)recipeClass.getField("sExtruderRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Bronze_Hammer"),
					(ArrayList<?>)recipeClass.getField("sHammerRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Boxinator"),
					(ArrayList<?>)recipeClass.getField("sBoxinatorRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Unboxinator"),
					(ArrayList<?>)recipeClass.getField("sUnboxinatorRecipes").get(null),
					-1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, "Generator_Diesel"),
					(ArrayList<?>)recipeClass.getField("sDieselFuels").get(null),
					12, 1000, true, null);
			generateRecipes(
					generator, getMachines(itemList, "Generator_Gas_Turbine"),
					(ArrayList<?>)recipeClass.getField("sTurbineFuels").get(null),
					16, 1000, true, null);

			// Things GregTech 1.7 has not implemented yet (at the time of writing this):
			/*
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sFusionRecipes").get(null),
					2, 1, -1, 0, true, "\u00a77  First reaction cost: %1$d EU");
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sGrinderRecipes").get(null),
					2, 4, -1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sBlastRecipes").get(null),
					2, 2, -1, 0, false, "\u00a77  Required temperature: %1$d");
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sImplosionRecipes").get(null),
					2, 2, -1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sSawmillRecipes").get(null),
					2, 3, -1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sHotFuels").get(null),
					1, 1, 24, 1000, true, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sDenseLiquidFuels").get(null),
					1, 1, 8, 1000, true, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sVacuumRecipes").get(null),
					1, 1, -1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sMagicFuels").get(null),
					1, 1, 24, 1000, true, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sDistillationRecipes").get(null),
					2, 4, -1, 0, false, null);
			generateRecipes(
					generator, getMachines(itemList, ""),
					(ArrayList)recipeClass.getField("sPlasmaFuels").get(null),
					1, 1, 2048, 1000, true, null);
			*/

			Class<?> modHandlerClass = Class.forName("gregtech.api.util.GT_ModHandler");
			generatePulverizerRecipes(
					generator, getMachines(itemList, "Macerator", 3),
					(Map<Integer, Object>)CommonUtilities.getPrivateValue(modHandlerClass, null, "sPulverizerRecipes"));
		}
		catch(ClassNotFoundException | IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
	}

	private ArrayList<ItemStack> getMachines(Class<? extends Enum<?>> itemList, String string) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException
	{
		return getMachines(itemList, string, -1);
	}

	private ArrayList<ItemStack> getMachines(Class<? extends Enum<?>> itemList, String string, int minimumTier) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException
	{
		ArrayList<ItemStack> machines = new ArrayList<>();

		Method get = itemList.getMethod("get", long.class, Object[].class);

		Class<?> gregTechAPI = Class.forName("gregtech.api.GregTech_API");
		Object[] metatileentities = (Object[])gregTechAPI.getField("METATILEENTITIES").get(null);
		Item machineBlock = Item.getItemFromBlock((Block)gregTechAPI.getField("sBlockMachines").get(null));

		Class<?> tieredMachine = Class.forName("gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock");
		Field tierField = tieredMachine.getField("mTier");

		for(Enum<?> e: itemList.getEnumConstants())
		{
			if(e.name().endsWith(string))
			{
				ItemStack item = (ItemStack)get.invoke(e, Long.valueOf(1), new Object[0]);

				if(item != null)
				{
					if(item.getItem() == machineBlock && minimumTier >= 0)
					{
						if(item.getItemDamage() >= 0 && item.getItemDamage() < metatileentities.length &&
								metatileentities[item.getItemDamage()] != null)
						{
							int tier = tierField.getInt(metatileentities[item.getItemDamage()]);

							if(tier < minimumTier)
								continue;
						}
						else
						{
							continue;
						}
					}

					machines.add(item);
				}
			}
		}

		return machines;
	}

	public static void generateRecipes(RecipeGenerator generator, ArrayList<ItemStack> machines, Collection<?> recipes,
			int constantEUt, int startEUOutputMult, boolean generated, final String extraFormat)
				throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{
		if(machines.size() < 1)
			return;

		int numInputs = 0;
		int numOutputs = 0;

		ItemStack typeMachine = machines.get(0);

		Class<?> recipeClass = Class.forName("gregtech.api.util.GT_Recipe");
		Field eutField = recipeClass.getField("mEUt");
		Field durationField = recipeClass.getField("mDuration");
		Field extraField;
		Field inputs = null;
		Field outputs = null;
		try
		{
			extraField = recipeClass.getField("mStartEU");
		}
		catch(NoSuchFieldException e)
		{
			extraField = recipeClass.getField("mSpecialValue");
		}

		inputs = recipeClass.getField("mInputs");
		outputs = recipeClass.getField("mOutputs");

		for(Object recipe: recipes)
		{
			numInputs = Math.max(numInputs, recipeLength((ItemStack[])inputs.get(recipe)));
			numOutputs = Math.max(numOutputs, recipeLength((ItemStack[])outputs.get(recipe)));
		}

		Slot[] recipeSlots = layoutMachineSlots(machines, numInputs, numOutputs, extraFormat);
		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, typeMachine);

		if(slotColumns(numInputs) + slotColumns(numOutputs) > 3)
		{
			template.setSize(79 + (slotColumns(numInputs) + slotColumns(numOutputs) - 3) * 18, 58);
		}


		for(Object recipe: recipes)
		{
			Object[] recipeContents = new Object[numInputs + numOutputs + 2];

			ItemStack[] inputStacks = (ItemStack[])inputs.get(recipe);
			ItemStack[] outputStacks = (ItemStack[])outputs.get(recipe);

			for(int i = 0; i < Math.min(inputStacks.length, numInputs); i++)
			{
				recipeContents[i] = inputStacks[i];
			}

			for(int i = 0; i < Math.min(outputStacks.length, numOutputs); i++)
			{
				recipeContents[i + numInputs] = outputStacks[i];
			}

			int eut = (constantEUt == -1)? eutField.getInt(recipe) : constantEUt;
			int extraData = extraField.getInt(recipe);
			int duration = durationField.getInt(recipe);
			int outputEU = (startEUOutputMult != 0)? extraData * startEUOutputMult : duration * eut;

			recipeContents[numInputs + numOutputs + 0] = machines;

			if(extraFormat == null)
			{
				recipeContents[numInputs + numOutputs + 1] = new Object[]{
						outputEU * (generated? 1 : -1), eut};
			}
			else
			{
				recipeContents[numInputs + numOutputs + 1] = new Object[]{
						outputEU * (generated? 1 : -1), eut, extraData};
			}

			generator.addRecipe(template, recipeContents);
		}
	}

	private static int recipeLength(ItemStack[] itemStacks)
	{
		for(int i = itemStacks.length; i > 0; i--)
		{
			if(itemStacks[i - 1] != null)
				return i;
		}

		return 0;
	}

	public static Slot[] layoutMachineSlots(ArrayList<ItemStack> machine, int numInputs, int numOutputs, final String extraFormat)
	{
		if(numInputs > 9 || numOutputs > 9)
		{
			throw new IllegalArgumentException("CraftGuide currently cannot handle GregTech machine recipes with more than 9 inputs or 9 outputs. In the unlikely case that more are needed, please report this to the current developer of CraftGuide.");
		}

		Slot[] recipeSlots = new Slot[numInputs + numOutputs + 2];

		int recipeHOffset = Math.max(3, 30 - 9 * (slotColumns(numInputs) + slotColumns(numOutputs)));

		for(int i = 0; i < numInputs; i++)
		{
			recipeSlots[i] = new ItemSlot(recipeHOffset + slotX(i, numInputs), 3 + slotY(i, numInputs), 16, 16, true).drawOwnBackground();
		}

		int outputHOffset = recipeHOffset + 20 + slotColumns(numInputs) * 18;

		for(int i = 0; i < numOutputs; i++)
		{
			recipeSlots[numInputs + i] = new ItemSlot(outputHOffset + slotX(i, numOutputs), 3 + slotY(i, numOutputs), 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		}

		int centreHOffset = recipeHOffset + slotColumns(numInputs) * 18 + 1;

		recipeSlots[numInputs + numOutputs + 0] = new ExtraSlot(centreHOffset, 30, 16, 16, machine).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);

		if(extraFormat == null)
		{
			recipeSlots[numInputs + numOutputs + 1] = new EUSlot(centreHOffset, 12);
		}
		else
		{
			recipeSlots[numInputs + numOutputs + 1] =
				new EUSlot(centreHOffset, 12)
				{
					@Override
					public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
					{
						List<String> lines = super.getTooltip(x, y, data, dataIndex);
						lines.add(String.format(extraFormat, (((Object[])data[dataIndex])[2])));
						return lines;
					}
				};
		}

		return recipeSlots;
	}

	private static int slotX(int i, int numSlots)
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

		case 9:
			return 18 * ((i % 3) - 2);

		default:
			throw new IllegalArgumentException();
		}
	}

	private static int slotY(int i, int numSlots)
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

		case 8: case 9:
			return (i / 3) * 18;

		default:
			throw new IllegalArgumentException();
		}
	}

	private static int slotColumns(int numSlots)
	{
		return numSlots <= 0? 0 : numSlots <= 2? 1 : numSlots <= 6? 2 : 3;
	}

	private void generatePulverizerRecipes(RecipeGenerator generator, ArrayList<ItemStack> machines, Map<Integer, Object> recipes) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		if(machines.size() < 1)
			return;

		Slot[] recipeSlots = new Slot[] {
				new ItemSlot(12, 21, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ChanceSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ExtraSlot(31, 30, 16, 16, machines).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
				new EUSlot(31, 12),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, machines.get(0));

		Class<?> recipeClass = Class.forName("gregtech.api.util.GT_PulverizerRecipe");
		Method getInput = recipeClass.getMethod("getInput");
		Method getPrimaryOutput = recipeClass.getMethod("getPrimaryOutput");
		Method getSecondaryOutput = recipeClass.getMethod("getSecondaryOutput");
		Method getSecondaryOutputChance = recipeClass.getMethod("getSecondaryOutputChance");
		Method getEnergy = recipeClass.getMethod("getEnergy");

		for(Object recipe: recipes.values())
		{
			if(!recipeClass.isInstance(recipe))
				continue;

			Object input = getInput.invoke(recipe);

			ItemStack output = (ItemStack)getPrimaryOutput.invoke(recipe);

			Object[] recipeContents = new Object[] {
					input,
					output,
					new Object[]{
						getSecondaryOutput.invoke(recipe),
						getSecondaryOutputChance.invoke(recipe),
					},
					machines,
					new Object[]{getEnergy.invoke(recipe), 3},
			};

			generator.addRecipe(template, recipeContents);
		}
	}

	@Override
	public Object[] extraMacerators()
	{
		try
		{
			Class<? extends Enum<?>> itemList = (Class<? extends Enum<?>>) Class.forName("gregtech.api.enums.ItemList");
			return getMachines(itemList, "Macerator").toArray(new ItemStack[0]);
		}
		catch(IllegalArgumentException | SecurityException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}

		return null;
	}

	@Override
	public Object[] extraExtractors()
	{
		try
		{
			Class<? extends Enum<?>> itemList = (Class<? extends Enum<?>>)Class.forName("gregtech.api.enums.ItemList");
			return getMachines(itemList, "Extractor").toArray(new ItemStack[0]);
		}
		catch(IllegalArgumentException | SecurityException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}

		return null;
	}

	@Override
	public Object[] extraCompressors()
	{
		try
		{
			Class<? extends Enum<?>> itemList = (Class<? extends Enum<?>>)Class.forName("gregtech.api.enums.ItemList");
			return getMachines(itemList, "Compressor").toArray(new ItemStack[0]);
		}
		catch(IllegalArgumentException | SecurityException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}

		return null;
	}
}
