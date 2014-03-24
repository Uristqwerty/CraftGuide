package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
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
			Class gregTechClass = Class.forName("gregtechmod.GT_Mod");
			Object instance = gregTechClass.getField("instance").get(null);
			Block machine;

			try
			{
				machine = ((Block[])gregTechClass.getField("mBlocks").get(instance))[1];
			}
			catch(NoSuchFieldException e)
			{
				Class gregTechAPI = Class.forName("gregtechmod.api.GregTech_API");
				machine = ((Block[])gregTechAPI.getField("sBlockList").get(null))[1];
			}

			Class recipeClass = Class.forName("gregtechmod.api.util.GT_Recipe");
			Class modHandlerClass = Class.forName("gregtechmod.api.util.GT_ModHandler");

			generateRecipes(
					generator, new ItemStack(machine, 1, 80),
					(ArrayList)recipeClass.getField("sFusionRecipes").get(null),
					2, 1, -1, 0, true, "\u00a77  First reaction cost: %1$d EU");
			generateRecipes(
					generator, new ItemStack(machine, 1, 62),
					(ArrayList)recipeClass.getField("sCentrifugeRecipes").get(null),
					2, 4, 5, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 25),
					(ArrayList)recipeClass.getField("sElectrolyzerRecipes").get(null),
					2, 4, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 28),
					(ArrayList)recipeClass.getField("sGrinderRecipes").get(null),
					2, 4, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 29),
					(ArrayList)recipeClass.getField("sBlastRecipes").get(null),
					2, 2, -1, 0, false, "\u00a77  Required temperature: %1$d");
			generateRecipes(
					generator, new ItemStack(machine, 1, 31),
					(ArrayList)recipeClass.getField("sImplosionRecipes").get(null),
					2, 2, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 32),
					(ArrayList)recipeClass.getField("sSawmillRecipes").get(null),
					2, 3, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 33),
					(ArrayList)recipeClass.getField("sDieselFuels").get(null),
					1, 1, 12, 1000, true, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 34),
					(ArrayList)recipeClass.getField("sTurbineFuels").get(null),
					1, 1, 16, 1000, true, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 35),
					(ArrayList)recipeClass.getField("sHotFuels").get(null),
					1, 1, 24, 1000, true, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 36),
					(ArrayList)recipeClass.getField("sDenseLiquidFuels").get(null),
					1, 1, 8, 1000, true, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 38),
					(ArrayList)recipeClass.getField("sVacuumRecipes").get(null),
					1, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 41),
					(ArrayList)recipeClass.getField("sChemicalRecipes").get(null),
					2, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 42),
					(ArrayList)recipeClass.getField("sMagicFuels").get(null),
					1, 1, 24, 1000, true, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 44),
					(ArrayList)recipeClass.getField("sDistillationRecipes").get(null),
					2, 4, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 55),
					(ArrayList)recipeClass.getField("sWiremillRecipes").get(null),
					1, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 56),
					(ArrayList)recipeClass.getField("sAlloySmelterRecipes").get(null),
					2, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 59),
					(ArrayList)recipeClass.getField("sBenderRecipes").get(null),
					1, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 60),
					(ArrayList)recipeClass.getField("sAssemblerRecipes").get(null),
					2, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 57),
					(ArrayList)recipeClass.getField("sCannerRecipes").get(null),
					2, 2, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 37),
					(ArrayList)recipeClass.getField("sPlasmaFuels").get(null),
					1, 1, 2048, 1000, true, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 111),
					(ArrayList)recipeClass.getField("sLatheRecipes").get(null),
					1, 2, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 113),
					(ArrayList)recipeClass.getField("sCutterRecipes").get(null),
					1, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 115),
					(ArrayList)recipeClass.getField("sExtruderRecipes").get(null),
					2, 1, -1, 0, false, null);
			generateRecipes(
					generator, new ItemStack(machine, 1, 133),
					(ArrayList)recipeClass.getField("sHammerRecipes").get(null),
					1, 1, -1, 0, false, null);

			generatePulverizerRecipes(generator,
					new ItemStack(machine, 1, 64), new ItemStack(machine, 1, 130),
					(Map<Integer, Object>)CommonUtilities.getPrivateValue(modHandlerClass, null, "sPulverizerRecipes"));
		}
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
		catch(IllegalArgumentException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
		catch(SecurityException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
		catch(IllegalAccessException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
		catch(InvocationTargetException e)
		{
			CraftGuideLog.log(e, "Error while adding GregTech recipes:", true);
		}
	}

	@SuppressWarnings("null")
	private void generateRecipes(RecipeGenerator generator, ItemStack machine, ArrayList recipes,
			int numInputs, int numOutputs, int constantEUt, int startEUOutputMult, boolean generated,
			final String extraFormat) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{

		Class recipeClass = Class.forName("gregtechmod.api.util.GT_Recipe");
		Field eutField = recipeClass.getField("mEUt");
		Field durationField = recipeClass.getField("mDuration");
		Field extraField = recipeClass.getField("mStartEU");

		Field input1 = null;
		Field input2 = null;
		Field output1 = null;
		Field output2 = null;
		Field output3 = null;
		Field output4 = null;

		Field inputs = null;
		Field outputs = null;

		try
		{
			input1 = recipeClass.getField("mInput1");
			input2 = recipeClass.getField("mInput2");
			output1 = recipeClass.getField("mOutput1");
			output2 = recipeClass.getField("mOutput2");
			output3 = recipeClass.getField("mOutput3");
			output4 = recipeClass.getField("mOutput4");
		}
		catch(NoSuchFieldException e)
		{
			inputs = recipeClass.getField("mInputs");
			outputs = recipeClass.getField("mOutputs");
		}

		if(outputs != null)
		{
			for(Object recipe: recipes)
			{
				numInputs = Math.max(numInputs, recipeLength((ItemStack[])inputs.get(recipe)));
				numOutputs = Math.max(numOutputs, recipeLength((ItemStack[])outputs.get(recipe)));
			}
		}
		else if(numInputs < 1 || numInputs > 2 || numOutputs < 1 || numOutputs > 4)
		{
			throw new IllegalArgumentException();
		}


		Slot[] recipeSlots = layoutMachineSlots(machine, numInputs, numOutputs, extraFormat);
		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, machine);

		if(slotColumns(numInputs) + slotColumns(numOutputs) > 3)
		{
			template.setSize(79 + (slotColumns(numInputs) + slotColumns(numOutputs) - 3) * 18, 58);
		}


		for(Object recipe: recipes)
		{
			Object[] recipeContents = new Object[numInputs + numOutputs + 2];

			if(output4 != null)
			{
				recipeContents[0] = input1.get(recipe);

				if(numInputs == 2)
				{
					recipeContents[1] = input2.get(recipe);
				}

				recipeContents[numInputs + 0] = output1.get(recipe);

				if(numOutputs > 1)
				{
					recipeContents[numInputs + 1] = output2.get(recipe);
				}

				if(numOutputs > 2)
				{
					recipeContents[numInputs + 2] = output3.get(recipe);
				}

				if(numOutputs > 3)
				{
					recipeContents[numInputs + 3] = output4.get(recipe);
				}
			}
			else
			{
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
			}

			int eut = (constantEUt == -1)? eutField.getInt(recipe) : constantEUt;
			int extraData = extraField.getInt(recipe);
			int duration = durationField.getInt(recipe);
			int outputEU = (startEUOutputMult != 0)? extraData * startEUOutputMult : duration * eut;

			recipeContents[numInputs + numOutputs + 0] = machine;

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

	private int recipeLength(ItemStack[] itemStacks)
	{
		for(int i = itemStacks.length; i > 0; i--)
		{
			if(itemStacks[i - 1] != null)
				return i;
		}

		return 0;
	}

	private Slot[] layoutMachineSlots(ItemStack machine, int numInputs, int numOutputs, final String extraFormat)
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

		case 9:
			return 18 * ((i % 3) - 2);

		default:
			throw new IllegalArgumentException();
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

		case 8: case 9:
			return (i / 3) * 18;

		default:
			throw new IllegalArgumentException();
		}
	}

	private int slotColumns(int numSlots)
	{
		return numSlots <= 0? 0 : numSlots <= 2? 1 : numSlots <= 6? 2 : 3;
	}

	private void generatePulverizerRecipes(RecipeGenerator generator, ItemStack electric, ItemStack steam, Map<Integer, Object> recipes) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		List<ItemStack> machines = Arrays.asList(electric, steam);

		Slot[] recipeSlots = new Slot[] {
				new ItemSlot(12, 21, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ChanceSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ExtraSlot(31, 30, 16, 16, machines).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
				new EUSlot(31, 12),
		};

		Slot[] recipeSlotsElectric = new Slot[] {
				new ItemSlot(12, 21, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ChanceSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ExtraSlot(31, 30, 16, 16, electric).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
				new EUSlot(31, 12),
		};

		Slot[] recipeSlotsSteam = new Slot[] {
				new ItemSlot(12, 21, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ChanceSlot(50, 30, 16, 16, true).setRatio(200).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ExtraSlot(31, 21, 16, 16, steam).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, machines.get(0));
		RecipeTemplate templateElectric = generator.createRecipeTemplate(recipeSlotsElectric, machines.get(0));
		RecipeTemplate templateSteam = generator.createRecipeTemplate(recipeSlotsSteam, machines.get(1));

		Class recipeClass = Class.forName("gregtechmod.api.util.GT_PulverizerRecipe");
		Method getInput = recipeClass.getMethod("getInput");
		Method getPrimaryOutput = recipeClass.getMethod("getPrimaryOutput");
		Method getSecondaryOutput = recipeClass.getMethod("getSecondaryOutput");
		Method getSecondaryOutputChance = recipeClass.getMethod("getSecondaryOutputChance");
		Method getEnergy = recipeClass.getMethod("getEnergy");

		Class prefixes = null;
		Object ore = null;
		Object denseOre = null;
		Object endOre = null;
		Object netherOre = null;
		Method contains = null;
		try
		{
			prefixes = Class.forName("gregtechmod.api.enums.OrePrefixes");
			ore = prefixes.getField("ore").get(null);
			denseOre = prefixes.getField("oreDense").get(null);
			endOre = prefixes.getField("oreEnd").get(null);
			netherOre = prefixes.getField("oreNether").get(null);
			contains = prefixes.getMethod("contains", ItemStack.class);
		}
		catch(ClassNotFoundException e)
		{
		}
		catch(NoSuchFieldException e)
		{
			prefixes = null;
		}


		for(Object recipe: recipes.values())
		{
			if(!recipeClass.isInstance(recipe))
				continue;

			Object input = getInput.invoke(recipe);

			ItemStack output = (ItemStack)getPrimaryOutput.invoke(recipe);
			if(prefixes != null && isOre(input, ore, denseOre, endOre, netherOre, contains))
			{
				Object[] recipeContentsElectric = new Object[] {
						input,
						output,
						new Object[]{
							getSecondaryOutput.invoke(recipe),
							getSecondaryOutputChance.invoke(recipe),
						},
						electric,
						new Object[]{getEnergy.invoke(recipe), 3},
				};

				generator.addRecipe(templateElectric, recipeContentsElectric);

				ItemStack steamOutput = output;

				if(steamOutput.stackSize > 1)
				{
					steamOutput = output.copy();
					steamOutput.stackSize /= 2;
				}

				Object[] recipeContentsSteam = new Object[] {
						input,
						steamOutput,
						new Object[]{
							getSecondaryOutput.invoke(recipe),
							getSecondaryOutputChance.invoke(recipe),
						},
						steam,
				};

				generator.addRecipe(templateSteam, recipeContentsSteam);
			}
			else
			{
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
	}

	private boolean isOre(Object input, Object ore, Object denseOre, Object endOre, Object netherOre, Method contains) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		return	(Boolean)contains.invoke(ore, input) ||
				(Boolean)contains.invoke(denseOre, input) ||
				(Boolean)contains.invoke(endOre, input) ||
				(Boolean)contains.invoke(netherOre, input);
	}

	@Override
	public ItemStack[] extraMacerators()
	{
		try
		{
			Block machine = ((Block[])Class.forName("gregtechmod.api.GregTech_API").getField("sBlockList").get(null))[1];
			return new ItemStack[] {
				new ItemStack(machine, 1, 50),
			};
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ItemStack[] extraExtractors()
	{
		try
		{
			Block machine = ((Block[])Class.forName("gregtechmod.api.GregTech_API").getField("sBlockList").get(null))[1];
			return new ItemStack[] {
				new ItemStack(machine, 1, 51),
				new ItemStack(machine, 1, 135),
			};
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ItemStack[] extraCompressors()
	{
		try
		{
			Block machine = ((Block[])Class.forName("gregtechmod.api.GregTech_API").getField("sBlockList").get(null))[1];
			return new ItemStack[] {
				new ItemStack(machine, 1, 52),
				new ItemStack(machine, 1, 134),
			};
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
