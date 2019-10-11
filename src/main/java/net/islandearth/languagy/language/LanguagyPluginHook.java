package net.islandearth.languagy.language;

public interface LanguagyPluginHook {

	/**
	 * Called when Languagy has successfully hooked into your plugin.
	 * <p>
	 * Within this, you may access your annotated field and set options as desired, such as {@link Translator#setDisplay(org.bukkit.Material)}
	 */
	void onLanguagyHook();
}
