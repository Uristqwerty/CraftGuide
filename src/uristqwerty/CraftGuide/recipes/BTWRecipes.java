package uristqwerty.CraftGuide.recipes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import uristqwerty.CraftGuide.CraftGuide;
import uristqwerty.CraftGuide.api.CraftGuideAPIObject;
import uristqwerty.CraftGuide.api.ExtraSlot;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeProvider;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.client.BWRData;

public class BTWRecipes extends CraftGuideAPIObject implements RecipeProvider
{
	private ItemStack crucible;
	private ItemStack hibachi;
	private ItemStack bellows;
	private ItemStack soulUrn;
	private ItemStack urn;
	private Class unfiredPotteryClass;
	private Class endStoneClass;
	private Block aestheticOpaque;
	private Item soap;
	private Item potash;
	private Item sawDust;
	private Item soulDust;
	private Item hellfireDust;
	private Item groundNetherrack;
	private Item concentratedHellfire;

	private Object[][][] extraMillstoneRecipes;
	private Object[][][] extraCauldronRecipes;
	private Object[][][] extraStokedCauldronRecipes;
	private Object[][][] extraCrucibleRecipes;
	private Object[][][] extraStokedCrucibleRecipes;

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		try
		{
			Class btw = Class.forName("mod_FCBetterThanWolves");
			aestheticOpaque = (Block)btw.getField("fcAestheticOpaque").get(null);
			crucible = new ItemStack((Block)btw.getField("fcCrucible").get(null));
			bellows = new ItemStack((Block)btw.getField("fcBellows").get(null));
			hibachi = new ItemStack((Block)btw.getField("fcBBQ").get(null));
			soulUrn = new ItemStack((Item)Class.forName("mod_FCBetterThanWolves").getField("fcSoulUrn").get(null));
			urn = new ItemStack((Item)btw.getField("fcUrn").get(null));
			unfiredPotteryClass = Class.forName("FCBlockUnfiredPottery");
			endStoneClass = Class.forName("FCBlockEndStone");

			soap = (Item)btw.getField("fcSoap").get(null);
			potash = (Item)btw.getField("fcPotash").get(null);
			sawDust = (Item)btw.getField("fcSawDust").get(null);
			soulDust = (Item)btw.getField("fcSoulDust").get(null);
			hellfireDust = (Item)btw.getField("fcHellfireDust").get(null);
			groundNetherrack = (Item)btw.getField("fcGroundNetherrack").get(null);
			concentratedHellfire = (Item)btw.getField("fcConcentratedHellfire").get(null);

			/* Setting these to null isn't really needed here, but it means that no
			 *  extra work is necessary in the future if/when the config settings can
			 *  be changed in-game. Would also work if BWR is automatically detected
			 *  on server join, so that if you switch between BWR and non-BWR servers
			 *  the recipe list can automatically change to be correct for whichever
			 *  you are currently in. */
			extraMillstoneRecipes = null;
			extraCauldronRecipes = null;
			extraStokedCauldronRecipes = null;
			extraCrucibleRecipes = null;
			extraStokedCrucibleRecipes = null;

			if(CraftGuide.insertBetterWithRenewablesRecipes || CraftGuide.betterWithRenewablesDetected)
			{
				if(BWRData.hasRecipes())
				{
					extraMillstoneRecipes = BWRData.getMillstoneRecipes();
					extraCauldronRecipes = BWRData.getCauldronRecipes();
					extraStokedCauldronRecipes = BWRData.getStokedCauldronRecipes();
					extraCrucibleRecipes = BWRData.getCrucibleRecipes();
					extraStokedCrucibleRecipes = BWRData.getStokedCrucibleRecipes();
				}
				else
				{
					addBWRExtraRecipes(btw);
				}
			}

			Object millstoneRecipes = Class.forName("FCCraftingManagerMillStone").getMethod("getInstance").invoke(null);
			ItemStack millstone = new ItemStack((Block)btw.getField("fcMillStone").get(null));
			addBulkRecipes(generator, millstoneRecipes, millstone, false, extraMillstoneRecipes);

			Object cauldronRecipes = Class.forName("FCCraftingManagerCauldron").getMethod("getInstance").invoke(null);
			ItemStack cauldron = new ItemStack((Block)btw.getField("fcCauldron").get(null));
			addBulkRecipes(generator, cauldronRecipes, cauldron, false, extraCauldronRecipes);

			Object cauldronStokedRecipes = Class.forName("FCCraftingManagerCauldronStoked").getMethod("getInstance").invoke(null);
			addBulkRecipes(generator, cauldronStokedRecipes, cauldron, true, extraStokedCauldronRecipes);

			Object crucibleRecipes = Class.forName("FCCraftingManagerCrucible").getMethod("getInstance").invoke(null);
			addBulkRecipes(generator, crucibleRecipes, crucible, false, extraCrucibleRecipes);

			Object crucibleStokedRecipes = Class.forName("FCCraftingManagerCrucibleStoked").getMethod("getInstance").invoke(null);
			addBulkRecipes(generator, crucibleStokedRecipes, crucible, true, extraStokedCrucibleRecipes);

			Object anvilRecipes = Class.forName("FCCraftingManagerAnvil").getMethod("getInstance").invoke(null);
			ItemStack anvil = new ItemStack((Block)btw.getField("fcAnvil").get(null));
			addAnvilRecipes(generator, anvilRecipes, anvil);

			ItemStack turntable = new ItemStack((Block)btw.getField("fcTurntable").get(null));
			addTurntableRecipes(generator, turntable);

			ItemStack kiln = new ItemStack((Block)btw.getField("fcKiln").get(null));
			addKilnRecipes(generator, kiln);

			ItemStack hopper = new ItemStack((Block)btw.getField("fcHopper").get(null));
			addHopperRecipes(generator, hopper);

			ItemStack saw = new ItemStack((Block)btw.getField("fcSaw").get(null));
			addSawRecipes(generator, saw);
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
	}

	private void addBulkRecipes(RecipeGenerator generator, Object manager, ItemStack block, boolean stoked, Object[][][] extraRecipes) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
	{
		Field recipes = Class.forName("FCCraftingManagerBulk").getDeclaredField("m_recipes");
		recipes.setAccessible(true);
		List recipeList = (List)recipes.get(manager);

		int inSize = 0, outSize = 0;

		for(Object recipe: recipeList)
		{
			List input = (List)Class.forName("FCCraftingManagerBulkRecipe").getMethod("getCraftingIngrediantList").invoke(recipe);
			List output = (List)Class.forName("FCCraftingManagerBulkRecipe").getMethod("getCraftingOutputList").invoke(recipe);

			inSize = Math.max(inSize, input.size());
			outSize = Math.max(outSize, output.size());
		}

		if(extraRecipes != null)
		{
			for(Object[][] recipe: extraRecipes)
			{
				if(recipe != null)
				{
					inSize = Math.max(inSize, recipe[0].length);
					outSize = Math.max(outSize, recipe[1].length);
				}
			}
		}

		int inColumns = (inSize + 2) / 3;
		int outColumns = (inSize + 2) / 3;

		Slot[] recipeSlots = new Slot[inColumns * 3 + outColumns * 3 + (stoked? 3 : 1)];

		for(int i = 0; i < inColumns; i++)
		{
			recipeSlots[i * 3 + 0] = new ItemSlot(i * 18 + 3,  3, 16, 16, true).drawOwnBackground();
			recipeSlots[i * 3 + 1] = new ItemSlot(i * 18 + 3, 21, 16, 16, true).drawOwnBackground();
			recipeSlots[i * 3 + 2] = new ItemSlot(i * 18 + 3, 39, 16, 16, true).drawOwnBackground();
		}

		for(int i = 0; i < outColumns; i++)
		{
			recipeSlots[inColumns * 3 + i * 3 + 0] = new ItemSlot(inColumns * 18 + i * 18 + 21,  3, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			recipeSlots[inColumns * 3 + i * 3 + 1] = new ItemSlot(inColumns * 18 + i * 18 + 21, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
			recipeSlots[inColumns * 3 + i * 3 + 2] = new ItemSlot(inColumns * 18 + i * 18 + 21, 39, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		}

		recipeSlots[inColumns * 3 + outColumns * 3] = new ExtraSlot(inColumns * 18 + 3, 21, 16, 16, block).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);

		if(stoked)
		{
			recipeSlots[inColumns * 3 + outColumns * 3 + 1] = new ExtraSlot(inColumns * 18 + 3, 39, 16, 16, hibachi).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
			recipeSlots[inColumns * 3 + outColumns * 3 + 2] = new ExtraSlot(inColumns * 18 + 3,  3, 16, 16, bellows).clickable().showName().setSlotType(SlotType.MACHINE_SLOT);
		}

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, block);
		template.setSize(inColumns * 18 + outColumns * 18 + 22, 58);

		for(Object recipe: recipeList)
		{
			List input = (List)Class.forName("FCCraftingManagerBulkRecipe").getMethod("getCraftingIngrediantList").invoke(recipe);
			List output = (List)Class.forName("FCCraftingManagerBulkRecipe").getMethod("getCraftingOutputList").invoke(recipe);

			Object[] recipeContents = new Object[inColumns * 3 + outColumns * 3 + (stoked? 3 : 1)];

			for(int i = 0; i < Math.min(inColumns * 3, input.size()); i++)
			{
				recipeContents[i] = input.get(i);
			}

			for(int i = 0; i < Math.min(outColumns * 3, output.size()); i++)
			{
				recipeContents[inColumns * 3 + i] = output.get(i);
			}

			recipeContents[inColumns * 3 + outColumns * 3] = block;

			if(stoked)
			{
				recipeContents[inColumns * 3 + outColumns * 3 + 1] = hibachi;
				recipeContents[inColumns * 3 + outColumns * 3 + 2] = bellows;
			}

			generator.addRecipe(template, recipeContents);
		}

		if(extraRecipes != null)
		{
			for(Object[][] recipe: extraRecipes)
			{
				if(recipe != null)
				{
					Object[] recipeContents = new Object[inColumns * 3 + outColumns * 3 + (stoked? 3 : 1)];

					for(int i = 0; i < Math.min(inColumns * 3, recipe[0].length); i++)
					{
						recipeContents[i] = recipe[0][i];
					}

					for(int i = 0; i < Math.min(outColumns * 3, recipe[1].length); i++)
					{
						recipeContents[inColumns * 3 + i] = recipe[1][i];
					}

					recipeContents[inColumns * 3 + outColumns * 3] = block;

					if(stoked)
					{
						recipeContents[inColumns * 3 + outColumns * 3 + 1] = hibachi;
						recipeContents[inColumns * 3 + outColumns * 3 + 2] = bellows;
					}

					generator.addRecipe(template, recipeContents);
				}
			}
		}
	}

	private void addAnvilRecipes(RecipeGenerator generator, Object manager, ItemStack anvil) throws SecurityException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		Field recipes = Class.forName("FCCraftingManagerAnvil").getDeclaredField("recipes");
		recipes.setAccessible(true);
		List recipeList = (List)recipes.get(manager);

		Slot[] recipeSlots = new Slot[16 + 2];

		for(int y = 0; y < 4; y++)
		{
			for(int x = 0; x < 4; x++)
			{
				recipeSlots[y * 4 + x] = new ItemSlot(x * 18 + 3, y * 18 + 3, 16, 16, true).drawOwnBackground();
			}
		}

		recipeSlots[16] = new ItemSlot(75, 21, 16, 16, true).drawOwnBackground().setSlotType(SlotType.OUTPUT_SLOT);
		recipeSlots[17] = new ExtraSlot(75, 39, 16, 16, anvil).clickable().showName().setSlotType(SlotType.OUTPUT_SLOT);
		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, anvil);
		template.setSize(96, 76);

		for(Object recipe: recipeList)
		{
			if(recipe instanceof ShapelessRecipes)
			{
				addShapelessAnvilRecipe(generator, template, (ShapelessRecipes)recipe, anvil);
			}
			else if(recipe instanceof ShapedRecipes)
			{
				addShapedAnvilRecipe(generator, template, (ShapedRecipes)recipe, anvil);
			}
		}
	}

	private void addShapelessAnvilRecipe(RecipeGenerator generator, RecipeTemplate template, ShapelessRecipes recipe, ItemStack anvil) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		List inputItems = (List)getPrivateValue(ShapelessRecipes.class, recipe, "b", "recipeItems");
		Object[] recipeContents = new Object[18];

		for(int i = 0; i < Math.min(inputItems.size(), 16); i++)
		{
			recipeContents[i] = inputItems.get(i);
		}

		recipeContents[16] = recipe.getRecipeOutput();
		recipeContents[17] = anvil;

		generator.addRecipe(template, recipeContents);
	}

	private void addShapedAnvilRecipe(RecipeGenerator generator, RecipeTemplate template, ShapedRecipes recipe, ItemStack anvil) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		int width = (Integer)getPrivateValue(ShapedRecipes.class, recipe, "b", "recipeWidth");
		int height = (Integer)getPrivateValue(ShapedRecipes.class, recipe, "c", "recipeHeight");
		Object[] items = (Object[])getPrivateValue(ShapedRecipes.class, recipe, "d", "recipeItems");
		Object[] recipeContents = new Object[18];

		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				recipeContents[y * 4 + x] = items[y * width + x];
			}
		}

		recipeContents[16] = recipe.getRecipeOutput();
		recipeContents[17] = anvil;

		generator.addRecipe(template, recipeContents);
	}

	private void addTurntableRecipes(RecipeGenerator generator, ItemStack turntable) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{
		Slot[] recipeSlots = new Slot[] {
			new ItemSlot(12, 21, 16, 16).drawOwnBackground(),
			new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
			new ItemSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
			new ExtraSlot(31, 21, 16, 16, turntable).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, turntable);
		Block unfiredPottery = (Block)Class.forName("mod_FCBetterThanWolves").getField("fcUnfiredPottery").get(null);
		ItemStack clay = new ItemStack(Item.clay);

		addTurntableRecipe(generator, template, turntable, new ItemStack(Block.blockClay), new ItemStack(unfiredPottery, 1, 0), clay);
		addTurntableRecipe(generator, template, turntable, unfiredPottery, 0, 1, null);
		addTurntableRecipe(generator, template, turntable, unfiredPottery, 1, 2, clay);
		addTurntableRecipe(generator, template, turntable, unfiredPottery, 2, 3, clay);
		addTurntableRecipe(generator, template, turntable, unfiredPottery, 3, 4, null);
		addTurntableRecipe(generator, template, turntable, new ItemStack(unfiredPottery, 1, 4), null, clay);
	}

	private void addTurntableRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack turntable, Block block, int dataIn, int dataOut, ItemStack drop)
	{
		addTurntableRecipe(generator, template, turntable, new ItemStack(block, 1, dataIn), new ItemStack(block, 1, dataOut), drop);
	}

	private void addTurntableRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack turntable, ItemStack input, ItemStack output, ItemStack drop)
	{
		Object[] recipe = new Object[4];

		recipe[0] = input;
		recipe[1] = output;
		recipe[2] = drop;
		recipe[3] = turntable;

		generator.addRecipe(template, recipe);
	}

	private void addKilnRecipes(RecipeGenerator generator, ItemStack kiln) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{
		Slot[] recipeSlots = new Slot[] {
			new ItemSlot(12, 21, 16, 16).drawOwnBackground(),
			new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
			new ItemSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
			new ExtraSlot(31, 21, 16, 16, kiln).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
			new ExtraSlot(31, 39, 16, 16, hibachi).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
			new ExtraSlot(31,  3, 16, 16, bellows).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
		};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, kiln);

		for(int i = 0; i < Block.blocksList.length; i++)
		{
			Block block = Block.blocksList[i];

			if(block != null && (Boolean)getPrivateValue(Block.class, block, "m_bCanBeCookedByKiln"))
			{
				addKilnRecipe(generator, template, kiln, block);
			}
		}
	}

	private void addKilnRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack kiln, Block block) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{
		if(unfiredPotteryClass.isAssignableFrom(block.getClass()))
		{
			ItemStack planter = new ItemStack((Block)Class.forName("mod_FCBetterThanWolves").getField("fcPlanter").get(null));
			ItemStack vase = new ItemStack((Block)Class.forName("mod_FCBetterThanWolves").getField("fcVase").get(null));
			ItemStack mould = new ItemStack((Item)Class.forName("mod_FCBetterThanWolves").getField("fcItemMould").get(null));

			addKilnRecipe(generator, template, kiln, new ItemStack(block, 1, 0), crucible);
			addKilnRecipe(generator, template, kiln, new ItemStack(block, 1, 1), planter);
			addKilnRecipe(generator, template, kiln, new ItemStack(block, 1, 2), vase);
			addKilnRecipe(generator, template, kiln, new ItemStack(block, 1, 3), urn);
			addKilnRecipe(generator, template, kiln, new ItemStack(block, 1, 4), mould);
		}
		else
		{
			ItemStack input = new ItemStack(block);

			if(endStoneClass.isAssignableFrom(block.getClass()))
			{
				ItemStack output1 = new ItemStack((Item)Class.forName("mod_FCBetterThanWolves").getField("fcItemBrimstone").get(null));
				ItemStack output2 = new ItemStack(aestheticOpaque, 1, 10);
				addKilnRecipe(generator, template, kiln, input, output1, output2);
			}
			else
			{
				int item = (Integer)getPrivateValue(Block.class, block, "m_iItemIndexDroppedWhenCookedByKiln");
				int damage = (Integer)getPrivateValue(Block.class, block, "m_iItemDamageDroppedWhenCookedByKiln");
				addKilnRecipe(generator, template, kiln, input, new ItemStack(item, 1, damage));
			}
		}
	}

	private void addKilnRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack kiln, ItemStack input, ItemStack output)
	{
		addKilnRecipe(generator, template, kiln, input, output, null);
	}

	private void addKilnRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack kiln, ItemStack input, ItemStack output1, ItemStack output2)
	{
		Object[] recipeContents = new Object[] {
				input, output1, output2,
				kiln, hibachi, bellows,
		};

		generator.addRecipe(template, recipeContents);
	}

	private void addHopperRecipes(RecipeGenerator generator, ItemStack hopper) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{
		Slot[] recipeSlots = new Slot[] {
				new ItemSlot(12, 12, 16, 16, true).drawOwnBackground(),
				new ItemSlot(12, 30, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ItemSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ItemSlot(31, 12, 16, 16, true).setSlotType(SlotType.MACHINE_SLOT),
				new ExtraSlot(31, 30, 16, 16, hopper).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
			};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, hopper);
		ItemStack groundNetherrackStack = new ItemStack(groundNetherrack, 8);
		ItemStack hellfireDustStack = new ItemStack(hellfireDust, 8);
		ItemStack wicker = new ItemStack((Item)Class.forName("mod_FCBetterThanWolves").getField("fcWicker").get(null));
		ItemStack slowsand = new ItemStack(Block.slowSand);
		ItemStack soulDustStack = new ItemStack(soulDust, 8);
		ItemStack sawDustStack = new ItemStack(sawDust, 8);

		addHopperRecipe(generator, template, hopper, wicker, new ItemStack(Block.gravel), null, new ItemStack(Block.sand), new ItemStack(Item.flint));
		addHopperRecipe(generator, template, hopper, slowsand, groundNetherrackStack, urn, hellfireDustStack, soulUrn);
		addHopperRecipe(generator, template, hopper, slowsand, soulDustStack, urn, sawDustStack, soulUrn);
	}

	private void addHopperRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack hopper, ItemStack filter, ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2)
	{
		Object[] recipeContents = new Object[] {
				input1, input2, output1, output2,filter, hopper,
		};

		generator.addRecipe(template, recipeContents);
	}

	private void addSawRecipes(RecipeGenerator generator, ItemStack saw) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{
		Slot[] recipeSlots = new Slot[] {
				new ItemSlot(12, 21, 16, 16, true).drawOwnBackground(),
				new ItemSlot(50, 12, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ItemSlot(50, 30, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground(),
				new ExtraSlot(31, 21, 16, 16, saw).clickable().showName().setSlotType(SlotType.MACHINE_SLOT),
			};

		RecipeTemplate template = generator.createRecipeTemplate(recipeSlots, saw);

		ItemStack bloodWood = new ItemStack((Block)Class.forName("mod_FCBetterThanWolves").getField("fcBloodWood").get(null));
		ItemStack gears = new ItemStack((Item)Class.forName("mod_FCBetterThanWolves").getField("fcGear").get(null));
		ItemStack soulDustStack = new ItemStack(soulDust, 2);
		ItemStack sawDustStack = new ItemStack(sawDust, 2);
		gears.stackSize = 2;

		Block aestheticNonOpaque = (Block)Class.forName("mod_FCBetterThanWolves").getField("fcAestheticNonOpaque").get(null);
		Block companionCube = (Block)Class.forName("mod_FCBetterThanWolves").getField("fcCompanionCube").get(null);

		int mouldingID = (Integer)Class.forName("mod_FCBetterThanWolves").getField("fcBlockWoodMouldingItemStubID").get(null);
		int sidingID = (Integer)Class.forName("mod_FCBetterThanWolves").getField("fcBlockWoodSidingItemStubID").get(null);
		int cornerID = (Integer)Class.forName("mod_FCBetterThanWolves").getField("fcBlockWoodCornerItemStubID").get(null);

		addSawRecipe(generator, template, saw, bloodWood, new ItemStack(Block.planks, 4, 3), soulDustStack);

		for(int i = 0; i < 4; i++)
		{
			addSawRecipe(generator, template, saw, Block.wood, Block.planks.blockID, i, 4, sawDustStack);
			addSawRecipe(generator, template, saw, Block.planks, sidingID, i, 2, null);
			addSawRecipe(generator, template, saw, sidingID, mouldingID, i, 2, null);
			addSawRecipe(generator, template, saw, mouldingID, cornerID, i, 2, null);
			addSawRecipe(generator, template, saw, new ItemStack(cornerID, 1, i), gears, null);
			addSawRecipe(generator, template, saw, Block.woodDoubleSlab, Block.woodSingleSlab.blockID, i, 2, null);
			addSawRecipe(generator, template, saw, Block.woodSingleSlab, mouldingID, i, 2, null);
		}

		addSawRecipe(generator, template, saw, Block.stairCompactPlanks, sidingID, mouldingID, 0);
		addSawRecipe(generator, template, saw, Block.stairsWoodSpruce, sidingID, mouldingID, 1);
		addSawRecipe(generator, template, saw, Block.stairsWoodBirch, sidingID, mouldingID, 2);
		addSawRecipe(generator, template, saw, Block.stairsWoodJungle, sidingID, mouldingID, 3);
		addSawRecipe(generator, template, saw, Block.fence, cornerID, 0, 2, null);
		addSawRecipe(generator, template, saw, new ItemStack(aestheticOpaque, 1, 0), new ItemStack(aestheticNonOpaque, 2, 5), null);
		addSawRecipe(generator, template, saw, new ItemStack(companionCube, 1, 0), new ItemStack(companionCube, 2, 1), null);
	}

	private void addSawRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack saw, Block inputBlock, int outputID, int damage, int outputSize, ItemStack dust)
	{
		addSawRecipe(generator, template, saw,inputBlock.blockID, outputID, damage, outputSize, dust);
	}

	private void addSawRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack saw, int inputID, int outputID, int damage, int outputSize, ItemStack dust)
	{
		addSawRecipe(generator, template, saw, new ItemStack(inputID, 1, damage), new ItemStack(outputID, outputSize, damage), dust);
	}

	private void addSawRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack saw, Block inputBlock, int outputID1, int outputID2, int outputDamage)
	{
		addSawRecipe(generator, template, saw, new ItemStack(inputBlock.blockID, 1, 0), new ItemStack(outputID1, 1, outputDamage), new ItemStack(outputID2, 1, outputDamage));
	}

	private void addSawRecipe(RecipeGenerator generator, RecipeTemplate template, ItemStack saw, ItemStack input, ItemStack output1, ItemStack output2)
	{
		Object[] recipeContents = new Object[] {
				input, output1, output2, saw,
		};

		generator.addRecipe(template, recipeContents);
	}

	private <T> Object getPrivateValue(Class<? extends T> objectClass, T object, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		return getPrivateValue(objectClass, object, name, name);
	}

	private <T> Object getPrivateValue(Class<? extends T> objectClass, T object, String obfuscatedName, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field;
		try
		{
			field = objectClass.getDeclaredField(obfuscatedName);
		}
		catch(NoSuchFieldException e)
		{
			field = objectClass.getDeclaredField(name);
		}

		field.setAccessible(true);
		return field.get(object);
	}

	private void addBWRExtraRecipes(Class btw) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{
		ItemStack dungBlock = new ItemStack(aestheticOpaque, 1, 1);
		Item dung = (Item)btw.getField("fcDung").get(null);
		Item scouredLeather = (Item)btw.getField("fcScouredLeather").get(null);
		Item tannedLeather = (Item)btw.getField("fcTannedLeather").get(null);

		extraMillstoneRecipes = new Object[][][]{
				addGoldGrindingRecipe(Item.plateGold, 8),
				addGoldGrindingRecipe(Item.legsGold, 7),
				addGoldGrindingRecipe(Item.helmetGold, 5),
				addGoldGrindingRecipe(Item.bootsGold, 4),
				addGoldGrindingRecipe(Item.axeGold, 3),
				addGoldGrindingRecipe(Item.pickaxeGold, 3),
				addGoldGrindingRecipe(Item.swordGold, 2),
				addGoldGrindingRecipe(Item.hoeGold, 2),
				addGoldGrindingRecipe(Item.shovelGold, 1),

				{{new ItemStack(concentratedHellfire, 9, -1), new ItemStack(Item.ingotGold, 3, -1)}, {new ItemStack(Item.redstone, 63)}},
				{{new ItemStack(concentratedHellfire, 1, -1), new ItemStack(Item.goldNugget, 3, -1)}, {new ItemStack(Item.redstone, 7)}},
				{{new ItemStack(groundNetherrack), new ItemStack(sawDust)}, {new ItemStack(hellfireDust), new ItemStack(soulDust)}},
				{{new ItemStack(groundNetherrack)}, {new ItemStack(hellfireDust)}},
		};

		extraStokedCauldronRecipes = new Object[][][]{
				addDiamondRecoveryRecipe(Item.plateDiamond, 8),
				addDiamondRecoveryRecipe(Item.legsDiamond, 7),
				addDiamondRecoveryRecipe(Item.helmetDiamond, 5),
				addDiamondRecoveryRecipe(Item.bootsDiamond, 4),
				addDiamondRecoveryRecipe(Item.axeDiamond, 3),
				addDiamondRecoveryRecipe(Item.pickaxeDiamond, 3),
				addDiamondRecoveryRecipe(Item.swordDiamond, 2),
				addDiamondRecoveryRecipe(Item.hoeDiamond, 2),
				addDiamondRecoveryRecipe(Item.shovelDiamond, 1),

				addLapisRecoveryRecipeA(11, 0, 0, 1),
				addLapisRecoveryRecipeB(11, 0, 0, 1),
				addLapisRecoveryRecipeA(3, 0, 0, 2),
				addLapisRecoveryRecipeB(3, 0, 0, 2),
				addLapisRecoveryRecipeA(9, 13, 13, 2),
				addLapisRecoveryRecipeB(9, 13, 13, 2),
				addLapisRecoveryRecipeA(10, 14, 14, 2),
				addLapisRecoveryRecipeB(10, 14, 14, 2),
				addLapisRecoveryRecipeA(2, 6, 14, 4),
				addLapisRecoveryRecipeB(2, 6, 14, 4),

				{{new ItemStack(Block.sapling, 1, 0)}, {new ItemStack(Block.deadBush)}},
		};

		extraCauldronRecipes = new Object[][][]{
				{{new ItemStack(Block.cobblestone), new ItemStack(Item.netherStalkSeeds), new ItemStack(soulDust)}, {new ItemStack(Block.netherrack), new ItemStack(sawDust)}},
				{{new ItemStack(Block.cobblestone, 8), new ItemStack(Item.netherStalkSeeds, 8), soulUrn}, {new ItemStack(Block.netherrack, 8)}},
				{{new ItemStack(dung, 9)}, {dungBlock}},
				{{new ItemStack(scouredLeather, 1, -1), dungBlock}, {new ItemStack(tannedLeather, 1, 0), new ItemStack(dung, 8, 0)}},
				{{new ItemStack(Item.silk, 3, -1), new ItemStack(Item.slimeBall, 1, -1)}, {new ItemStack(Block.web)}},
		};
	}

	private Object[][] addGoldGrindingRecipe(Item item, int number)
	{
		return new Object[][]{{new ItemStack(item)}, {new ItemStack(Item.goldNugget, number * 3)}};
	}

	private Object[][] addDiamondRecoveryRecipe(Item item, int number)
	{
		return new Object[][]{
			{new ItemStack(item), new ItemStack(potash, number * 8), new ItemStack(concentratedHellfire, number * 1)},
			{new ItemStack(Item.diamond, number)}};
	}

	private Object[][] addLapisRecoveryRecipeA(int inColor, int outColor1, int outColor2, int quantity)
	{
		if(outColor1 == outColor2)
		{
			return new Object[][]{
					{
						new ItemStack(Block.cloth, quantity * 8, inColor),
						new ItemStack(soap, quantity * 2, -1),
						new ItemStack(Item.clay, 1, -1)
					},
					{
						new ItemStack(Item.dyePowder, 1, 4),
						new ItemStack(Block.cloth, quantity * 8, outColor1),
					}
			};
		}
		else
		{
			return new Object[][]{
					{
						new ItemStack(Block.cloth, quantity * 8, inColor),
						new ItemStack(soap, quantity * 2, -1),
						new ItemStack(Item.clay, 1, -1)
					},
					{
						new ItemStack(Item.dyePowder, 1, 4),
						new ItemStack(Block.cloth, quantity * 4, outColor1),
						new ItemStack(Block.cloth, quantity * 4, outColor2),
					}
			};
		}
	}

	private Object[][] addLapisRecoveryRecipeB(int inColor, int outColor1, int outColor2, int quantity)
	{
		if(outColor1 == outColor2)
		{
			return new Object[][]{
					{
						new ItemStack(Block.cloth, quantity * 8, inColor),
						new ItemStack(soap, quantity * 2, -1),
					},
					{
						new ItemStack(Block.cloth, quantity * 8, outColor1),
					}
			};
		}
		else
		{
			return new Object[][]{
					{
						new ItemStack(Block.cloth, quantity * 8, inColor),
						new ItemStack(soap, quantity * 2, -1),
					},
					{
						new ItemStack(Block.cloth, quantity * 4, outColor1),
						new ItemStack(Block.cloth, quantity * 4, outColor2),
					}
			};
		}
	}
}
