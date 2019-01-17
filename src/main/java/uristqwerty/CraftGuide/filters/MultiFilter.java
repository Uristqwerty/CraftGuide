package uristqwerty.CraftGuide.filters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import uristqwerty.CraftGuide.CommonUtilities;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.ForgeExtensions;
import uristqwerty.CraftGuide.api.CombinableItemFilter;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.LiquidFilter;
import uristqwerty.CraftGuide.api.PseudoFluidFilter;
import uristqwerty.CraftGuide.api.PseudoFluidStack;
import uristqwerty.CraftGuide.api.Renderer;

public class MultiFilter implements CombinableItemFilter
{
	private CombinableItemFilter items = new MultipleItemFilter(new ArrayList<ItemStack>());
	private ArrayList<String> strings = new ArrayList<String>();
	private ArrayList<FluidStack> liquids = new ArrayList<FluidStack>();
	private ArrayList<PseudoFluidStack> pseudoLiquids = new ArrayList<PseudoFluidStack>();

	public MultiFilter(CombinableItemFilter items)
	{
		this.addItemFilter(items);
	}

	@Override
	public boolean matches(Object item)
	{
		if(items != null && items.matches(item))
			return true;

		for(String s: strings)
			if(stringMatch(s, item))
				return true;

		for(FluidStack s: liquids)
			if(liquidMatch(s, item))
				return true;

		for(PseudoFluidStack s: pseudoLiquids)
			if(liquidMatch(s, item))
				return true;

		return false;
	}

	@Override
	public void draw(Renderer renderer, int x, int y)
	{
		items.draw(renderer, x, y);
	}

	@Override
	public List<String> getTooltip()
	{
		ArrayList<String> tooltip = new ArrayList<String>();

		for(String s: strings)
			tooltip.add("Text: \"" + s + "\"");

		for(FluidStack s: liquids)
			tooltip.add("Liquid: \"" + s.getLocalizedName() + "\"");

		for(PseudoFluidStack s: pseudoLiquids)
			tooltip.add("Liquid: \"" + s.getLocalizedName() + "\"");

		tooltip.addAll(items.getTooltip());
		return tooltip;
	}

	@Override
	public ItemFilter addItemFilter(ItemFilter other)
	{
		if(other instanceof SingleItemFilter || other instanceof MultipleItemFilter)
		{
			ItemFilter filter = items.addItemFilter(other);
			if(filter instanceof CombinableItemFilter)
			{
				MultiFilter new_filter = new MultiFilter(this);
				new_filter.items = (CombinableItemFilter)filter;
				return new_filter;
			}
		}
		else if(other instanceof StringItemFilter)
		{
			String comparison = ((StringItemFilter)other).comparison;
			comparison = comparison.toLowerCase();
			for(String s: strings)
				if(comparison.equals(s))
					return this;

			MultiFilter new_filter = new MultiFilter(this);
			new_filter.strings = new ArrayList<String>(new_filter.strings);
			new_filter.strings.add(comparison);
			return new_filter;
		}
		else if(other instanceof LiquidFilter)
		{
			FluidStack stack = ((LiquidFilter)other).liquid;
			for(FluidStack s: liquids)
				if(s.isFluidEqual(stack))
					return this;

			MultiFilter new_filter = new MultiFilter(this);
			new_filter.liquids = new ArrayList<FluidStack>(new_filter.liquids);
			new_filter.liquids.add(stack);
			return new_filter;
		}
		else if(other instanceof PseudoFluidFilter)
		{
			PseudoFluidStack stack = ((PseudoFluidFilter)other).liquid;
			for(PseudoFluidStack s: pseudoLiquids)
				if(s.isFluidEqual(stack))
					return this;

			MultiFilter new_filter = new MultiFilter(this);
			new_filter.pseudoLiquids = new ArrayList<PseudoFluidStack>(new_filter.pseudoLiquids);
			new_filter.pseudoLiquids.add(stack);
			return new_filter;
		}
		return this;
	}
	@Override
	public ItemFilter subtractItemFilter(ItemFilter other)
	{
		if(other instanceof SingleItemFilter || other instanceof MultipleItemFilter)
		{
			ItemFilter filter = items.subtractItemFilter(other);
			if(filter instanceof CombinableItemFilter)
				items = (CombinableItemFilter)filter;
			else if(filter != null)
				items = new MultipleItemFilter(new ArrayList<ItemStack>());
		}
		else if(other instanceof StringItemFilter)
		{
			String str = ((StringItemFilter)other).comparison;
			Iterator<String> iter = strings.iterator();
			while(iter.hasNext())
				if(iter.next().equals(str))
					iter.remove();

			if(liquids.isEmpty() && pseudoLiquids.isEmpty() && strings.isEmpty())
				return items;
		}
		else if(other instanceof LiquidFilter)
		{
			FluidStack stack = ((LiquidFilter)other).liquid;
			Iterator<FluidStack> iter = liquids.iterator();
			while(iter.hasNext())
				if(iter.next().isFluidEqual(stack))
					iter.remove();

			if(liquids.isEmpty() && pseudoLiquids.isEmpty() && strings.isEmpty())
				return items;
		}
		else if(other instanceof PseudoFluidFilter)
		{
			PseudoFluidStack stack = ((PseudoFluidFilter)other).liquid;
			Iterator<PseudoFluidStack> iter = pseudoLiquids.iterator();
			while(iter.hasNext())
				if(iter.next().isFluidEqual(stack))
					iter.remove();

			if(liquids.isEmpty() && pseudoLiquids.isEmpty() && strings.isEmpty())
				return items;
		}
		return this;
	}

	@Override
	public List<ItemStack> getRepresentativeItems()
	{
		return items.getRepresentativeItems();
	}

	private static boolean stringMatch(String comparison, Object item)
	{
		if(item instanceof ItemStack)
		{
			try
			{
				return CommonUtilities.searchExtendedItemStackText(item, comparison);
			}
			catch (Throwable e)
			{
				CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.StringItemFilter.matches ItemStack branch");
				throw new RuntimeException(e);
			}
		}
		else if(item instanceof String)
		{
			try
			{
				return ((String)item).toLowerCase().contains(comparison);
			}
			catch (Throwable e)
			{
				CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.StringItemFilter.matches String branch");
				throw new RuntimeException(e);
			}
		}
		else if(item instanceof FluidStack)
		{
			return ((FluidStack)item).getLocalizedName().toLowerCase().contains(comparison);
		}
		else if(item instanceof PseudoFluidStack)
		{
			return ((PseudoFluidStack)item).getLocalizedName().toLowerCase().contains(comparison);
		}
		else if(item instanceof List)
		{
			try
			{
				List<?> list = (List<?>)item;

				for(Object o: list)
				{
					if(stringMatch(comparison, o))
					{
						return true;
					}
				}

				if(list.size() < 1)
				{
					List<String> lines = ForgeExtensions.emptyOreDictEntryText(list);

					if(lines != null)
					{
						for(String line: lines)
						{
							if(line != null && line.toLowerCase().contains(comparison))
							{
								return true;
							}
						}
					}
				}


				return false;
			}
			catch (Throwable e)
			{
				CraftGuideLog.log("exception trace: uristqwerty.CraftGuide.StringItemFilter.matches List branch");
				throw new RuntimeException(e);
			}
		}
		else
		{
			return false;
		}
	}

	private static boolean liquidMatch(FluidStack liquid, Object item)
	{
		if(item instanceof ItemStack)
		{
			ItemStack stack = (ItemStack)item;

			if(stack.getItem() != null)
				return liquid.isFluidEqual(stack);
		}
		else if(item instanceof FluidStack)
		{
			return liquid.isFluidEqual((FluidStack)item);
		}
		else if(item instanceof String)
		{
			return liquid.getLocalizedName().toLowerCase().contains(((String)item).toLowerCase());
		}
		else if(item instanceof List)
		{
			for(Object object: ((List<?>)item))
			{
				if(liquidMatch(liquid, object))
				{
					return true;
				}
			}
		}

		return false;
	}

	private static boolean liquidMatch(PseudoFluidStack liquid, Object item)
	{
		if(item instanceof PseudoFluidStack)
		{
			return liquid.isFluidEqual((PseudoFluidStack)item);
		}
		else if(item instanceof String)
		{
			return liquid.getLocalizedName().toLowerCase().contains(((String)item).toLowerCase());
		}
		else if(item instanceof List)
		{
			for(Object object: ((List<?>)item))
			{
				if(liquidMatch(liquid, object))
				{
					return true;
				}
			}
		}

		return false;
	}
}
