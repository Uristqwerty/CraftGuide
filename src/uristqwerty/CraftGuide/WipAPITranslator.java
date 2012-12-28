package uristqwerty.CraftGuide;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ExtraSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeGenerator;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeProvider;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeTemplate;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ISlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.OutputSlot;
import uristqwerty.CraftGuide.api.CraftGuideRecipe;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.gui.minecraft.Image;
import uristqwerty.gui.texture.BlankTexture;
import uristqwerty.gui.texture.BorderedTexture;
import uristqwerty.gui.texture.DynamicTexture;
import uristqwerty.gui.texture.SubTexture;
import uristqwerty.gui.texture.Texture;
import uristqwerty.gui.texture.TextureClip;


/**
 * Load recipes from mods that interact with the WIP API, and convert them to the
 * current one.
 */
@Deprecated
public class WipAPITranslator implements IRecipeGenerator
{
	private Texture defaultBackground = new BlankTexture();
	private Texture defaultBackgroundSelected;
	private RecipeGeneratorImplementation generator;
	private static ItemStack workbench = new ItemStack(Block.workbench);

	private static class WIPAPIRecipeTemplate implements IRecipeTemplate
	{
		RecipeTemplate realTemplate;

		public WIPAPIRecipeTemplate(ISlot[] slots, ItemStack craftingType,
				Texture defaultBackground, Texture defaultBackgroundSelected)
		{
			realTemplate = new DefaultRecipeTemplate(convertSlots(slots), craftingType, defaultBackground, defaultBackgroundSelected);
		}

		private static Slot[] convertSlots(ISlot[] oldSlots)
		{
			Slot[] newSlots = new Slot[oldSlots.length];

			for(int i = 0; i < oldSlots.length; i++)
			{
				ISlot slot = oldSlots[i];

				if(slot instanceof OutputSlot)
				{
					OutputSlot os = (OutputSlot)slot;
					newSlots[i] = new uristqwerty.CraftGuide.api.ItemSlot(
							os.x, os.y, os.width, os.height, os.drawQuantity)
							.setSlotType(SlotType.OUTPUT_SLOT);
				}
				else if(slot instanceof ExtraSlot)
				{
					ExtraSlot es = (ExtraSlot)slot;
					newSlots[i] = new uristqwerty.CraftGuide.api.ExtraSlot(
							es.x, es.y, es.width, es.height, es.displayed)
							.clickable(es.canClick)
							.showName(es.showName);
				}
				else if(slot instanceof ItemSlot)
				{
					ItemSlot is = (ItemSlot)slot;
					newSlots[i] = new uristqwerty.CraftGuide.api.ItemSlot(
							is.x, is.y, is.width, is.height, is.drawQuantity);
				}
			}

			return newSlots;
		}

		@Override
		public IRecipeTemplate setSize(int width, int height)
		{
			realTemplate.setSize(width, height);
			return this;
		}

		@Override
		public ItemStack getCraftingType()
		{
			return realTemplate.getCraftingType();
		}

		@Override
		public ICraftGuideRecipe generate(Object[] items)
		{
			return null;
		}

		public CraftGuideRecipe generateActualRecipe(Object[] crafting)
		{
			return realTemplate.generate(crafting);
		}

	}

	public WipAPITranslator(RecipeGeneratorImplementation generator)
	{
		Texture source = DynamicTexture.instance("base_image");
		defaultBackgroundSelected = new BorderedTexture(
				new Texture[]{
						new TextureClip(source, 117,  1,  2, 2),
						new SubTexture (source, 120,  1, 32, 2),
						new TextureClip(source, 153,  1,  2, 2),
						new SubTexture (source, 117,  4,  2, 32),
						new SubTexture (source, 120,  4, 32, 32),
						new SubTexture (source, 153,  4,  2, 32),
						new TextureClip(source, 117, 37,  2, 2),
						new SubTexture (source, 120, 37, 32, 2),
						new TextureClip(source, 153, 37,  2, 2),
				}, 2);

		this.generator = generator;
	}

	public static void generateRecipes(RecipeGeneratorImplementation generator)
	{
		WipAPITranslator instance = new WipAPITranslator(generator);

		CraftGuideLog.log("  Getting recipes from WIP API...");
		for(Object object: ReflectionAPI.APIObjects)
		{
			if(object instanceof IRecipeProvider)
			{
				CraftGuideLog.log("    Generating recipes from " + object.getClass().getName());
				try
				{
					((IRecipeProvider)object).generateRecipes(instance);
				}
				catch(Exception e)
				{
					CraftGuideLog.log(e);
				}
			}
		}
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ISlot[] slots, ItemStack craftingType)
	{
		if(craftingType == null)
		{
			craftingType = workbench;
		}

		return new WIPAPIRecipeTemplate(slots, craftingType, defaultBackground, defaultBackgroundSelected);
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ISlot[] slots,
			ItemStack craftingType, String backgroundTexture, int backgroundX,
			int backgroundY, int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate(slots, craftingType,
				backgroundTexture, backgroundX, backgroundY,
				backgroundTexture, backgroundSelectedX, backgroundSelectedY);
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ISlot[] slots,
			ItemStack craftingType, String backgroundTexture, int backgroundX,
			int backgroundY, String backgroundSelectedTexture,
			int backgroundSelectedX, int backgroundSelectedY)
	{
		return new WIPAPIRecipeTemplate(
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
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
			ItemStack craftingType, String backgroundTexture, int backgroundX,
			int backgroundY, int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate((ISlot[])slots, craftingType,
				backgroundTexture, backgroundX, backgroundY,
				backgroundTexture, backgroundSelectedX, backgroundSelectedY);
	}

	@Override
	public IRecipeTemplate createRecipeTemplate(ItemSlot[] slots,
			ItemStack craftingType, String backgroundTexture, int backgroundX,
			int backgroundY, String backgroundSelectedTexture,
			int backgroundSelectedX, int backgroundSelectedY)
	{
		return createRecipeTemplate((ISlot[])slots, craftingType,
				backgroundTexture, backgroundX, backgroundY,
				backgroundSelectedTexture, backgroundSelectedX, backgroundSelectedY);
	}

	@Override
	public void addRecipe(IRecipeTemplate template, Object[] crafting)
	{
		if(template instanceof WIPAPIRecipeTemplate)
		{
			WIPAPIRecipeTemplate temp = (WIPAPIRecipeTemplate)template;
			generator.addRecipe(temp.generateActualRecipe(crafting), temp.getCraftingType());
		}
		else
		{
			throw new RuntimeException("I'm far too lazy to implement this, since it probably will never be called, even once.");
		}
	}

	@Override
	public void addRecipe(ICraftGuideRecipe recipe, ItemStack craftingType)
	{
		throw new RuntimeException("I'm far too lazy to implement this, since it probably will never be called, even once.");
	}

	@Override
	public void setDefaultTypeVisibility(ItemStack type, boolean visible)
	{
		generator.setDefaultTypeVisibility(type, visible);
	}

	@Override
	public Object[] getCraftingRecipe(IRecipe recipe)
	{
		return getCraftingRecipe(recipe, false);
	}

	@Override
	public Object[] getCraftingRecipe(IRecipe recipe, boolean allowSmallGrid)
	{
		return generator.getCraftingRecipe(recipe, allowSmallGrid);
	}
}
