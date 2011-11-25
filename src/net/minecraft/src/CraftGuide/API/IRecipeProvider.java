package net.minecraft.src.CraftGuide.API;

import java.util.Collection;

import net.minecraft.src.CraftGuide.Recipe;

public interface IRecipeProvider
{
	void generateRecipes(IRecipeGenerator generator);
}
