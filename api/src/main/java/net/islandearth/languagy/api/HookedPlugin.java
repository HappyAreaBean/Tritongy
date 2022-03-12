package net.islandearth.languagy.api;

import net.islandearth.languagy.api.language.Language;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HookedPlugin {

	private final Plugin plugin;
	private final File folder;
	private final File fallback;
	private final Map<Language, FileConfiguration> cachedLanguages = new ConcurrentHashMap<>();
	private boolean debug;

	public HookedPlugin(Plugin plugin, File folder, File fallback) {
		this.plugin = plugin;
		this.folder = folder;
		this.fallback = fallback;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public File getFolder() {
		return folder;
	}

	public File getFallback() {
		return fallback;
	}

	public Map<Language, FileConfiguration> getCachedLanguages() {
		return Collections.unmodifiableMap(cachedLanguages);
	}

	public void addCachedLanguage(Language language, FileConfiguration config) {
		cachedLanguages.put(language, config);
	}

	public void removeCachedLanguage(Language language) {
		cachedLanguages.remove(language);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
