package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import uristqwerty.CraftGuide.client.ui.text.TranslatedTextSource;

/**
 * Implements all functionality that relies on Forge. As this class
 *  is only instantiated if Forge is actually present, this allows
 *  the rest of CraftGuide to use other loaders (so far has included
 *  pure FML, ModLoader, LiteLoader, and even direct jar insertion).
 */
public class ForgeExtensionsImplementation extends ForgeExtensions
{
	private static final TranslatedTextSource emptyOreText =
			new TranslatedTextSource("craftguide.gui.empty_oredict_type");
	@Override
	public boolean matchesTypeImpl(IRecipe recipe)
	{
		return recipe instanceof ShapedOreRecipe || recipe instanceof ShapelessOreRecipe;
	}

	@Override
	public boolean isShapelessRecipeImpl(IRecipe recipe)
	{
		return recipe instanceof ShapelessOreRecipe;
	}

	@Override
	public Object[] getCraftingRecipeImpl(RecipeGeneratorImplementation gen, IRecipe recipe, boolean allowSmallGrid)
	{
		try
		{
			if(recipe instanceof ShapedOreRecipe)
			{
				int width = (Integer)CommonUtilities.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "width");
				int height = (Integer)CommonUtilities.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "height");
				Object[] items = (Object[])CommonUtilities.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe)recipe, "input");

				if(allowSmallGrid && width < 3 && height < 3)
				{
					return gen.getSmallShapedRecipe(width, height, items, ((ShapedOreRecipe)recipe).getRecipeOutput());
				}
				else
				{
					return gen.getCraftingShapedRecipe(width, height, items, ((ShapedOreRecipe)recipe).getRecipeOutput());
				}
			}
			else if(recipe instanceof ShapelessOreRecipe)
			{
				List<?> items = (List<?>)CommonUtilities.getPrivateValue(ShapelessOreRecipe.class, (ShapelessOreRecipe)recipe, "input");
				return gen.getCraftingShapelessRecipe(items, ((ShapelessOreRecipe)recipe).getRecipeOutput());
			}
		}
		catch(SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
		{
			CraftGuideLog.log(e);
		}

		return null;
	}

	private IdentityHashMap<List<?>, String> mappingCache = new IdentityHashMap<>();

	@Override
	public List<String> emptyOreDictEntryTextImpl(List<?> oreDictionaryList)
	{
		if(!mappingCache.containsKey(oreDictionaryList))
		{
			mappingCache.put(oreDictionaryList, getOreDictionaryNameImpl(oreDictionaryList));
		}

		String name = mappingCache.get(oreDictionaryList);

		if(name == null)
		{
			return null;
		}
		else
		{
			List<String> text = new ArrayList<>(1);
			text.add(emptyOreText.format(name));
			return text;
		}
	}

	private IdentityHashMap<List<?>, String> oreDictName = new IdentityHashMap<>();

	@Override
	public String getOreDictionaryNameImpl(List<?> list)
	{
		if(oreDictName.containsKey(list))
		{
			return oreDictName.get(list);
		}

		String name = getOreDictName(list);
		oreDictName.put(list, name);

		return name;
	}

	private String getOreDictName(List<?> list)
	{
		try
		{
			List<List<ItemStack>> listList = (List<List<ItemStack>>)CommonUtilities.getPrivateValue(OreDictionary.class, null, "idToStackUn");
			for(int i = 0; i < listList.size(); i++)
			{
				if(listList.get(i) == list)
				{
					return OreDictionary.getOreName(i);
				}
			}
		}
		catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			CraftGuideLog.log(e, "", true);
		}

		return null;
	}
}
