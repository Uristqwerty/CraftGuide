package uristqwerty.CraftGuide.api;

import net.minecraft.util.ResourceLocation;

public interface PseudoFluidStack
{
	public ResourceLocation getIcon();
	public String getLocalizedName();
	public int getQuantity();
	public boolean isFluidEqual(PseudoFluidStack other);
}
