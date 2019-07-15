package uristqwerty.CraftGuide.recipes;

import net.minecraftforge.fml.common.Loader;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;

public class _AddRecipes
{
	public static void add()
	{
		alwaysAdd("uristqwerty.CraftGuide.recipes.DefaultRecipeProvider");
		alwaysAdd("uristqwerty.CraftGuide.recipes.BrewingRecipes");
		alwaysAdd("uristqwerty.CraftGuide.recipes.GrassSeedDrops");

		addIfModPresent("gregtech", "uristqwerty.CraftGuide.recipes.GregTechRecipes");
		addIfModPresent("extendedWorkbench", "uristqwerty.CraftGuide.recipes.ExtendedWorkbench");
		addIfModPresent("BuildCraft|Factory", "uristqwerty.CraftGuide.recipes.BuildCraftRecipes");
		addIfModPresent("IC2", "uristqwerty.CraftGuide.recipes.IC2ExperimentalRecipes");
		addIfModPresent("tconstruct", "uristqwerty.CraftGuide.recipes.TinkersConstruct");
		addIfModPresent("calculator", "uristqwerty.CraftGuide.recipes.Calculator");
		addIfModPresent("actuallyadditions", "uristqwerty.CraftGuide.recipes.ActuallyAdditions");
	}

	private static boolean addIfModPresent(String modID, String recipeClass)
	{
		if(Loader.isModLoaded(modID))
		{
			return alwaysAdd(recipeClass);
		}
		return false;
	}

	private static boolean alwaysAdd(String recipeClass)
	{
		try
		{
			Object instance = Class.forName(recipeClass).newInstance();
			CraftGuideAPIObject.registerCallbackObject(instance);
			return true;
		}
		catch(ClassNotFoundException e)
		{
			CraftGuideLog.log(e);
		}
		catch(Exception | LinkageError e)
		{
			CraftGuideLog.log(e, "", true);
		}
		return false;
	}
}
