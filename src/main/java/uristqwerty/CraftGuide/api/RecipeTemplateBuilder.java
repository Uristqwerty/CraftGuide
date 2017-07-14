package uristqwerty.CraftGuide.api;


/**
 * Prototype for a more convenient recipe API.
 */
public interface RecipeTemplateBuilder
{
	public RecipeTemplateBuilder nextColumn();
	public RecipeTemplateBuilder nextColumn(int gap);
	public RecipeTemplateBuilder shapelessItemGrid(int width, int height);
	public RecipeTemplateBuilder shapedItemGrid(int width, int height);
	public RecipeTemplateBuilder item();
	public RecipeTemplateBuilder outputItem();
	public RecipeTemplateBuilder machineItem();
	public RecipeTemplateBuilder chanceItem();
	public RecipeTemplateBuilder liquid();
	public RecipeTemplateBuilder text(int width);
	public RecipeTemplateBuilder textBlock(int width, int rows);
	public RecipeTemplateBuilder icon(int width, int height);
	public RecipeTemplateBuilder iconWithData(int width, int height, IconMode mode, int spaceForText);
	public RecipeTemplateBuilder setColumnAlign(VerticalAlign align);
	public RecipeTemplateBuilder setItemAlign(HorizontalAlign align);
	public RecipeTemplateBuilder nextSlotType(TemplateBuilderSlotType type);

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
}
