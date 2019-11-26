package net.islandearth.languagy.api;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class HookedPlugin {

	private Plugin plugin;
	private Material display;
	private File fallbackFolder;
	private File fallback;

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
}
