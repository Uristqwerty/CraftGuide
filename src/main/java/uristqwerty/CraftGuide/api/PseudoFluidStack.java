package uristqwerty.CraftGuide.api;

import net.minecraft.util.IIcon;

public interface PseudoFluidStack
{
	public IIcon getIcon();
	public String getLocalizedName();
	public int getQuantity();
	public boolean isFluidEqual(PseudoFluidStack other);
}
