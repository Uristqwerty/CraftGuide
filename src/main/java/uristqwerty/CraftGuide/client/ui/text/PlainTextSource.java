package uristqwerty.CraftGuide.client.ui.text;

public class PlainTextSource extends TextSource
{
	private String text;

	public PlainTextSource(String text)
	{
		this.text = text;
	}

	@Override
	public String getText()
	{
		return text;
	}
}
