package uristqwerty.CraftGuide.client.ui.text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class TextSource
{
	public static interface TextChangeListener
	{
		public void onTextChanged(TextSource source);
	}

	private List<WeakReference<TextChangeListener>> listeners = new ArrayList<>();

	public void addListener(TextChangeListener listener)
	{
		listeners.add(new WeakReference<>(listener));
	}

	public void removeListener(TextChangeListener listener)
	{
		for(Iterator<WeakReference<TextChangeListener>> i = listeners.iterator(); i.hasNext();)
		{
			WeakReference<TextChangeListener> ref = i.next();
			if(ref.get() == listener)
			{
				i.remove();
			}
		}
	}

	protected void sendTextChanged()
	{
		for(WeakReference<TextChangeListener> ref: listeners)
		{
			TextChangeListener listener = ref.get();

			if(listener != null)
			{
				listener.onTextChanged(this);
			}
		}
	}

	public abstract String getText();
}
