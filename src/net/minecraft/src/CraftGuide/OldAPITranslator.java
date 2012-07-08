package net.minecraft.src.CraftGuide;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeProvider;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;
import uristqwerty.CraftGuide.RecipeGenerator;
import uristqwerty.CraftGuide.RecipeTemplate;
import uristqwerty.CraftGuide.ui.Rendering.GuiTexture;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;

public class OldAPITranslator implements IRecipeGenerator
{
	private RecipeGenerator actualGenerator;
	private static ItemStack workbench = new ItemStack(Block.workbench);
	
	public OldAPITranslator(RecipeGenerator generator)
	{
		actualGenerator = generator;
	}

	public static void generateRecipes(RecipeGenerator generator)
	{
		new OldAPITranslator(generator).generate();
	}

	private void generate()
	{
		for(Object object: ReflectionAPI.APIObjects)
		{
			if(object instanceof IRecipeProvider)
			{
				((IRecipeProvider)object).generateRecipes(this);
			}
		}
	}

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
			return new FakeRecipeTemplate(
					slots,
					craftingType,
					new TexturedRect(0, 0, 79, 58, 
						GuiTexture.getInstance(backgroundTexture),
						backgroundX, backgroundY),
					new TexturedRect(0, 0, 79, 58, 
						GuiTexture.getInstance(backgroundSelectedTexture),
						backgroundSelectedX, backgroundSelectedY));
	}

	@Override
	public void addRecipe(IRecipeTemplate template, ItemStack[] items)
	{
		actualGenerator.addRecipe(((FakeRecipeTemplate)template).getRealTemplate(), items);
	}
}
