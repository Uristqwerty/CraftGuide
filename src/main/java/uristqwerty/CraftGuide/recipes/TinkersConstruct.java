package uristqwerty.CraftGuide.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate.RecipeBuilder;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder.TemplateBuilderSlotType;

public class TinkersConstruct implements RecipeProvider
{
	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		ItemStack smelteryController = new ItemStack(TinkerSmeltery.smelteryController);
		ItemStack smelteryLavaTank = new ItemStack(TinkerSmeltery.searedTank);
		ItemStack basin = new ItemStack(TinkerSmeltery.castingBlock, 1, 1);
		ItemStack castTable = new ItemStack(TinkerSmeltery.castingBlock, 1, 0);
		ItemStack liquidOutput = new ItemStack(TinkerSmeltery.smelteryIO);

		if(FluidRegistry.isUniversalBucketEnabled())
		{
			ConstructedRecipeTemplate template = generator.buildTemplate(smelteryLavaTank)
					.item().nextColumn(1)
					.machineItem().machineItem().machineItem().nextColumn(1)
					.nextSlotType(TemplateBuilderSlotType.OUTPUT)
					.liquid().finishTemplate();

			UniversalBucket bucket = ForgeModContainer.getInstance().universalBucket;
			SortedSet<Integer> heatLevels = new TreeSet<>();
			for(FluidStack fluid: TinkerRegistry.getSmelteryFuels())
			{
				if(FluidRegistry.getBucketFluids().contains(fluid.getFluid()))
				{
					heatLevels.add(fluid.getFluid().getTemperature(fluid));
				}
			}
			HashMap<Integer, ArrayList<ItemStack>> fuelSets = new HashMap<>();
			for(MeltingRecipe recipe: TinkerRegistry.getAllMeltingRecipies())
			{
				Integer found = null;
				for(Integer i: heatLevels)
				{
					if(i >= recipe.temperature)
					{
						found = i;
						break;
					}
				}
				if(found == null)
					continue;
				ArrayList<ItemStack> fuels = fuelSets.get(found);
				if(fuels == null)
				{
					fuels = new ArrayList<>();
					fuelSets.put(found, fuels);

					for(FluidStack fluid: TinkerRegistry.getSmelteryFuels())
					{
						if(fluid.getFluid().getTemperature(fluid) >= recipe.temperature
								&& FluidRegistry.getBucketFluids().contains(fluid.getFluid()))
						{
							fuels.add(UniversalBucket.getFilledBucket(bucket, fluid.getFluid()));
						}
					}
				}
				template.buildRecipe()
					.item(convertInput(recipe.input))
					.item(smelteryController).item(smelteryLavaTank).item(fuels)
					.liquid(recipe.output)
					.addRecipe(generator);
			}
		}

		int maxAlloyInputs = 0;
		for(AlloyRecipe recipe: TinkerRegistry.getAlloys())
		{
			maxAlloyInputs = Math.max(maxAlloyInputs, recipe.getFluids().size());
		}

		RecipeTemplateBuilder alloyBuilder = generator.buildTemplate(smelteryController);
		for(int i = 0; i < maxAlloyInputs; i++)
		{
			alloyBuilder.liquid();
			if(i % 4 == 3)
				alloyBuilder.nextColumn();
		}

		ConstructedRecipeTemplate alloyTemplate = alloyBuilder.nextColumn(1)
			.machineItem().nextColumn()
			.nextSlotType(TemplateBuilderSlotType.OUTPUT)
			.liquid().finishTemplate();

		for(AlloyRecipe recipe: TinkerRegistry.getAlloys())
		{
			RecipeBuilder builder = alloyTemplate.buildRecipe();
			List<FluidStack> inputs = recipe.getFluids();

			for(int i = 0; i < maxAlloyInputs; i++)
			{
				if(i < inputs.size())
					builder.liquid(inputs.get(i));
				else
					builder.liquid((FluidStack)null);
			}

			builder.item(smelteryController)
				.liquid(recipe.getResult())
				.addRecipe(generator);
		}

		ConstructedRecipeTemplate basinCasting = generator.buildTemplate(basin)
				.liquid().item().nextColumn(1)
				.machineItem().machineItem().nextColumn(1)
				.outputItem().finishTemplate();

		for(CastingRecipe recipe: TinkerRegistry.getAllBasinCastingRecipes())
		{
			basinCasting.buildRecipe()
				.liquid(recipe.getFluid()).item(convertInput(recipe.cast))
				.item(liquidOutput).item(basin)
				.item(recipe.getResult())
				.addRecipe(generator);
		}

		ConstructedRecipeTemplate tableCasting = generator.buildTemplate(castTable)
				.liquid().item().nextColumn(1)
				.machineItem().machineItem().nextColumn(1)
				.outputItem().finishTemplate();

		for(CastingRecipe recipe: TinkerRegistry.getAllBasinCastingRecipes())
		{
			tableCasting.buildRecipe()
				.liquid(recipe.getFluid()).item(convertInput(recipe.cast))
				.item(liquidOutput).item(castTable)
				.item(recipe.getResult())
				.addRecipe(generator);
		}
	}

	private List<ItemStack> convertInput(RecipeMatch input)
	{
		if(input == null || input.getInputs() == null)
			return null;

		ArrayList<ItemStack> result = new ArrayList<>();

		for(ItemStack item: input.getInputs())
		{
			if(item != null)
			{
				ItemStack copy = item.copy();
				copy.stackSize = input.amountNeeded;
				result.add(copy);
			}
		}

		if(result.isEmpty())
			return null;
		return result;
	}
}
