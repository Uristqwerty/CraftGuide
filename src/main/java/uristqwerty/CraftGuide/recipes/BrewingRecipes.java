package uristqwerty.CraftGuide.recipes;

import java.util.ArrayList;
import java.util.TreeSet;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.common.brewing.VanillaBrewingRecipe;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.itemtype.ItemType;

public class BrewingRecipes implements RecipeProvider
{
	private static final int MAX_FULL_ITERATIONS = 10;
	private static final int MAX_VANILLA_ONLY_ITERATIONS = 100;

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		TreeSet<ItemType> ingredients = new TreeSet<>();
		TreeSet<ItemType> inputs = new TreeSet<>();

		for(Item item: Item.REGISTRY)
		{
			ArrayList<ItemStack> itemSet = new ArrayList<>();
			CreativeTabs tabs[] = item.getCreativeTabs();
			for(CreativeTabs tab: tabs)
			{
				item.getSubItems(item, tab, itemSet);
			}

			for(ItemStack stack: itemSet)
			{
				if(BrewingRecipeRegistry.isValidIngredient(stack))
					ingredients.add(ItemType.getInstance(stack));
				if(BrewingRecipeRegistry.isValidInput(stack))
					inputs.add(ItemType.getInstance(stack));
			}
		}

		ItemStack brewingStand = new ItemStack(Items.BREWING_STAND);
		ConstructedRecipeTemplate template = generator.buildTemplate(brewingStand)
				.item().item().nextColumn(1)
				.machineItem().nextColumn(1)
				.outputItem().finishTemplate();

		TreeSet<ItemType> discoveredInputs = new TreeSet<>();
		TreeSet<ItemType> inputsToTry = inputs;
		int iterations = 0;

		do
		{
			discoveredInputs = new TreeSet<>();

			for(IBrewingRecipe recipe: BrewingRecipeRegistry.getRecipes())
			{
				for(ItemType ingredient: ingredients)
				{
					final ItemStack ingredientStack = ingredient.getDisplayStack();
					if(recipe.isIngredient(ingredientStack))
					{
						for(ItemType input: inputsToTry)
						{
							final ItemStack inputStack = input.getDisplayStack();
							if(recipe.isInput(inputStack))
							{
								ItemStack result = recipe.getOutput(inputStack, ingredientStack);
								if(result != null)
								{
									ItemType resultType = ItemType.getInstance(result);
									if(!inputs.contains(resultType))
									{
										discoveredInputs.add(resultType);
									}

									template.buildRecipe()
										.item(ingredientStack).item(inputStack)
										.item(brewingStand)
										.item(result)
										.addRecipe(generator);
								}
							}
						}
					}
				}
			}
			inputs.addAll(discoveredInputs);
			inputsToTry = discoveredInputs;
		} while(!inputsToTry.isEmpty() && iterations++ < MAX_FULL_ITERATIONS);

		IBrewingRecipe recipe = new VanillaBrewingRecipe();
		do
		{
			discoveredInputs = new TreeSet<>();

			for(ItemType ingredient: ingredients)
			{
				final ItemStack ingredientStack = ingredient.getDisplayStack();
				if(recipe.isIngredient(ingredientStack))
				{
					for(ItemType input: inputsToTry)
					{
						final ItemStack inputStack = input.getDisplayStack();
						if(recipe.isInput(inputStack))
						{
							ItemStack result = recipe.getOutput(inputStack, ingredientStack);
							if(result != null)
							{
								ItemType resultType = ItemType.getInstance(result);
								if(!inputs.contains(resultType))
								{
									discoveredInputs.add(resultType);
								}

								template.buildRecipe()
									.item(ingredientStack).item(inputStack)
									.item(brewingStand)
									.item(result)
									.addRecipe(generator);
							}
						}
					}
				}
			}
			inputs.addAll(discoveredInputs);
			inputsToTry = discoveredInputs;
		} while(!inputsToTry.isEmpty() && iterations++ < MAX_VANILLA_ONLY_ITERATIONS);

		generator.setDefaultTypeVisibility(brewingStand, false);
	}
}
