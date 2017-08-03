package uristqwerty.CraftGuide.template_builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.DefaultRecipeTemplate;
import uristqwerty.CraftGuide.Recipe;
import uristqwerty.CraftGuide.RecipeGeneratorImplementation;
import uristqwerty.CraftGuide.api.ChanceSlot;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate.RecipeBuilder;
import uristqwerty.CraftGuide.api.ConstructedRecipeTemplate.SubunitBuilder;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.LiquidSlot;
import uristqwerty.CraftGuide.api.NamedTexture;
import uristqwerty.CraftGuide.api.PseudoFluidStack;
import uristqwerty.CraftGuide.api.RecipeGenerator;
import uristqwerty.CraftGuide.api.RecipeTemplateBuilder;
import uristqwerty.CraftGuide.api.Renderer;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.api.SlotType;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.api.slotTypes.IconSlot;
import uristqwerty.CraftGuide.api.slotTypes.TextSlot;

public class RecipeTemplateBuilderImplementation implements RecipeTemplateBuilder
{
	private ItemStack craftingType;
	private int maxColumnHeight = 0;
	private ArrayList<ColumnLayout> columns = new ArrayList<ColumnLayout>();
	private ColumnLayout currentColumn = new ColumnLayout();
	private TemplateBuilderSlotType slotType = TemplateBuilderSlotType.INPUT;
	private HorizontalAlign itemAlign = HorizontalAlign.CENTER;
	private VerticalAlign defaultColumnAlign = VerticalAlign.CENTER;

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
		currentColumn.items.add(new ShapelessGrid(width, height, slotType, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18 * width);
		currentColumn.totalHeight += 18 * height;
		return this;
	}

	@Override
	public RecipeTemplateBuilder shapedItemGrid(int width, int height)
	{
		currentColumn.items.add(new ShapedGrid(width, height, slotType, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18 * width);
		currentColumn.totalHeight += 18 * height;
		return this;
	}

	@Override
	public RecipeTemplateBuilder item()
	{
		currentColumn.items.add(new Item(slotType, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder outputItem()
	{
		currentColumn.items.add(new Item(TemplateBuilderSlotType.OUTPUT, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder machineItem()
	{
		currentColumn.items.add(new Item(TemplateBuilderSlotType.MACHINE, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder chanceItem()
	{
		currentColumn.items.add(new ChanceItem(slotType, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder liquid()
	{
		currentColumn.items.add(new Liquid(slotType, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, 18);
		currentColumn.totalHeight += 18;
		return this;
	}

	@Override
	public RecipeTemplateBuilder text(int width, TextOverflow overflowHandling)
	{
		return textBlock(width, 1, overflowHandling);
	}

	@Override
	public RecipeTemplateBuilder textBlock(int width, int rows, TextOverflow overflowHandling)
	{
		currentColumn.items.add(new Text(width, rows, overflowHandling, slotType, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, width);
		currentColumn.totalHeight += rows * 9;
		return this;
	}

	@Override
	public RecipeTemplateBuilder icon(int width, int height)
	{
		return iconWithData(width, height, IconMode.PLAIN_ICON, 0);
	}

	@Override
	public RecipeTemplateBuilder iconWithData(int width, int height, IconMode mode, int spaceForText)
	{
		currentColumn.items.add(new Icon(width, height, mode, spaceForText, slotType, itemAlign));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, width + spaceForText);
		currentColumn.totalHeight += height + 1;
		return this;
	}

	@Override
	public RecipeTemplateBuilder setColumnAlign(VerticalAlign align)
	{
		defaultColumnAlign  = align;
		currentColumn.align = align;
		return this;
	}

	@Override
	public RecipeTemplateBuilder setItemAlign(HorizontalAlign align)
	{
		itemAlign = align;
		return this;
	}

	@Override
	public RecipeTemplateBuilder repeatedSubunit(SubunitLayout layoutMode, SubunitDescriptor contents)
	{
		RecipeTemplateBuilderImplementation innerBuilder = new RecipeTemplateBuilderImplementation(null);
		innerBuilder.slotType = slotType;
		contents.defineSubunit(innerBuilder);
		ConstructedRecipeTemplate innerTemplate = innerBuilder.finishTemplate();
		final int innerHeight = innerBuilder.maxColumnHeight + 3;

		int subunitWidth = 1;
		for(ColumnLayout column: innerBuilder.columns)
		{
			subunitWidth = Math.max(subunitWidth, column.offset + column.totalWidth);
		}

		currentColumn.align = VerticalAlign.TOP;
		currentColumn.items.add(new Subunit(innerTemplate, itemAlign, subunitWidth, innerHeight));
		currentColumn.totalWidth = Math.max(currentColumn.totalWidth, subunitWidth);
		currentColumn.totalHeight += innerHeight;
		return this;
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
			if(column.align == VerticalAlign.BOTTOM)
			{
				yOffset = maxColumnHeight - column.totalHeight + 3;
			}
			else if(column.align == VerticalAlign.TOP)
			{
				yOffset = 3;
			}

			for(ColumnItem item: column.items)
			{
				dataPattern.add(item);
				if(item instanceof Item)
				{
					int itemWidth = 18;
					int hAlign = calcHorizontalAlign(item.align, column.totalWidth, itemWidth);
					slots.add(new ItemSlot(xOffset + 1 + hAlign, yOffset + 1, 16, 16, true)
						.drawOwnBackground(item.slotType.drawBackground())
						.setSlotType(item.slotType.toSlotType()));
					yOffset += 18;
				}
				else if(item instanceof ChanceItem)
				{
					int itemWidth = 18;
					int hAlign = calcHorizontalAlign(item.align, column.totalWidth, itemWidth);
					slots.add(new ChanceSlot(xOffset + 1 + hAlign, yOffset + 1, 16, 16, true)
						.setRatio(1)
						.drawOwnBackground(item.slotType.drawBackground())
						.setSlotType(item.slotType.toSlotType()));
					yOffset += 18;
				}
				else if(item instanceof Liquid)
				{
					int itemWidth = 18;
					int hAlign = calcHorizontalAlign(item.align, column.totalWidth, itemWidth);
					slots.add(new LiquidSlot(xOffset + 1 + hAlign, yOffset + 1)
						.setSlotType(item.slotType.toSlotType()));
					yOffset += 18;
				}
				else if(item instanceof ShapelessGrid)
				{
					ShapelessGrid grid = (ShapelessGrid)item;
					int itemWidth = 18 * grid.width;
					int hAlign = calcHorizontalAlign(item.align, column.totalWidth, itemWidth);
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
					int itemWidth = 18 * grid.width;
					int hAlign = calcHorizontalAlign(item.align, column.totalWidth, itemWidth);
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
				else if(item instanceof Text)
				{
					Text text = (Text)item;
					int itemWidth = text.width;
					int hAlign = calcHorizontalAlign(item.align, column.totalWidth, itemWidth);
					slots.add(new TextSlot(xOffset + hAlign, yOffset));
					yOffset += text.rows * 9;
				}
				else if(item instanceof Icon)
				{
					Icon icon = (Icon)item;
					int itemWidth = icon.iconWidth + icon.textWidth;
					int hAlign = calcHorizontalAlign(item.align, column.totalWidth, itemWidth);
					slots.add(new IconSlot(xOffset + hAlign, yOffset, icon.iconWidth, icon.height));
					if(icon.mode == IconMode.ICON_AND_LABEL)
					{
						slots.add(new TextSlot(xOffset + hAlign + icon.iconWidth, yOffset + (icon.height - 9) / 2));
					}
					yOffset += icon.height;
				}
				else if(item instanceof Subunit)
				{
					Subunit subunit = (Subunit)item;
					slots.add(new SubunitSlot(item.align, xOffset, yOffset, subunit.width, subunit.height, column.totalWidth, subunit.innerTemplate));
					yOffset += subunit.height;
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
		template.template = (DefaultRecipeTemplate)RecipeGeneratorImplementation.instance.createRecipeTemplate(template.slots, craftingType);
		template.template.setSize(rightEdge + 3, maxColumnHeight + 6);

		return template;
	}

	private static int calcHorizontalAlign(HorizontalAlign align, int totalWidth, int itemWidth)
	{
		switch(align)
		{
		case LEFT: return 0;
		case RIGHT: return totalWidth - itemWidth;
		case CENTER: default: return (totalWidth - itemWidth) / 2;
		}
	}

	private void finishColumn(int gap)
	{
		maxColumnHeight = Math.max(maxColumnHeight, currentColumn.totalHeight);
		columns.add(currentColumn);
		ColumnLayout newColumn = new ColumnLayout();
		newColumn.offset = currentColumn.offset + currentColumn.totalWidth + gap;
		newColumn.align = defaultColumnAlign;
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
		DefaultRecipeTemplate template;

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
		private final DefaultRecipeTemplate template;
		private int dataIndex = 0, slotIndex = 0;
		private Object[] recipeData;

		public RecipeBuilderImplementation(ColumnItem[] data, Slot[] slots, DefaultRecipeTemplate template)
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
			Text textDef = (Text)nextData();
			String unwrappedLines[] = text.split("\n");
			switch(textDef.overflow)
			{
			default:
			case OVERFLOW:
				pushRecipeData(unwrappedLines);
				return this;
			case TRUNCATE:
				{
					final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
					if(unwrappedLines.length > textDef.rows)
					{
						unwrappedLines = Arrays.copyOf(unwrappedLines, textDef.rows);
					}
					for(int i = 0; i < unwrappedLines.length; i++)
					{
						while(fontRenderer.getStringWidth(unwrappedLines[i]) > textDef.width)
						{
							unwrappedLines[i] = unwrappedLines[i].substring(0, unwrappedLines[i].length() - 1);
						}
					}
					pushRecipeData(unwrappedLines);
					return this;
				}
			case WRAP:
				{
					final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
					ArrayList<String> wrappedLines = new ArrayList<String>();
					for(int i = 0; i < unwrappedLines.length; i++)
					{
						String overflow = "";
						String current = unwrappedLines[i];
						do
						{
							if(fontRenderer.getStringWidth(current) > textDef.width)
							{
								final int split = current.length() - 1;
								overflow = current.substring(split) + overflow;
								current = current.substring(0, split);
							}
							else
							{
								wrappedLines.add(current);
								current = overflow;
								overflow = "";
							}
						}
						while(!current.isEmpty());
					}
					pushRecipeData(wrappedLines);
					return this;
				}
			}
		}

		@Override
		public RecipeBuilder icon(String sourceTexture, String iconName)
		{
			return icon(sourceTexture, iconName, null, 0, 0, 16, 16);
		}

		@Override
		public RecipeBuilder icon(String sourceTexture, String iconName, Integer iconTint, float u, float v, float u2, float v2)
		{
			Icon icon = (Icon)nextData();
			if(icon.mode != IconMode.PLAIN_ICON)
				throw new RuntimeException("Data type mismatch");
			if(iconTint == null)
				iconTint = 0xffffffff;

			Object source = convertTextureSource(sourceTexture, iconName);
			pushRecipeData(new Object[] {source, u, v, u2, v2, iconTint});
			return this;
		}

		@Override
		public RecipeBuilder iconWithText(String sourceTexture, String iconName, String text)
		{
			return iconWithText(sourceTexture, iconName, null, 0, 0, 16, 16, text);
		}

		@Override
		public RecipeBuilder iconWithText(String sourceTexture, String iconName, Integer iconTint, float u, float v, float u2, float v2, String text)
		{
			Icon icon = (Icon)nextData();
			if(icon.mode == IconMode.PLAIN_ICON)
				throw new RuntimeException("Data type mismatch");
			if(iconTint == null)
				iconTint = 0xffffffff;

			Object source = convertTextureSource(sourceTexture, iconName);
			if(icon.mode == IconMode.ICON_AND_STACKSIZE)
			{
				pushRecipeData(new Object[] {source, u, v, u2, v2, iconTint, text});
			}
			else if(icon.mode == IconMode.ICON_AND_LABEL)
			{
				pushRecipeData(new Object[] {source, u, v, u2, v2, iconTint});
				pushRecipeData(text);
			}
			return this;
		}

		@Override
		public <T> RecipeBuilder subUnit(T[] items, SubunitBuilder<T> builder)
		{
			return subUnit(items != null? Arrays.asList(items) : null, builder);
		}

		@Override
		public <T> RecipeBuilder subUnit(Collection<T> items, SubunitBuilder<T> builder)
		{
			Subunit subunit = (Subunit)nextData();
			ArrayList<Object[]> contents = new ArrayList<Object[]>();
			if(items != null)
			{
				for(T item: items)
				{
					RecipeBuilderImplementation recipeBuilder = (RecipeBuilderImplementation)subunit.innerTemplate.buildRecipe();
					builder.build(item, recipeBuilder);
					contents.add(recipeBuilder.recipeData);
				}
			}
			pushRecipeData(contents.toArray());
			return this;
		}

		private Object convertTextureSource(String sourceTexture, String iconName)
		{
			final ResourceLocation sourceLocation = new ResourceLocation(sourceTexture);
			if(iconName == null || iconName.isEmpty())
			{
				return sourceLocation;
			}
			ITextureObject texture = Minecraft.getMinecraft().renderEngine.getTexture(sourceLocation);
			if(texture instanceof TextureMap)
			{
				TextureAtlasSprite sprite = ((TextureMap)texture).getAtlasSprite(iconName);
				return new Object[] {sourceLocation, sprite};
			}
			return null;
		}

		private void pushRecipeData(Object item)
		{
			recipeData[slotIndex++] = item;
		}

		private ColumnItem nextData()
		{
			if(dataIndex >= data.length)
				throw new RuntimeException("Slot underflow and I haven't implemented proper error handling");
			return data[dataIndex++];
		}

		@Override
		public void addRecipe(RecipeGenerator generator)
		{
			if(dataIndex == data.length && slotIndex == slots.length)
			{
				int height = template.height();
				for(int i = 0; i < slots.length; i++)
				{
					if(slots[i] instanceof SubunitSlot)
					{
						SubunitSlot sub = (SubunitSlot)slots[i];
						int len = ((Object[])recipeData[i]).length;
						final int perRow = sub.perRow(len);
						if(perRow > 0)
						{
							int rows = (len + perRow - 1) / perRow;
							height = Math.max(height, sub.groupY + rows * sub.unitHeight + 3);
						}
					}
				}

				if(height != template.height())
				{
					Recipe recipe = template.generateWithSize(recipeData, template.width(), height);
					generator.addRecipe(recipe, template.getCraftingType());
				}
				else
				{
					generator.addRecipe(template, recipeData);
				}
			}
		}
	}

	private static class ColumnLayout
	{
		public VerticalAlign align = VerticalAlign.CENTER;
		int totalHeight = 0, totalWidth = 0, offset = 0;
		ArrayList<ColumnItem> items = new ArrayList<ColumnItem>();
	}

	private static abstract class ColumnItem
	{
		final TemplateBuilderSlotType slotType;
		final HorizontalAlign align;

		public ColumnItem(TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			this.slotType = slotType;
			this.align = align;
		}
	}

	private static class ShapelessGrid extends ColumnItem
	{
		final int width, height;

		public ShapelessGrid(int width, int height, TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			super(slotType, align);
			this.width = width;
			this.height = height;
		}
	}

	private static class Item extends ColumnItem
	{
		public Item(TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			super(slotType, align);
		}
	}

	private static class ChanceItem extends ColumnItem
	{
		public ChanceItem(TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			super(slotType, align);
		}
	}

	private static class Liquid extends ColumnItem
	{
		public Liquid(TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			super(slotType, align);
		}
	}

	private static class ShapedGrid extends ColumnItem
	{
		final int width, height;

		public ShapedGrid(int width, int height, TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			super(slotType, align);
			this.width = width;
			this.height = height;
		}
	}

	private static class Text extends ColumnItem
	{
		final int width, rows;
		final TextOverflow overflow;

		public Text(int width, int rows, TextOverflow overflowMode, TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			super(slotType, align);
			this.width = width;
			this.rows = rows;
			this.overflow = overflowMode;
		}
	}

	private static class Icon extends ColumnItem
	{
		final int iconWidth, textWidth, height;
		final IconMode mode; 

		public Icon(int width, int height, IconMode mode, int spaceForText, TemplateBuilderSlotType slotType, HorizontalAlign align)
		{
			super(slotType, align);
			this.iconWidth = width;
			this.height = height;
			this.textWidth = spaceForText;
			this.mode = mode;
		}
	}

	private static class Subunit extends ColumnItem
	{
		final int width, height;
		final ConstructedRecipeTemplateImplementation innerTemplate;

		public Subunit(ConstructedRecipeTemplate innerTemplate, HorizontalAlign align, int width, int height)
		{
			super(TemplateBuilderSlotType.DECORATIVE, align);
			this.innerTemplate = (ConstructedRecipeTemplateImplementation)innerTemplate;
			this.width = width;
			this.height = height;
		}
	}

	private static class SubunitSlot implements Slot
	{
		private int groupX, groupY, groupWidth, unitWidth, unitHeight;
		private HorizontalAlign alignment;
		private ConstructedRecipeTemplateImplementation template;

		public SubunitSlot(HorizontalAlign align, int xOffset, int yOffset, int width, int height, int totalWidth,
				ConstructedRecipeTemplateImplementation innerTemplate)
		{
			alignment = align;
			groupX = xOffset;
			groupY = yOffset;
			groupWidth = totalWidth;
			unitWidth = width;
			unitHeight = height;
			template = innerTemplate;
		}
		@Override
		public void draw(Renderer renderer, int x, int y, Object[] data, int dataIndex, boolean isMouseOver)
		{
			Object subunits[] = (Object[])data[dataIndex];
			final int units = subunits.length;
			int left = left(units);
			int perRow = perRow(units);
			for(int i = 0; i < subunits.length; i++)
			{
				int unitX = x + left + (i % perRow) * unitWidth;
				int unitY = y + groupY + (i / perRow) * unitHeight;
				Object subunit[] = (Object[])subunits[i];
				for(int j = 0; j < template.slots.length; j++)
				{
					template.slots[j].draw(renderer, unitX, unitY, subunit, j, isMouseOver);
				}
			}
		}
		@Override
		public ItemFilter getClickedFilter(int x, int y, Object[] data, int dataIndex)
		{
			Object subunits[] = (Object[])data[dataIndex];
			final int units = subunits.length;
			int left = left(units);
			int perRow = perRow(units);
			int rows = (units + perRow - 1) / perRow;
			int index = getIndex(x, y, left, units, perRow, rows);
			if(index < 0)
				return null;
			x = leftOf(x, index, left, perRow);
			y = topOf(y, index, perRow);
			Object subunit[] = (Object[])subunits[index];
			for(int i = 0; i < template.slots.length; i++)
			{
				Slot s = template.slots[i];
				if(s.isPointInBounds(x, y, subunit, i))
				{
					return s.getClickedFilter(x, y, subunit, i);
				}
			}
			return null;
		}
		@Override
		public boolean isPointInBounds(int x, int y, Object[] data, int dataIndex)
		{
			Object subunits[] = (Object[])data[dataIndex];
			final int units = subunits.length;
			int left = left(units);
			int perRow = perRow(units);
			int rows = (units + perRow - 1) / perRow;
			int index = getIndex(x, y, left, units, perRow, rows);
			if(index < 0)
				return false;
			x = leftOf(x, index, left, perRow);
			y = topOf(y, index, perRow);
			Object subunit[] = (Object[])subunits[index];
			for(int i = 0; i < template.slots.length; i++)
			{
				Slot s = template.slots[i];
				if(s.isPointInBounds(x, y, subunit, i))
					return true;
			}
			return false;
		}
		@Override
		public List<String> getTooltip(int x, int y, Object[] data, int dataIndex)
		{
			Object subunits[] = (Object[])data[dataIndex];
			final int units = subunits.length;
			int left = left(units);
			int perRow = perRow(units);
			int rows = (units + perRow - 1) / perRow;
			int index = getIndex(x, y, left, units, perRow, rows);
			if(index < 0)
				return null;
			x = leftOf(x, index, left, perRow);
			y = topOf(y, index, perRow);
			Object subunit[] = (Object[])subunits[index];
			for(int i = 0; i < template.slots.length; i++)
			{
				Slot s = template.slots[i];
				if(s.isPointInBounds(x, y, subunit, i))
				{
					return s.getTooltip(x, y, subunit, i);
				}
			}
			return null;
		}

		@Override
		public boolean matches(ItemFilter filter, Object[] data, int dataIndex, SlotType type)
		{
			Object subunits[] = (Object[])data[dataIndex];

			for(Object subunit: subunits)
			{
				Object innerData[] = (Object[])subunit;
				for(int i = 0; i < innerData.length; i++)
				{
					if(template.slots[i].matches(filter, innerData, i, type))
					{
						return true;
					}
				}
			}

			return false;
		}

		int leftOf(int x, int index, int left, int perRow)
		{
			return x - left - (index % perRow) * unitWidth;
		}

		int topOf(int y, int index, int perRow)
		{
			return y - groupY - (index / perRow) * unitHeight;
		}

		private int getIndex(int x, int y, int left, int units, int perRow, int rows)
		{
			if(x < left || x > left + perRow * unitWidth || y < groupY || y > groupY + rows * unitHeight)
				return -1;

			int i = (x - left) / unitWidth +
					perRow * (y - groupY) / unitHeight;
			return i < units? i : -1;
		}

		private int left(int itemCount)
		{
			if(alignment == HorizontalAlign.LEFT)
			{
				return groupX;
			}
			int perRow = perRow(itemCount);
			int unusedWidth = groupWidth - unitWidth * perRow;
			if(alignment == HorizontalAlign.RIGHT)
			{
				return unusedWidth + groupX;
			}
			else
			{
				return unusedWidth/2 + groupX;
			}
		}

		private int perRow(int itemCount)
		{
			return Math.min(groupWidth / unitWidth, itemCount);
		}
	}
}
