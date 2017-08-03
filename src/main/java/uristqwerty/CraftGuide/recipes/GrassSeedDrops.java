package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandom.Item;
import net.minecraftforge.common.ForgeHooks;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.api.ChanceSlot;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;

public class GrassSeedDrops extends CraftGuideAPIObject implements RecipeProvider
{
	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			/* Why? Why is WeightedRandom.Item completely public, but ForgeHooks.SeedEntry not?
			 * Forge, of all packages, should be designed with the idea that mods may need
			 * access to odd places.
			 */
			Field seedList = ForgeHooks.class.getDeclaredField("seedList");
			seedList.setAccessible(true);
			List<? extends WeightedRandom.Item> entries = (List<? extends Item>) seedList.get(null);
			Field seedField = Class.forName("net.minecraftforge.common.ForgeHooks$SeedEntry").getDeclaredField("seed");
			seedField.setAccessible(true);

			ItemStack grass = new ItemStack(Blocks.tallgrass, 1, 1);

			double total = 0;
			for(WeightedRandom.Item entry: entries)
			{
				total += entry.itemWeight;
			}

			total *= 8;

			Slot[] slots = new Slot[] {
					new ExtraSlot(18, 21, 16, 16, grass).clickable().showName().setSlotType(SlotType.INPUT_SLOT),
					new ChanceSlot(44, 21, 16, 16, true).setFormatString(" (%1$.6f%% chance)").setRatio(100000000).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
			};

			RecipeTemplate template = generator.createRecipeTemplate(slots, grass);

			for(WeightedRandom.Item entry: entries)
			{
				Object[] recipeContents = new Object[]{
						grass,
						new Object[]{
								seedField.get(entry),
								(int)(entry.itemWeight/total * 100000000),
						},
				};

				generator.addRecipe(template, recipeContents);
			}
		}
		catch(SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException e)
		{
			CraftGuideLog.log(e);
		}
	}
}
