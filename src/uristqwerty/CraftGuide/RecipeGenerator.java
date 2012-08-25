package uristqwerty.CraftGuide;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeGenerator;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeTemplate;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.minecraft.Image;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;

public class RecipeGenerator implements IRecipeGenerator
{
	private Map<ItemStack, List<ICraftGuideRecipe>> recipes = new HashMap<ItemStack, List<ICraftGuideRecipe>>();
	public List<ItemStack> disabledTypes = new LinkedList<ItemStack>();
	private static ItemStack workbench = new ItemStack(Block.workbench);

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
			String backgroundTexture, int backgroundX, int backgroundY,
			int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate(slots,
				backgroundTexture, backgroundX, backgroundY,
				backgroundTexture, backgroundSelectedX, backgroundSelectedY);
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
			ItemStack craftingType, String backgroundTexture, int backgroundX,
			int backgroundY, int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate(slots, craftingType,
				backgroundTexture, backgroundX, backgroundY,
				backgroundTexture, backgroundSelectedX, backgroundSelectedY);
	}
	
	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
		String backgroundTexture, int backgroundX, int backgroundY,
		String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate(slots, workbench,
				backgroundTexture, backgroundX, backgroundY,
				backgroundSelectedTexture, backgroundSelectedX, backgroundSelectedY);
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots, ItemStack craftingType,
			String backgroundTexture, int backgroundX, int backgroundY,
			String backgroundSelectedTexture, int backgroundSelectedX, int backgroundSelectedY)
	{
		return new RecipeTemplate(
				slots,
				craftingType,
				new TexturedRect(0, 0, 79, 58, 
					Image.getImage(backgroundTexture),
					backgroundX, backgroundY),
				new TexturedRect(0, 0, 79, 58, 
					Image.getImage(backgroundSelectedTexture),
					backgroundSelectedX, backgroundSelectedY));
	}

	@Override
	public void addRecipe(IRecipeTemplate template, Object[] items)
	{
		RecipeTemplate recipeTemplate = (RecipeTemplate)template;

		List<ICraftGuideRecipe> recipeList = recipes.get(recipeTemplate.craftingType);
		
		if(recipeList == null)
		{
			recipeList = new LinkedList<ICraftGuideRecipe>();
			recipes.put(recipeTemplate.craftingType, recipeList);
		}
		
		recipeList.add(recipeTemplate.generate(items));
	}

	public Map<ItemStack, List<ICraftGuideRecipe>> getRecipes()
	{
		return recipes;
	}
	
	public void clearRecipes()
	{
		recipes.clear();
	}

	@Override
	public void setDefaultTypeVisibility(ItemStack type, boolean visible)
	{
		if(visible)
		{
			disabledTypes.remove(type);
		}
		else if(!disabledTypes.contains(type))
		{
			disabledTypes.add(type);
		}
	}
}
