package net.islandearth.languagy.api.language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LanguagyImplementation {

	/**
	 * Gets the default language of this implementation.
	 * @return default language, defaulting to {@link Language#ENGLISH}
	 */
	Language value() default Language.ENGLISH;

	/**
	 * Gets the default language folder of this implementation.
	 * @return the default language folder, defaulting to "lang"
	 */
	String defaultFolder() default "lang";
}
