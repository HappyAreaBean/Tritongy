package net.islandearth.languagy.api;

import com.google.common.collect.ImmutableMap;
import net.islandearth.languagy.api.language.Language;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HookedPlugin {

	private final Plugin plugin;
	private Material display;
	private final File fallbackFolder;
	private final File fallback;
	private Map<Language, FileConfiguration> cachedLanguages = new ConcurrentHashMap<>();
	private boolean debug;

	public HookedPlugin(Plugin plugin, Material display, File fallbackFolder, File fallback) {
		this.plugin = plugin;
		this.display = display;
		this.fallbackFolder = fallbackFolder;
		this.fallback = fallback;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public Material getDisplay() {
		return display;
	}

	public void setDisplay(Material display) {
		this.display = display;
	}

	public File getFallbackFolder() {
		return fallbackFolder;
	}

	public File getFallback() {
		return fallback;
	}

	public Map<Language, FileConfiguration> getCachedLanguages() {
		return ImmutableMap.copyOf(cachedLanguages);
	}

	public void setCachedLanguages(Map<Language, FileConfiguration> cachedLanguages) {
		this.cachedLanguages = cachedLanguages;
	}

	public void addCachedLanguage(Language language, FileConfiguration config) {
		cachedLanguages.put(language, config);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
