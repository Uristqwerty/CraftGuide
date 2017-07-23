package uristqwerty.gui_craftguide.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a Texture class, indicating that it may be used in, and using what
 * human-convenient name, theme data files, and (hopefully) eventually the UI of
 * an in-game theme editor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TextureMeta
{
	public String name();

	/**
	 * Marks a field as being used by theme files and to be visible in an editor
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface TextureParameter
	{
	}

	/**
	 * For @TextureParameter arrays with a constant size, this annotation is
	 * used to declare what that size is.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface ListSize
	{
		public int value();
	}

	/**
	 * Identifies a constructor that can be called to directly create a Texture
	 * instance.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.CONSTRUCTOR})
	public @interface TextureInit
	{
	}

	/**
	 * Indicates that a @TextureParameter can be produced by multiple types of Object
	 * rather than written out directly. This allows shorthand notations in theme files,
	 * such as more easily representing a strip of animation frames or a bordered texture.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface GeneratedFrom
	{
		public Class<?>[] value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface ListElementType
	{
		public Class<?> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface WithUnits
	{
		public Unit[] value();
	}

	public @interface Unit
	{
		public double multiplier();
		public String[] names();
	}
}
