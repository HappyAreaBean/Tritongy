package net.islandearth.languagy.language;

public class LanguageParser {
	
	/**
	 * 
	 * @param code - the language code, e.g "en_gb"
	 * @return The language. If the language does
	 * not exist within the enum, BRITISH_ENGLISH
	 * is returned as the fallback default.
	 */
	public static Language getFromCode(String code)
	{
		for(Language language : Language.values())
		{
			if(language.getCode().equals(code)) return language;
		} return Language.BRITISH_ENGLISH;
	}
}
