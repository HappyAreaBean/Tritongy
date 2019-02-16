package net.islandearth.languagy.api;

public class LanguagyAPI {

	private static Languagy languagy;
	
	public static void set(Languagy languagy) {
		if (LanguagyAPI.languagy != null) {
			throw new IllegalArgumentException("Plugin has already been set!");
		}
		
		LanguagyAPI.languagy = languagy;
	}
	
	public static Languagy get() {
		return languagy;
	}
}
