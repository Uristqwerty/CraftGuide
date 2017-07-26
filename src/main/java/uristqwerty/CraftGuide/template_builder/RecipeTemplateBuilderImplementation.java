package uristqwerty.CraftGuide.template_builder;

import static uristqwerty.CraftGuide.CraftGuide.unimplemented;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.RecipeGeneratorImplementation;
import uristqwerty.CraftGuide.api.ChanceSlot;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate.RecipeBuilder;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.LiquidSlot;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.api.PseudoFluidStack;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeTemplate;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder;
import uristqwerty.CraftGuide.api.Renderer;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.api.Util;

public class RecipeTemplateBuilderImplementation implements RecipeTemplateBuilder
{
	private ItemStack craftingType;
	private int maxColumnHeight = 0;
	private ArrayList<ColumnLayout> columns = new ArrayList<ColumnLayout>();
	private ColumnLayout currentColumn = new ColumnLayout();
	private TemplateBuilderSlotType slotType = TemplateBuilderSlotType.INPUT;

	public RecipeTemplateBuilderImplementation(ItemStack type)
	{
		craftingType = type;
	}

	@Override
	public RecipeTemplateBuilder nextColumn()
	{
		return nextColumn(0);
	}

	@Override
	public RecipeTemplateBuilder nextColumn(int gap)
	{
		finishColumn(gap);
		return this;
	}

	@Override
	public RecipeTemplateBuilder shapelessItemGrid(int width, int height)
	{
		currentColumn.items.add(new ShapelessGrid(width, height, slotType));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18 * width);
		currentColumn.totalHeight += 18 * height;
		return this;
	}

	@Override
	public RecipeTemplateBuilder shapedItemGrid(int width, int height)
	{
		currentColumn.items.add(new ShapedGrid(width, height, slotType));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18 * width);
		currentColumn.totalHeight += 18 * height;
		return this;
	}

	@Override
	public RecipeTemplateBuilder item()
	{
		currentColumn.items.add(new Item(slotType));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder outputItem()
	{
		currentColumn.items.add(new Item(TemplateBuilderSlotType.OUTPUT));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder machineItem()
	{
		currentColumn.items.add(new Item(TemplateBuilderSlotType.MACHINE));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder chanceItem()
	{
		currentColumn.items.add(new ChanceItem(slotType));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder liquid()
	{
		currentColumn.items.add(new Liquid(slotType));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder text(int width)
	{
		throw unimplemented();
	}

	@Override
	public RecipeTemplateBuilder textBlock(int width, int rows)
	{
		throw unimplemented();
	}

	@Override
	public RecipeTemplateBuilder icon(int width, int height)
	{
		throw unimplemented();
	}

	@Override
	public RecipeTemplateBuilder iconWithData(int width, int height, IconMode mode, int spaceForText)
	{
		throw unimplemented();
	}

	@Override
	public RecipeTemplateBuilder setColumnAlign(VerticalAlign align)
	{
		throw unimplemented();
	}

	@Override
	public RecipeTemplateBuilder setItemAlign(HorizontalAlign align)
	{
		throw unimplemented();
	}

	@Override
	public RecipeTemplateBuilder nextSlotType(TemplateBuilderSlotType type)
	{
		slotType = type;
		return this;
	}

	@Override
	public ConstructedRecipeTemplate finishTemplate()
	{
		finishColumn(0);
		ArrayList<ColumnItem> dataPattern = new ArrayList<ColumnItem>();
		ArrayList<Slot> slots = new ArrayList<Slot>();

		int xOffset = 0;
		int rightEdge = 0;
		for(ColumnLayout column: columns)
		{
			xOffset = column.offset + 3;
			rightEdge = Math.max(rightEdge, xOffset + column.totalWidth);
			int yOffset = (maxColumnHeight - column.totalHeight) / 2 + 3;
			for(ColumnItem item: column.items)
			{
				dataPattern.add(item);
				if(item instanceof Item)
				{
					int hAlign = (column.totalWidth - 18) / 2;
					slots.add(new ItemSlot(xOffset + 1 + hAlign, yOffset + 1, 16, 16, true)
						.drawOwnBackground(item.slotType.drawBackground())
						.setSlotType(item.slotType.toSlotType()));
					yOffset += 18;
				}
				else if(item instanceof ChanceItem)
				{
					int hAlign = (column.totalWidth - 18) / 2;
					slots.add(new ChanceSlot(xOffset + 1 + hAlign, yOffset + 1, 16, 16, true)
						.setRatio(1)
						.drawOwnBackground(item.slotType.drawBackground())
						.setSlotType(item.slotType.toSlotType()));
					yOffset += 18;
				}
				else if(item instanceof Liquid)
				{
					int hAlign = (column.totalWidth - 18) / 2;
					slots.add(new LiquidSlot(xOffset + 1 + hAlign, yOffset + 1)
						.setSlotType(item.slotType.toSlotType()));
					yOffset += 18;
				}
				else if(item instanceof ShapelessGrid)
				{
					ShapelessGrid grid = (ShapelessGrid)item;
					int hAlign = (column.totalWidth - 18 * grid.width) / 2;
					slots.add(new DecorationSlot(xOffset, yOffset, grid.width * 18, grid.height * 18, "shapeless-grid"));
					for(int y = 0; y < grid.height; y++)
					{
						for(int x = 0; x < grid.width; x++)
						{
							slots.add(new ItemSlot(xOffset + x * 18 + 1 + hAlign, yOffset + y * 18 + 1, 16, 16, true)
								.drawOwnBackground(false)
								.setSlotType(item.slotType.toSlotType()));
						}
					}
					yOffset += grid.height * 18;
				}
				else if(item instanceof ShapedGrid)
				{
					ShapedGrid grid = (ShapedGrid)item;
					int hAlign = (column.totalWidth - 18 * grid.width) / 2;
					for(int y = 0; y < grid.height; y++)
					{
						for(int x = 0; x < grid.width; x++)
						{
							slots.add(new ItemSlot(xOffset + x * 18 + 1 + hAlign, yOffset + y * 18 + 1, 16, 16, true)
								.drawOwnBackground(item.slotType.drawBackground())
								.setSlotType(item.slotType.toSlotType()));
						}
					}
					yOffset += grid.height * 18;
				}
				else
				{
					CraftGuideLog.log("Unimplemented thingy! " + item.getClass().getSimpleName());
				}
			}
		}
		ConstructedRecipeTemplateImplementation template = new ConstructedRecipeTemplateImplementation();
		template.slots = slots.toArray(new Slot[0]);
		template.data = dataPattern.toArray(new ColumnItem[0]);
		template.template = RecipeGeneratorImplementation.instance.createRecipeTemplate(template.slots, craftingType);
		template.template.setSize(rightEdge + 3, maxColumnHeight + 6);

		return template;
	}

	private void finishColumn(int gap)
	{
		maxColumnHeight = Math.max(maxColumnHeight, currentColumn.totalHeight);
		columns.add(currentColumn);
		ColumnLayout newColumn = new ColumnLayout();
		newColumn.offset = currentColumn.offset + currentColumn.totalWidth + gap;
		currentColumn = newColumn;
	}
	
	private static class DecorationSlot implements Slot
	{
		private final int x, y, w, h;
		private final NamedTexture texture;
		public DecorationSlot(int xOffset, int yOffset, int width, int height, String texture)
		{
			x = xOffset;
			y = yOffset;
			w = width;
			h = height;
			this.texture = Util.instance.getTexture(texture);
		}

		@Override
		public void draw(Renderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver)
		{
			renderer.renderRect(this.x + x, this.y + y, w, h, texture);
		}

		@Override
		public ItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
		{
			return null;
		}

		@Override
		public boolean isPointInBounds(int x, int y, Object[] data, int dataIndex)
		{
			return false;
		}

		@Override
		public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
		{
			return null;
		}

		@Override
		public boolean matches(ItemFilter filter, Object[] data, int dataIndex, SlotType type)
		{
			return false;
		}
	}

	private static class ConstructedRecipeTemplateImplementation implements ConstructedRecipeTemplate
	{
		ColumnItem[] data;
		Slot[] slots;
		RecipeTemplate template;

		@Override
		public RecipeBuilder buildRecipe()
		{
			return new RecipeBuilderImplementation(data, slots, template);
		}
	}

	private static class RecipeBuilderImplementation implements RecipeBuilder
	{
		private final ColumnItem[] data;
		private final Slot[] slots;
		private final RecipeTemplate template;
		private int dataIndex = 0, slotIndex = 0;
		private Object[] recipeData;

		public RecipeBuilderImplementation(ColumnItem[] data, Slot[] slots, RecipeTemplate template)
		{
			this.slots = slots;
			this.data = data;
			this.template = template;
			recipeData = new Object[slots.length];
		}

		@Override
		public RecipeBuilder shapelessItemGrid(Object[] items)
		{
			return shapelessItemGrid(Arrays.asList(items));
		}

		@Override
		public RecipeBuilder shapelessItemGrid(List<?> items)
		{
			ShapelessGrid grid = (ShapelessGrid)nextData();
			if(items.size() > grid.width * grid.height)
				throw new RuntimeException("Shapeless recipe given more items than fit");

			pushRecipeData(null);

			for(int i = 0; i < grid.width * grid.height; i++)
			{
				if(i < items.size())
					pushRecipeData(items.get(i));
				else
					pushRecipeData(null);
			}

			return this;
		}

		@Override
		public RecipeBuilder shapedItemGrid(int width, int height, Object[] items)
		{
			return shapedItemGrid(width, height, Arrays.asList(items));
		}

		@Override
		public RecipeBuilder shapedItemGrid(int width, int height, List<?> items)
		{
			ShapedGrid grid = (ShapedGrid)nextData();
			if(width > grid.width || height > grid.height || items.size() != width * height)
				throw new RuntimeException("Shaped recipe given mismatched data");
			int yOffset = (grid.height - height) / 2;
			int xOffset = (grid.width - width) / 2;
			for(int y = 0; y < height; y++)
			{
				int yPos = y + yOffset;
				for(int x = 0; x < width; x++)
				{
					int xPos = x + xOffset;
					recipeData[slotIndex + yPos * grid.width + xPos] = items.get(y * width + x);
				}
			}
			slotIndex += grid.width * grid.height;
			return this;
		}

		@Override
		public RecipeBuilder item(Object item)
		{
			if(nextData() instanceof Item)
				pushRecipeData(item);
			else
				throw new RuntimeException("Data type mismatch");

			return this;
		}

		@Override
		public RecipeBuilder chanceItem(Object item, double chance)
		{
			if(nextData() instanceof ChanceItem)
				pushRecipeData(new Object[]{item, chance});
			else
				throw new RuntimeException("Data type mismatch");

			return this;
		}

		private void pushRecipeData(Object item)
		{
			recipeData[slotIndex++] = item;
		}

		@Override
		public void addRecipe(RecipeGenerator generator)
		{
			if(dataIndex == data.length && slotIndex == slots.length)
				generator.addRecipe(template, recipeData);
		}

		private ColumnItem nextData()
		{
			if(dataIndex >= data.length)
				throw new RuntimeException("Slot underflow and I haven't implemented proper error handling");
			return data[dataIndex++];
		}

		@Override
		public RecipeBuilder liquid(FluidStack liquid)
		{
			if(nextData() instanceof Liquid)
				pushRecipeData(liquid);
			else
				throw new RuntimeException("Data type mismatch");

			return this;
		}

		@Override
		public RecipeBuilder liquid(PseudoFluidStack liquid)
		{
			if(nextData() instanceof Liquid)
				pushRecipeData(liquid);
			else
				throw new RuntimeException("Data type mismatch");

			return this;
		}

		@Override
		public RecipeBuilder text(String text)
		{
			throw unimplemented();
		}

		@Override
		public RecipeBuilder icon(String iconName)
		{
			throw unimplemented();
		}

		@Override
		public RecipeBuilder icon(String iconName, float u, float v, float u2, float v2)
		{
			throw unimplemented();
		}

		@Override
		public RecipeBuilder iconWithData(String iconName, int data)
		{
			throw unimplemented();
		}

		@Override
		public RecipeBuilder iconWithData(String iconName, float u, float v, float u2, float v2, int data)
		{
			throw unimplemented();
		}
	}

	private static class ColumnLayout
	{
		int totalHeight = 0, totalWidth = 0, offset = 0;
		ArrayList<ColumnItem> items = new ArrayList<ColumnItem>();
	}

	private static abstract class ColumnItem
	{
		final TemplateBuilderSlotType slotType;

		public ColumnItem(TemplateBuilderSlotType slotType)
		{
			this.slotType = slotType;
		}
	}

	private static class ShapelessGrid extends ColumnItem
	{
		final int width, height;

		public ShapelessGrid(int width, int height, TemplateBuilderSlotType slotType)
		{
			super(slotType);
			this.width = width;
			this.height = height;
		}
	}

	private static class Item extends ColumnItem
	{
		public Item(TemplateBuilderSlotType slotType)
		{
			super(slotType);
		}
	}

	private static class ChanceItem extends ColumnItem
	{
		public ChanceItem(TemplateBuilderSlotType slotType)
		{
			super(slotType);
		}
	}

	private static class Liquid extends ColumnItem
	{
		public Liquid(TemplateBuilderSlotType slotType)
		{
			super(slotType);
		}
	}

	private static class ShapedGrid extends ColumnItem
	{
		final int width, height;

		public ShapedGrid(int width, int height, TemplateBuilderSlotType slotType)
		{
			super(slotType);
			this.width = width;
			this.height = height;
		}
	}
}
