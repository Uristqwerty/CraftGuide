package net.minecraft.src.CraftGuide;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeProvider;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.RecipeGeneratorImplementation;
import uristqwerty.gui.minecraft.Image;
import uristqwerty.gui.texture.TextureClip;

public class OldAPITranslator implements IRecipeGenerator
{
	private RecipeGeneratorImplementation actualGenerator;
	private static ItemStack workbench = new ItemStack(Block.workbench);

	public OldAPITranslator(RecipeGeneratorImplementation generator)
	{
		actualGenerator = generator;
	}

	public static void generateRecipes(RecipeGeneratorImplementation generator)
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
					new TextureClip(
							Image.fromJar(backgroundTexture),
							backgroundX, backgroundY, 79, 58),
					new TextureClip(
							Image.fromJar(backgroundSelectedTexture),
							backgroundSelectedX, backgroundSelectedY, 79, 58));
	}

	@Override
	public void addRecipe(IRecipeTemplate template, ItemStack[] items)
	{
		actualGenerator.addRecipe(((FakeRecipeTemplate)template).getRealTemplate(), ((FakeRecipeTemplate)template).convertItemsList(items));
	}
}
