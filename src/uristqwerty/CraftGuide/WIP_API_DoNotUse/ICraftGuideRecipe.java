package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import net.minecraft.src.ItemStack;

/**
 * Contains a number of methods that allow instances of {@link IRecipeFilter} and
 * {@link IRecipeFilter2} to determine what items a recipe contains.
 * <br><br>
 * Also contains a few bad decisions which can't be fixed without risking problems
 * with existing mods (ItemSlot should really have its own file, getSlotUnderMouse
 * really shouldn't be a part of this interface at all).
 */

public interface ICraftGuideRecipe
{
	public boolean containsItem(ItemStack filter);
	public boolean containsItem(IItemFilter filter);
	public Object[] getItems();
	public int width();
	public int height();
	public IItemFilter getRecipeClickedResult(int x, int y);
	public List<String> getItemText(int mouseX, int mouseY);
	public void draw(IRenderer renderer, int xOffset, int yOffset, boolean isMouseOver, int mouseX, int mouseY);
}
