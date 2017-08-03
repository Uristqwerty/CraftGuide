package uristqwerty.CraftGuide.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder.TemplateBuilderSlotType;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftSilicon;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.api.recipes.IFlexibleRecipe;
import buildcraft.api.recipes.IFlexibleRecipeViewable;
import buildcraft.api.recipes.IIntegrationRecipe;

public class BuildCraftRecipes extends CraftGuideAPIObject implements RecipeProvider
{
	@Override
	public void generateRecipes(RecipeGenerator generator)
	{

		addAssemblyRecipes(generator,
				new ItemStack(BuildCraftSilicon.assemblyTableBlock),
				new ItemStack(BuildCraftSilicon.laserBlock));
		addIntegrationRecipes(generator,
				new ItemStack(BuildCraftSilicon.assemblyTableBlock, 1, 2),
				new ItemStack(BuildCraftSilicon.laserBlock));
		addRefineryRecipes(generator, new ItemStack(BuildCraftFactory.refineryBlock));
	}

	private void addAssemblyRecipes(RecipeGenerator generator, ItemStack table, ItemStack laser)
	{
		int maxInput = 1;

		for(IFlexibleRecipe<ItemStack> recipe: BuildcraftRecipeRegistry.assemblyTable.getRecipes())
		{
			if(recipe instanceof IFlexibleRecipeViewable)
			{
				IFlexibleRecipeViewable viewable = (IFlexibleRecipeViewable)recipe;
				maxInput = Math.max(maxInput, viewable.getInputs().size());
			}
		}

		int rows = (maxInput + 2) / 3;
		ConstructedRecipeTemplate template = generator.buildTemplate(table)
			.shapelessItemGrid(3, rows).nextColumn(1)
			.outputItem().machineItem().machineItem()
			.finishTemplate();

		for(IFlexibleRecipe<ItemStack> recipe: BuildcraftRecipeRegistry.assemblyTable.getRecipes())
		{
			if(recipe instanceof IFlexibleRecipeViewable)
			{
				IFlexibleRecipeViewable viewable = (IFlexibleRecipeViewable)recipe;
				template.buildRecipe()
					.shapelessItemGrid(convertAll(viewable.getInputs()))
					.item(viewable.getOutput()).item(table).item(laser)
					.addRecipe(generator);
			}
		}
	}

	private void addIntegrationRecipes(RecipeGenerator generator, ItemStack table, ItemStack laser)
	{
		List<? extends IIntegrationRecipe> recipes = BuildcraftRecipeRegistry.integrationTable.getRecipes();
		int maxExpansions = 0;

		for(IIntegrationRecipe recipe: recipes)
		{
			List<List<ItemStack>> components = recipe.getExampleExpansions();
			maxExpansions = Math.max(maxExpansions, components.size());
		}

		int expansionCols = maxExpansions < 3? 1 : 2;
		int expansionRows = (maxExpansions + expansionCols - 1) / expansionCols;

		ConstructedRecipeTemplate template = generator.buildTemplate(table)
			.item().shapelessItemGrid(expansionCols, expansionRows).nextColumn(1)
			.machineItem().machineItem().nextColumn(1)
			.outputItem()
			.finishTemplate();

		for(IIntegrationRecipe recipe: recipes)
		{
			template.buildRecipe()
				.item(recipe.getExampleInput()).shapelessItemGrid(recipe.getExampleExpansions())
				.item(laser).item(table)
				.item(recipe.getExampleOutput())
				.addRecipe(generator);
		}
	}

	private Object[] convertAll(Collection<?> inputs)
	{
		ArrayList<Object> out = new ArrayList<>();

		for(Object in: inputs)
			out.add(convert(in));

		return out.toArray();
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
		ConstructedRecipeTemplate oneInput = generator.buildTemplate(refinery)
				.liquid().nextColumn(1)
				.machineItem().nextColumn(1)
				.nextSlotType(TemplateBuilderSlotType.OUTPUT).liquid()
				.finishTemplate();

		ConstructedRecipeTemplate twoInputs = generator.buildTemplate(refinery)
				.liquid().liquid().nextColumn(1)
				.machineItem().nextColumn(1)
				.nextSlotType(TemplateBuilderSlotType.OUTPUT).liquid()
				.finishTemplate();

		for(IFlexibleRecipe<FluidStack> recipe: BuildcraftRecipeRegistry.refinery.getRecipes())
		{
			if(recipe instanceof IFlexibleRecipeViewable)
			{
				IFlexibleRecipeViewable viewable = (IFlexibleRecipeViewable)recipe;
				List<Object> inputs = (List<Object>)viewable.getInputs();

				if(inputs.size() == 1)
				{
					oneInput.buildRecipe()
						.liquid((FluidStack)inputs.get(0))
						.item(refinery)
						.liquid((FluidStack)viewable.getOutput())
						.addRecipe(generator);
				}
				else if(inputs.size() == 2)
				{
					twoInputs.buildRecipe()
						.liquid((FluidStack)inputs.get(0)).liquid((FluidStack)inputs.get(1))
						.item(refinery)
						.liquid((FluidStack)viewable.getOutput())
						.addRecipe(generator);
				}
				else
				{
					CraftGuideLog.log("Warning: Unexpected input count for BuildCraft recinery recipe: " + inputs.size());
				}
			}
		}
	}
}
