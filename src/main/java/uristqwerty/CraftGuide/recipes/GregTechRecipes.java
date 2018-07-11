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
import net.minecraftforge.fluids.FluidStack;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.Pair;
import uristqwerty.CraftGuide.api.ChanceSlot;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate.RecipeBuilder;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate.SubunitBuilder;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.EUSlot;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder.SubunitDescriptor;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder.SubunitLayout;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder.TemplateBuilderSlotType;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.recipes.IC2ExperimentalRecipes.AdditionalMachines;

public class GregTechRecipes extends CraftGuideAPIObject implements RecipeProvider, AdditionalMachines
{
	private RecipeGenerator generator;
	private Class<?> recipeClass;

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

			this.generator = generator;
			this.recipeClass = recipeClass;

			generateMachine("sOreWasherRecipes", "orewasher");
			generateMachine("sThermalCentrifugeRecipes", "thermalcentrifuge");
			generateMachine("sCompressorRecipes", "compressor");
			generateMachine("sExtractorRecipes", "extractor");
			generateMachine("sRecyclerRecipes", "recycler");
			generateMachine("sFurnaceRecipes", "furnace");
			generateMachine("sMicrowaveRecipes", "microwave");
			generateMachine("sScannerFakeRecipes", "scanner");
			generateMachine("sRockBreakerFakeRecipes", "rockbreaker");
			generateMachine("sByProductList", "byproductlist");
			generateMachine("sReplicatorFakeRecipes", "replicator");
			generateMachine("sAssemblylineVisualRecipes", "fakeAssemblylineProcess");
			generateMachine("sPlasmaArcFurnaceRecipes", "plasmaarcfurnace");
			generateMachine("sArcFurnaceRecipes", "arcfurnace");
			generateMachine("sPrinterRecipes", "printer");
			generateMachine("sSifterRecipes", "sifter");
			generateMachine("sPressRecipes", "press");
			generateMachine("sLaserEngraverRecipes", "laserengraver");
			generateMachine("sMixerRecipes", "mixer");
			generateMachine("sAutoclaveRecipes", "autoclave");
			generateMachine("sElectroMagneticSeparatorRecipes", "electromagneticseparator");
			generateMachine("sPolarizerRecipes", "polarizer");
			generateMachine("sMaceratorRecipes", "macerator");
			generateMachine("sChemicalBathRecipes", "chemicalbath");
			generateMachine("sFluidCannerRecipes", "fluidcanner");
			generateMachine("sBrewingRecipes", "brewer");
			generateMachine("sFluidHeaterRecipes", "fluidheater");
			generateMachine("sDistilleryRecipes", "distillery");
			generateMachine("sFermentingRecipes", "fermenter");
			generateMachine("sFluidSolidficationRecipes", "fluidsolidifier");
			generateMachine("sFluidExtractionRecipes", "fluidextractor");
			generateMachine("sBoxinatorRecipes", "packager");
			generateMachine("sUnboxinatorRecipes", "unpackager");
			generateMachine("sFusionRecipes", "fusioncomputer");
			generateMachine("sCentrifugeRecipes", "centrifuge");
			generateMachine("sElectrolyzerRecipes", "electrolyzer");
			generateMachine("sBlastRecipes", "blastfurnace");
			generateMachine("sPrimitiveBlastRecipes", "brickedblastfurnace");
			generateMachine("sImplosionRecipes", "implosioncompressor");
			generateMachine("sVacuumRecipes", "vacuumfreezer");
			generateMachine("sChemicalRecipes", "chemicalreactor");
			generateMachine("sMultiblockChemicalRecipes", "largechemicalreactor");
			generateMachine("sDistillationRecipes", "distillationtower");
			generateMachine("sCrakingRecipes", "craker");
			generateMachine("sPyrolyseRecipes", "pyro");
			generateMachine("sWiremillRecipes", "wiremill");
			generateMachine("sBenderRecipes", "bender");
			generateMachine("sAlloySmelterRecipes", "alloysmelter");
			generateMachine("sAssemblerRecipes", "assembler");
			generateMachine("sCircuitAssemblerRecipes", "circuitassembler");
			generateMachine("sCannerRecipes", "canner");
			generateMachine("sCNCRecipes", "cncmachine");
			generateMachine("sLatheRecipes", "lathe");
			generateMachine("sCutterRecipes", "cuttingsaw");
			generateMachine("sSlicerRecipes", "slicer");
			generateMachine("sExtruderRecipes", "extruder");
			generateMachine("sHammerRecipes", "hammer");
			generateMachine("sAmplifiers", "uuamplifier");
			generateMachine("sMassFabFakeRecipes", "massfab");

			this.generator = null;
			this.recipeClass = null;

//			generateRecipes(generator, itemList, "Centrifuge",
//					recipeClass, "sCentrifugeRecipes",
//					5, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Electrolyzer",
//					recipeClass, "sElectrolyzerRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "ChemicalReactor",
//					recipeClass, "sChemicalRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Wiremill",
//					recipeClass, "sWiremillRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "AlloySmelter",
//					recipeClass, "sAlloySmelterRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Bender",
//					recipeClass, "sBenderRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Assembler",
//					recipeClass, "sAssemblerRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Canner",
//					recipeClass, "sCannerRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Lathe",
//					recipeClass, "sLatheRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Cutter",
//					recipeClass, "sCutterRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Extruder",
//					recipeClass, "sExtruderRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Bronze_Hammer",
//					recipeClass, "sHammerRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Boxinator",
//					recipeClass, "sBoxinatorRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Unboxinator",
//					recipeClass, "sUnboxinatorRecipes",
//					-1, 0, false, null);
//			generateRecipes(
//					generator, itemList, "Generator_Diesel",
//					recipeClass, "sDieselFuels",
//					12, 1000, true, null);
//			generateRecipes(
//					generator, itemList, "Generator_Gas_Turbine",
//					recipeClass, "sTurbineFuels",
//					16, 1000, true, null);

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

	private static ArrayList<ItemStack> getMachines(Class<? extends Enum<?>> itemList, String string) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException
	{
		return getMachines(itemList, string, -1);
	}

	private static ArrayList<ItemStack> getMachines(Class<? extends Enum<?>> itemList, String string, int minimumTier) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException
	{
		ArrayList<ItemStack> machines = new ArrayList<ItemStack>();

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



	public void generateMachine(String recipeField, String idSearch)
				throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException
	{
		try
		{
			ArrayList<ItemStack> machines = new ArrayList<ItemStack>();
			Class<?> gregTechAPI = Class.forName("gregtech.api.GregTech_API");
			Class<?> iMetaTileEntity = Class.forName("gregtech.api.interfaces.metatileentity.IMetaTileEntity");
			Block machineBlock = (Block)gregTechAPI.getField("sBlockMachines").get(null);
			Method getMetaName = iMetaTileEntity.getMethod("getMetaName");
			Object[] metatileentities = (Object[])gregTechAPI.getField("METATILEENTITIES").get(null);
			for(int i = 0; i < metatileentities.length; i++)
			{
				if(metatileentities[i] != null)
				{
					String metaName = (String)getMetaName.invoke(metatileentities[i]);
					for(String part: metaName.split("\\."))
						if(part.equals(idSearch))
							machines.add(new ItemStack(machineBlock, 1, i));
				}
			}

			if(machines.size() < 1)
			{
				CraftGuideLog.log("Found 0 gregtech machine blocks for " + idSearch);
				return;
			}

			Object recipes = recipeClass.getField(recipeField).get(null);
			Collection<?> recipeList;
			if(recipes instanceof Collection)
				recipeList = (Collection<?>)recipes;
			else
				recipeList = (Collection<?>)recipes.getClass().getField("mRecipeList").get(recipes);

			generateMachine(machines, recipeList);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e);
		}
		catch(InvocationTargetException e)
		{
			CraftGuideLog.log(e);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e);
		}
	}

	public void generateMachine(ArrayList<ItemStack> machines, Collection<?> recipes)
				throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{
		if(machines.size() < 1)
			return;

		int numInputs = 0;
		int numOutputs = 0;
		int numFluidInputs = 0;
		int numFluidOutputs = 0;

		ItemStack typeMachine = machines.get(0);

		Class<?> recipeClass = Class.forName("gregtech.api.util.GT_Recipe");
		Field inputs = null;
		Field outputs = null;
		Field inputFluids = null;
		Field outputFluids = null;
		Field outputChances = null;

		inputs = recipeClass.getField("mInputs");
		outputs = recipeClass.getField("mOutputs");
		inputFluids = recipeClass.getField("mFluidInputs");
		outputFluids = recipeClass.getField("mFluidOutputs");
		outputChances = recipeClass.getField("mChances");

		for(Object recipe: recipes)
		{
			numInputs = Math.max(numInputs, recipeLength((ItemStack[])inputs.get(recipe)));
			numOutputs = Math.max(numOutputs, recipeLength((ItemStack[])outputs.get(recipe)));
			numFluidInputs = Math.max(numFluidInputs, recipeLength((FluidStack[])inputFluids.get(recipe)));
			numFluidOutputs = Math.max(numFluidOutputs, recipeLength((FluidStack[])outputFluids.get(recipe)));
		}

		ConstructedRecipeTemplate template = generator.buildTemplate(typeMachine)
			.repeatedSubunit(SubunitLayout.HORIZONTAL_WRAPPING, Math.min(numFluidInputs == 0? 2 : 1, numInputs), new SubunitDescriptor()
				{
					@Override
					public void defineSubunit(RecipeTemplateBuilder b)
					{
						b.item();
					}
				}) // TODO: Replace inner class with b -> b.item()
			.nextColumn()
			.repeatedSubunit(SubunitLayout.HORIZONTAL_WRAPPING, Math.min(1, numFluidInputs), new SubunitDescriptor()
				{
					@Override
					public void defineSubunit(RecipeTemplateBuilder b)
					{
						b.liquid();
					}
				}) // TODO: Replace inner class with b -> b.liquid()
			.nextColumn(1)
			.machineItem()
			.nextSlotType(TemplateBuilderSlotType.OUTPUT)
			.repeatedSubunit(SubunitLayout.HORIZONTAL_WRAPPING, 1, new SubunitDescriptor()
			{
				@Override
				public void defineSubunit(RecipeTemplateBuilder b)
				{
					b.liquid();
				}
			}) // TODO: Replace inner class
			.nextColumn(1)
			.repeatedSubunit(SubunitLayout.HORIZONTAL_WRAPPING, Math.min(3, numOutputs), new SubunitDescriptor()
				{
					@Override
					public void defineSubunit(RecipeTemplateBuilder b)
					{
						b.chanceItem();
					}
				}) // TODO: Replace inner class
			.finishTemplate();

		for(Object recipe: recipes)
		{
			ItemStack[] outputItems = (ItemStack[])outputs.get(recipe);
			int[] chance = (int[])outputChances.get(recipe);
			ArrayList<Pair<ItemStack, Integer>> outputData = new ArrayList<Pair<ItemStack, Integer>>();
			for(int i = 0; i < outputItems.length; i++)
			{
				outputData.add(new Pair<ItemStack, Integer>(outputItems[i], chance[i]));
			}
			template.buildRecipe()
// TODO: Enjoy Lambdas
//				.subUnit((ItemStack[])inputs.get(recipe), (i, b) -> b.item(i))
//				.subUnit((FluidStack[])inputFluids.get(recipe), (f, b) -> b.liquid(f))
//				.item(machines)
//				.subUnit(outputData, (i, b) -> b.chanceItem(i.first, i.second / 10000.0))
//				.subUnit((FluidStack[])outputFluids.get(recipe), (f, b) -> b.liquid(f))
//				.addRecipe(generator);
				.subUnit((ItemStack[])inputs.get(recipe), new SubunitBuilder<ItemStack>()
					{
						@Override
						public void build(ItemStack i, RecipeBuilder b)
						{
							b.item(i);
						}
					})
				.subUnit((FluidStack[])inputFluids.get(recipe), new SubunitBuilder<FluidStack>()
					{
						@Override
						public void build(FluidStack f, RecipeBuilder b)
						{
							b.liquid(f);
						}
					})
				.item(machines)
				.subUnit((FluidStack[])outputFluids.get(recipe), new SubunitBuilder<FluidStack>()
				{
					@Override
					public void build(FluidStack f, RecipeBuilder b)
					{
						b.liquid(f);
					}
				})
				.subUnit(outputData, new SubunitBuilder<Pair<ItemStack, Integer>>()
					{
						@Override
						public void build(Pair<ItemStack, Integer> i, RecipeBuilder b)
						{
							b.chanceItem(i.first, i.second / 10000.0);
						}
					})
				.addRecipe(generator);
		}
	}

	public static void generateRecipes(RecipeGenerator generator, Class<? extends Enum<?>> itemList, String machineName,
			Class<?> recipeClass, String recipeField, int constantEUt, int startEUOutputMult, boolean generated, final String extraFormat)
				throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException
	{
		try
		{
			ArrayList<ItemStack> machines = getMachines(itemList, machineName);
			Object recipes = recipeClass.getField("sCentrifugeRecipes").get(null);
			Collection<?> recipeList;
			if(recipes instanceof Collection)
				recipeList = (Collection<?>)recipes;
			else
				recipeList = (Collection<?>)recipes.getClass().getField("mRecipeList").get(recipes);

			generateRecipes(generator, machines, recipeList, constantEUt, startEUOutputMult, generated, extraFormat);
		}
		catch(NoSuchFieldException e)
		{
			CraftGuideLog.log(e);
		}
		catch(InvocationTargetException e)
		{
			CraftGuideLog.log(e);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e);
		}
	}

	public static void generateRecipes(RecipeGenerator generator, ArrayList<ItemStack> machines, Collection<?> recipes,
			int constantEUt, int startEUOutputMult, boolean generated, final String extraFormat)
				throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{
		if(machines.size() < 1)
			return;

		int numInputs = 0;
		int numOutputs = 0;
		int numFluidInputs = 0;
		int numFluidOutputs = 0;

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
			numFluidInputs = Math.max(numFluidInputs, recipeLength((FluidStack[])inputs.get(recipe)));
			numFluidOutputs = Math.max(numFluidOutputs, recipeLength((FluidStack[])outputs.get(recipe)));
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

	private static int recipeLength(FluidStack[] fluidStacks)
	{
		for(int i = fluidStacks.length; i > 0; i--)
		{
			if(fluidStacks[i - 1] != null)
				return i;
		}

		return 0;
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
			return i < 6? (18 * (i % 3)) : (i == 6? 6 : 24);

		default:
			return 18 * (i % 3);
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

		default:
			return (i / 3) * 18;
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
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(InvocationTargetException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch (NoSuchFieldException e)
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
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(InvocationTargetException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch (NoSuchFieldException e)
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
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(InvocationTargetException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch(NoSuchMethodException e)
		{
			CraftGuideLog.log(e, "", true);
		}
		catch (NoSuchFieldException e)
		{
			CraftGuideLog.log(e, "", true);
		}

		return null;
	}
}
