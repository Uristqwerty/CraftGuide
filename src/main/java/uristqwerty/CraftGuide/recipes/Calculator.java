package uristqwerty.CraftGuide.recipes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import sonar.calculator.mod.CalculatorItems;
import sonar.calculator.mod.common.recipes.AlgorithmSeparatorRecipes;
import sonar.calculator.mod.common.recipes.AtomicCalculatorRecipes;
import sonar.calculator.mod.common.recipes.CalculatorRecipes;
import sonar.calculator.mod.common.recipes.ConductorMastRecipes;
import sonar.calculator.mod.common.recipes.ExtractionChamberRecipes;
import sonar.calculator.mod.common.recipes.FlawlessCalculatorRecipes;
import sonar.calculator.mod.common.recipes.PrecisionChamberRecipes;
import sonar.calculator.mod.common.recipes.ProcessingChamberRecipes;
import sonar.calculator.mod.common.recipes.ReassemblyChamberRecipes;
import sonar.calculator.mod.common.recipes.RestorationChamberRecipes;
import sonar.calculator.mod.common.recipes.ScientificRecipes;
import sonar.calculator.mod.common.recipes.StoneSeparatorRecipes;
import sonar.core.recipes.ISonarRecipe;
import sonar.core.recipes.RecipeHelperV2;
import sonar.core.recipes.RecipeItemStack;
import sonar.core.recipes.RecipeOreStack;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate.RecipeBuilder;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder;

public class Calculator implements RecipeProvider
{
	private static final ItemStack UNKNOWN;
	static
	{
		UNKNOWN = new ItemStack(Items.PAPER);
		NBTTagCompound display = new NBTTagCompound();
		display.setString("Name", "Error: Unknown type. See CraftGuide.log");
		UNKNOWN.setTagInfo("display", display);
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		add(generator, StoneSeparatorRecipes.instance(), 1, 2, CalculatorItems.stoneSeparator);
		add(generator, RestorationChamberRecipes.instance(), 1, 1, CalculatorItems.restorationChamber);
		add(generator, ReassemblyChamberRecipes.instance(), 1, 1, CalculatorItems.reassemblyChamber);
		add(generator, ProcessingChamberRecipes.instance(), 1, 1, CalculatorItems.processingChamber);
		add(generator, PrecisionChamberRecipes.instance(), 1, 2, CalculatorItems.precisionChamber);
		add(generator, ExtractionChamberRecipes.instance(), 1, 2, CalculatorItems.extractionChamber);
		add(generator, AlgorithmSeparatorRecipes.instance(), 1, 2, CalculatorItems.algorithmSeparator);
		add(generator, AtomicCalculatorRecipes.instance(), 3, 1, CalculatorItems.atomicCalculator);
		add(generator, CalculatorRecipes.instance(), 2, 1, CalculatorItems.itemCalculator);
		add(generator, ConductorMastRecipes.instance(), 1, 1, CalculatorItems.conductorMast);
		add(generator, FlawlessCalculatorRecipes.instance(), 4, 1, CalculatorItems.itemFlawlessCalculator);
		add(generator, ScientificRecipes.instance(), 2, 1, CalculatorItems.itemScientificCalculator);
	}

	private <T extends ISonarRecipe> void add(RecipeGenerator generator, RecipeHelperV2<T> recipes, int in, int out, Block block)
	{
		add(generator, recipes, in, out, new ItemStack(block));
	}

	private <T extends ISonarRecipe> void add(RecipeGenerator generator, RecipeHelperV2<T> recipes, int in, int out, Item item)
	{
		add(generator, recipes, in, out, new ItemStack(item));
	}

	private <T extends ISonarRecipe> void add(RecipeGenerator generator, RecipeHelperV2<T> recipes, int in, int out, ItemStack machine)
	{
		RecipeTemplateBuilder build = generator.buildTemplate(machine);
		for(int i = 0; i < in; i++)
			build.item();
		build.nextColumn(1).machineItem().nextColumn(1);
		for(int i = 0; i < out; i++)
			build.outputItem();

		ConstructedRecipeTemplate template = build.finishTemplate();
		for(T recipe: recipes.getRecipes())
		{
			RecipeBuilder buildRecipe = template.buildRecipe();
			for(int i = 0; i < in; i++)
				buildRecipe.item(convert(recipe.inputs().get(i)));
			buildRecipe.item(machine);
			for(int i = 0; i < out; i++)
				buildRecipe.item(convert(recipe.outputs().get(i)));
			buildRecipe.addRecipe(generator);
		}
	}

	private Object convert(Object object)
	{
		if(object instanceof ItemStack || object instanceof List)
			return object;

		if(object instanceof RecipeItemStack)
			return ((RecipeItemStack)object).stack;
		else if(object instanceof RecipeOreStack)
			return OreDictionary.getOres(((RecipeOreStack)object).oreType);

		CraftGuideLog.log("Calculator recipes: Unknown type: " + object.getClass().getName());
		return UNKNOWN;
	}
}
