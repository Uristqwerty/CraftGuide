package net.minecraft.src.CraftGuide;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeProvider;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.RecipeGenerator;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import uristqwerty.gui.minecraft.Image;

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
		CraftGuideLog.log("  Getting recipes from old API...");
		for(Object object: ReflectionAPI.APIObjects)
		{
			if(object instanceof IRecipeProvider)
			{
				CraftGuideLog.log("    Generating recipes from " + object.getClass().getName());
				try
				{
					((IRecipeProvider)object).generateRecipes(this);
				}
				catch(Exception e)
				{
					CraftGuideLog.log(e);
				}
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
						Image.getImage(backgroundTexture),
						backgroundX, backgroundY),
					new TexturedRect(0, 0, 79, 58, 
						Image.getImage(backgroundSelectedTexture),
						backgroundSelectedX, backgroundSelectedY));
	}

	@Override
	public void addRecipe(IRecipeTemplate template, ItemStack[] items)
	{
		actualGenerator.addRecipe(((FakeRecipeTemplate)template).getRealTemplate(), items);
	}
}
