package uristqwerty.CraftGuide.client.ui.text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import uristqwerty.CraftGuide.CraftGuideLog;
import net.minecraft.util.StatCollector;

public class TranslatedTextSource extends TextSource
{
	private static List<WeakReference<TranslatedTextSource>> instances = new ArrayList<>();

	private String rawText;
	private String translatedText;

	public TranslatedTextSource(String text)
	{
		rawText = text;
		translatedText = translate(rawText);

		instances.add(new WeakReference<>(this));
	}

	@Override
	public String getText()
	{
		return translatedText;
	}

	public void setText(String text)
	{
		translatedText = translate(text);
		sendTextChanged();
	}

	private static String translate(String text)
	{
		return StatCollector.translateToLocal(text);
	}

	public static void reloadAll()
	{
		try
		{
			for(WeakReference<TranslatedTextSource> reference: instances)
			{
				if(reference != null)
				{
					TranslatedTextSource text = reference.get();

					if(text != null)
					{
						text.setText(text.rawText);
					}
				}
			}
		}
		catch(Exception e)
		{
			CraftGuideLog.log(e, "", true);
		}
	}
}
