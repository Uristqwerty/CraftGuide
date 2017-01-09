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
	public RecipeTemplateBuilder setColumnAlign(VerticalAlign align);
	public RecipeTemplateBuilder setItemAlign(HorizontalAlign align);
	public RecipeTemplateBuilder nextSlotType(TemplateBuilderSlotType type);

	public ConstructedRecipeTemplate finishTemplate();

	public static enum TemplateBuilderSlotType
	{
		INPUT, OUTPUT, MACHINE, DECORATIVE;

		public boolean drawBackgroupnd()
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
		LEFt, CENTER, RIGHT
	}

}
