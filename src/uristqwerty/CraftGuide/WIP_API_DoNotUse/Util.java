package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * WARNING: This class will be removed for the Minecraft 1.5 update!<br><br>
 *
 * Contains a number of methods that implement common functionality
 * that would otherwise need to be implemented by everyone who uses
 * the API, or that relies on parts of CraftGuide not included in
 * the API.
 */

@Deprecated
public abstract class Util
{
	/**
	 * An instance of Util, containing the method implementations.
	 * <br><br>
	 * It is set during CraftGuide's {@literal @PreInit}.
	 */
	public static Util instance;

	/**
	 * Converts the passed ItemStack's name and information into a List
	 * of Strings for display, similar to how GuiContainer does it.
	 * Additionally, contains logic to try an alternative if given a stack
	 * with a damage of -1 which produces an unusable name and information,
	 * if CraftGuide is set to always show item IDs it will insert the item's
	 * ID and damage value as the second line. If an exception is thrown
	 * at any time during the process, it will log it to CraftGuide.log and
	 * generate an error text to display that at least shows the item ID and
	 * damage.
	 * @param stack
	 * @return
	 */
	public abstract List<String> getItemStackText(ItemStack stack);

	/**
	 * Gets a standard {@link IItemFilter} for any of the common types:
	 * <li>ItemStack
	 * <li>List of ItemStacks
	 * <li>String
	 *
	 * @param item
	 * @return
	 */
	public abstract IItemFilter getCommonFilter(Object item);

	/**
	 * Gets a texture usable with {@link IRenderer#renderRect}
	 * from a String identifier. At the moment, it only accepts the
	 * hardcoded values "ItemStack-Any", "ItemStack-Background", and
	 * "ItemStack-OreDict", but the eventual intention is to load the
	 * definitions from external text files, to allow for a far more
	 * advanced ability to re-skin the entire GUI than is normally
	 * possible from just swapping a texture file.
	 *
	 * @param identifier
	 * @return
	 */
	public abstract NamedTexture getTexture(String identifier);
}
