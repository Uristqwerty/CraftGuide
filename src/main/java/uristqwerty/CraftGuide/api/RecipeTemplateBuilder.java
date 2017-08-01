package uristqwerty.CraftGuide.api;


/**
 * An alternate API style, using method chaining rather than requiring
 * code to construct data arrays, and using a higher-level layout abstraction
 * (automatically aligning items into specified columns, rather than using
 * absolute pixel coordinates).
 * <br><br>
 * Ultimately, this is implemented as a layer on top of the older Slot-based
 * API (which in turn creates layout and structure around the lowest-level
 * CraftGuideRecipe interface). However, as a side effect of being limited
 * to a built-in set of types, theoretically a server implementation could
 * exist that serializes recipes and sends them to the client. Similarily,
 * there are vague future development ideas of allowing recipes to specify
 * a "layout name", unused by default, but allowing texture packs, themes,
 * and even the mods themselves to create alternate presentations of the
 * same data, rather than just being limited to changing textures in a way
 * that works with the fixed poisitions and sizes of the originally-specified
 * recipes.
 */
public interface RecipeTemplateBuilder
{
	public RecipeTemplateBuilder nextColumn();
	public RecipeTemplateBuilder nextColumn(int columnMargin);
	public RecipeTemplateBuilder shapelessItemGrid(int columns, int rows);
	public RecipeTemplateBuilder shapedItemGrid(int columns, int rows);
	public RecipeTemplateBuilder item();
	public RecipeTemplateBuilder outputItem();
	public RecipeTemplateBuilder machineItem();
	public RecipeTemplateBuilder chanceItem();
	public RecipeTemplateBuilder liquid();
	public RecipeTemplateBuilder text(int width, TextOverflow overflowHandling);
	public RecipeTemplateBuilder textBlock(int width, int rows, TextOverflow overflowHandling);
	public RecipeTemplateBuilder icon(int width, int height);
	public RecipeTemplateBuilder iconWithData(int width, int height, IconMode mode, int spaceForText);
	public RecipeTemplateBuilder setColumnAlign(VerticalAlign align);
	public RecipeTemplateBuilder setItemAlign(HorizontalAlign align);
	public RecipeTemplateBuilder nextSlotType(TemplateBuilderSlotType type);
	public RecipeTemplateBuilder repeatedSubunit(SubunitLayout layoutMode, SubunitDescriptor contents);

	public ConstructedRecipeTemplate finishTemplate();

	public static enum TemplateBuilderSlotType
	{
		INPUT, OUTPUT, MACHINE, DECORATIVE;

		public boolean drawBackground()
		{
			return this.equals(INPUT) || this.equals(OUTPUT);
		}

		public SlotType toSlotType()
		{
			return
				this.equals(INPUT)? SlotType.INPUT_SLOT :
				this.equals(OUTPUT)? SlotType.OUTPUT_SLOT :
				this.equals(MACHINE)? SlotType.MACHINE_SLOT :
					SlotType.DISPLAY_SLOT;
		}
	}

	public static enum VerticalAlign
	{
		TOP, CENTER, BOTTOM
	}

	public static enum HorizontalAlign
	{
		LEFT, CENTER, RIGHT
	}

	public static enum IconMode
	{
		PLAIN_ICON, ICON_AND_STACKSIZE, ICON_AND_LABEL
	}

	public static enum SubunitLayout
	{
		VERTICAL, HORIZONTAL_WRAPPING
	}

	public static enum TextOverflow
	{
		OVERFLOW, TRUNCATE, WRAP
	}

	/**
	 * Intended to be used with Java 8 lambdas, but I won't be requiring Java 8 for
	 * the final Minecraft 1.7.10 release, and I want this API as part of it.
	 * <br><br>
	 * Main inspiration is thinking through how I would implement something like
	 * ThaumCraft's aspects, and realizing that "find the max quantity up-front"
	 * is not the ideal solution.
	 */
	public static interface SubunitDescriptor
	{
		public void defineSubunit(RecipeTemplateBuilder innerBuilder);
	}
}
