package uristqwerty.CraftGuide.recipes;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CrusherRecipe;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import de.ellpeck.actuallyadditions.api.recipe.LensConversionRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder.TemplateBuilderSlotType;

public class ActuallyAdditions implements RecipeProvider
{
	private static final ItemStack UNKNOWN_LENS;
	static
	{
		UNKNOWN_LENS = new ItemStack(Items.PAPER);
		NBTTagCompound display = new NBTTagCompound();
		display.setString("Name", "non-default lens (TODO: proper display)");
		UNKNOWN_LENS.setTagInfo("display", display);
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		ItemStack crusher = new ItemStack(Item.getByNameOrId("actuallyadditions:blockGrinder"));
		ConstructedRecipeTemplate crusherTemplate = generator.buildTemplate(crusher)
				.item().nextColumn(1)
				.machineItem().nextColumn(1).nextSlotType(TemplateBuilderSlotType.OUTPUT)
				.outputItem().chanceItem()
				.finishTemplate();

		for(CrusherRecipe recipe: ActuallyAdditionsAPI.CRUSHER_RECIPES)
		{
			crusherTemplate.buildRecipe()
				.item(recipe.inputStack)
				.item(crusher)
				.item(recipe.outputOneStack)
				.chanceItem(recipe.outputTwoStack, recipe.outputTwoChance / 100.0)
				.addRecipe(generator);
		}

		ItemStack empowerer = new ItemStack(Item.getByNameOrId("actuallyadditions:blockEmpowerer"));
		ItemStack stand = new ItemStack(Item.getByNameOrId("actuallyadditions:blockDisplayStand"));
		ConstructedRecipeTemplate empowererTemplate = generator.buildTemplate(empowerer)
				.item().nextColumn()
				.item().item().item().nextColumn()
				.item().nextColumn(1)
				.machineItem().machineItem().machineItem().nextColumn(1)
				.outputItem()
				.finishTemplate();

		for(EmpowererRecipe recipe: ActuallyAdditionsAPI.EMPOWERER_RECIPES)
		{
			empowererTemplate.buildRecipe()
				.item(recipe.modifier1)
				.item(recipe.modifier2).item(recipe.input).item(recipe.modifier3)
				.item(recipe.modifier4)
				.item(stand).item(empowerer).item(stand)
				.item(recipe.output)
				.addRecipe(generator);
		}
		ItemStack reconstructor = new ItemStack(Item.getByNameOrId("actuallyadditions:blockAtomicReconstructor"));
		ConstructedRecipeTemplate reconstructorTemplate = generator.buildTemplate(reconstructor)
				.item().nextColumn(1)
				.machineItem().nextColumn(1)
				.outputItem()
				.finishTemplate();
		ConstructedRecipeTemplate reconstructorTemplateLens = generator.buildTemplate(reconstructor)
				.item().nextColumn(1)
				.machineItem().machineItem().nextColumn(1)
				.outputItem()
				.finishTemplate();
		for(LensConversionRecipe recipe: ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES)
		{
			if(recipe.type == ActuallyAdditionsAPI.lensDefaultConversion)
			{
				reconstructorTemplate.buildRecipe()
					.item(recipe.inputStack)
					.item(reconstructor)
					.item(recipe.outputStack)
					.addRecipe(generator);
			}
			else
			{
				reconstructorTemplateLens.buildRecipe()
					.item(recipe.inputStack)
					.item(UNKNOWN_LENS).item(reconstructor)
					.item(recipe.outputStack)
					.addRecipe(generator);
			}
		}
	}
}
