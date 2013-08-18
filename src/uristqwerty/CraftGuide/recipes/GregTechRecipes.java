package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.EUSlot;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.recipes.IC2Recipes.AdditionalMachines;

public class GregTechRecipes extends CraftGuideAPIObject implements RecipeProvider, AdditionalMachines
{
	public GregTechRecipes()
	{
		super();
		IC2Recipes.additionalMachines.add(this);
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
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
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
	}

	private void generateRecipes(RecipeGenerator generator, ItemStack machine, ArrayList recipes,
			int numInputs, int numOutputs, int constantEUt, int startEUOutputMult, boolean generated,
			final String extraFormat) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{
		Slot[] recipeSlots = new Slot[numInputs + numOutputs + 2];

		if(numInputs == 1)
		{
			recipeSlots[0] = new ItemSlot(numOutputs <= 2? 12 : 3, 21, 16, 16, true).drawOwnBackground();
		}
		else if(numInputs == 2)
		{
			recipeSlots[0] = new ItemSlot(numOutputs <= 2? 12 : 3, 12, 16, 16, true).drawOwnBackground();
			recipeSlots[1] = new ItemSlot(numOutputs <= 2? 12 : 3, 30, 16, 16, true).drawOwnBackground();
		}
		else
		{
			throw new IllegalArgumentException();
		}

		if(numOutputs == 1)
		{
			recipeSlots[numInputs + 0] = new ItemSlot(50, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		}
		else if(numOutputs == 2)
		{
			recipeSlots[numInputs + 0] = new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			recipeSlots[numInputs + 1] = new ItemSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		}
		else
		{
			recipeSlots[numInputs + 0] = new ItemSlot(41, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			recipeSlots[numInputs + 1] = new ItemSlot(59, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();

			if(numOutputs == 3)
			{
				recipeSlots[numInputs + 2] = new ItemSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			}
			else if(numOutputs == 4)
			{
				recipeSlots[numInputs + 2] = new ItemSlot(41, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
				recipeSlots[numInputs + 3] = new ItemSlot(59, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			}
			else
			{
				throw new IllegalArgumentException();
			}
		}

		recipeSlots[numInputs + numOutputs + 0] = new ExtraSlot(numOutputs <= 2? 31 : 22, 30, 16, 16, machine).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);

		if(extraFormat == null)
		{
			recipeSlots[numInputs + numOutputs + 1] = new EUSlot(numOutputs <= 2? 31 : 22, 12);
		}
		else
		{
			recipeSlots[numInputs + numOutputs + 1] =
				new EUSlot(numOutputs <= 2? 31 : 22, 12)
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

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, machine);

		Class recipeClass = Class.forName("gregtechmod.api.util.GT_Recipe");
		Field eutField = recipeClass.getField("mEUt");
		Field durationField = recipeClass.getField("mDuration");
		Field extraField = recipeClass.getField("mStartEU");

		for(Object recipe: recipes)
		{
			Object[] recipeContents = new Object[numInputs + numOutputs + 2];
			recipeContents[0] = recipeClass.getField("mInput1").get(recipe);

			if(numInputs == 2)
			{
				recipeContents[1] = recipeClass.getField("mInput2").get(recipe);
			}

			recipeContents[numInputs + 0] = recipeClass.getField("mOutput1").get(recipe);

			if(numOutputs > 1)
			{
				recipeContents[numInputs + 1] = recipeClass.getField("mOutput2").get(recipe);
			}

			if(numOutputs > 2)
			{
				recipeContents[numInputs + 2] = recipeClass.getField("mOutput3").get(recipe);
			}

			if(numOutputs > 3)
			{
				recipeContents[numInputs + 3] = recipeClass.getField("mOutput4").get(recipe);
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
