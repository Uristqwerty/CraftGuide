

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.CraftGuide.API.CraftGuideAPIObject;
import net.minecraft.src.CraftGuide.API.ExtraSlot;
import net.minecraft.src.CraftGuide.API.IRecipeGenerator;
import net.minecraft.src.CraftGuide.API.IRecipeProvider;
import net.minecraft.src.CraftGuide.API.ICraftGuideRecipe.ItemSlot;
import net.minecraft.src.CraftGuide.API.IRecipeTemplate;
import net.minecraft.src.CraftGuide.API.OutputSlot;

import eloraam.core.CraftLib;

public class RP2Recipes extends CraftGuideAPIObject implements IRecipeProvider
{
	@Override
	public void generateRecipes(IRecipeGenerator generator)
	{
		try
		{
			ItemStack alloyFurnace = new ItemStack(RedPowerBase.blockAppliance.blockID, 1, 0);
			ItemSlot[] craftingSlots = new ItemSlot[]{
				new ItemSlot( 3,  3, 16, 16, 0, true),
				new ItemSlot(21,  3, 16, 16, 1, true),
				new ItemSlot(39,  3, 16, 16, 2, true),
				new ItemSlot( 3, 21, 16, 16, 3, true),
				new ItemSlot(21, 21, 16, 16, 4, true),
				new ItemSlot(39, 21, 16, 16, 5, true),
				new ItemSlot( 3, 39, 16, 16, 6, true),
				new ItemSlot(21, 39, 16, 16, 7, true),
				new ItemSlot(39, 39, 16, 16, 8, true),
				new OutputSlot(59, 31, 16, 16, 9, true),
				new ExtraSlot(59, 11, 16, 16, 10, alloyFurnace),
			};
			
			IRecipeTemplate template = generator.createRecipeTemplate(craftingSlots, alloyFurnace,
				"gui/CraftGuideRecipe.png", 163, 1, 163, 61);
			
			List recipes = getRecipes();
			
			if(recipes != null)
			{
				for(Object o: recipes)
				{
					addRecipe(generator, template, (List)o);
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	private void addRecipe(IRecipeGenerator generator, IRecipeTemplate template, List o)
	{
		ItemStack[] input = (ItemStack[])o.get(0);
		ItemStack output  = (ItemStack)  o.get(1);
		
		ItemStack[] recipe = new ItemStack[11];

		for(int i = 0; i < 9; i++)
		{
			if(i < input.length)
			{
				recipe[i] = input[i];
			}
			else
			{
				recipe[i] = null;
			}
		}
		recipe[9] = output;
		recipe[10] = null;
		
		generator.addRecipe(template, recipe);
	}

	private List getRecipes()
	{
		try
		{
			Field alloyRecipes = CraftLib.class.getDeclaredField("alloyRecipes");
			alloyRecipes.setAccessible(true);
			return (List)alloyRecipes.get(null);
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
