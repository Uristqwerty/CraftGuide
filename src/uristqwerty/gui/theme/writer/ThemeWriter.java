package uristqwerty.gui.theme.writer;

import java.io.OutputStream;

import uristqwerty.gui.theme.Theme;

public interface ThemeWriter
{
	/**
	 * Writes a Theme to the specified OutputStream.
	 * <br><br>
	 * returns true on success, or false if there was any
	 * sort of failure.
	 * @param theme
	 * @param output
	 * @return
	 */
	public boolean write(Theme theme, OutputStream output);
}
