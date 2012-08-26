package uristqwerty.CraftGuide.WIP_API_DoNotUse;

import uristqwerty.CraftGuide.WIP_API.SlotType;

public interface IRecipe
{
	/**
	 * Search the recipe to see if it contains at least one of the specified object.
	 * Assumes a SlotType of ANY_SLOT.
	 */
	public boolean contains(Object search);
	
	/**
	 * Search the recipe to see if it contains at least one of the specified object.
	 */
	public boolean contains(Object search, SlotType type);
}
