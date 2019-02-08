package net.islandearth.languagy.api;

public class LanguagyAPI {

	private static Languagy languagy;
	
	public static void set(Languagy languagy) {
		if (languagy != null) {
			throw new IllegalArgumentException("Plugin has already been set!");
		}
		
		LanguagyAPI.languagy = languagy;
	}
	
	public static Languagy getLanguagy() {
		return languagy;
	}
}
