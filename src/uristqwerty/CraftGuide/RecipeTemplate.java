package uristqwerty.CraftGuide;

import uristqwerty.CraftGuide.WIP_API_DoNotUse.ICraftGuideRecipe;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.IRecipeTemplate;
import uristqwerty.CraftGuide.WIP_API_DoNotUse.ItemSlot;
import uristqwerty.CraftGuide.ui.Rendering.TexturedRect;
import net.minecraft.src.ItemStack;

public class RecipeTemplate implements IRecipeTemplate
{
	private ItemSlot[] slots;
	private TexturedRect background, backgroundSelected;
	private int width = 79, height = 58; 
	
	ItemStack craftingType;
	
	public RecipeTemplate(ItemSlot[] slots, ItemStack craftingType, TexturedRect background, TexturedRect backgroundSelected)
	{
		this.slots = slots;
		this.background = background;
		this.backgroundSelected = backgroundSelected;
		this.craftingType = craftingType;
	}

	public ICraftGuideRecipe generate(Object[] items)
	{
		return new Recipe(slots, items, background, backgroundSelected).setSize(width, height);
	}

	@Override
	public IRecipeTemplate setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		if(background != null)
		{
			background.setSize(width, height);
		}
		
		if(backgroundSelected != null)
		{
			backgroundSelected.setSize(width, height);
		}
		
		return this;
	}
}
